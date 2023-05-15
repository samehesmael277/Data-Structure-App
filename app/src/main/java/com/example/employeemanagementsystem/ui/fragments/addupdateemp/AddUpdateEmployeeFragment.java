package com.example.employeemanagementsystem.ui.fragments.addupdateemp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.data.room.AppDatabase;
import com.example.employeemanagementsystem.databinding.FragmentAddUpdateEmployeeBinding;
import com.example.employeemanagementsystem.model.Employee;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddUpdateEmployeeFragment extends Fragment {

    private FragmentAddUpdateEmployeeBinding binding;

    private AppDatabase db;
    private Disposable disposable;

    private NavDestination currentDestination;

    private int addOrUpdate;

    private Employee empArgs;

    private final Employee currentEmpForAdd = new Employee();

    private static final String TAG = "myTAG";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddUpdateEmployeeBinding.inflate(inflater, container, false);

        db = AppDatabase.getInstance(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(requireView());
        currentDestination = navController.getCurrentDestination();

        getArgs();
        setActions();
        if (addOrUpdate == 1)
            handleUiToUpdate();
    }

    private void getArgs() {
        assert getArguments() != null;
        addOrUpdate = getArguments().getInt("add_or_update");
        empArgs = getArguments().getParcelable("employee");
    }

    private void handleUiToUpdate() {
        binding.tvAddUpdateEmp.setText("Update Employee");
        binding.imgBtnDelete.setVisibility(View.VISIBLE);

        binding.edEmpName.setText(empArgs.getEmpName());
        binding.edEmpDept.setText(empArgs.getEmpDept());
        binding.edEmpDob.setText(empArgs.getEmpDob());
        binding.edEmpSalary.setText(empArgs.getEmpSalary() + "");
    }

    private void setActions() {
        binding.imgBtnBack.setOnClickListener(v -> {
            if (addOrUpdate == 0)
                goToHomeFragment();
            else
                goToEmployeesFragment();
        });

        binding.imgBtnSave.setOnClickListener(v -> getDataFromUi());

        binding.imgBtnDelete.setOnClickListener(v -> {
            loading(true);
            deleteEmployee();
        });
    }

    private void getDataFromUi() {
        String empName = binding.edEmpName.getText().toString();
        String empDept = binding.edEmpDept.getText().toString();
        String empDob = binding.edEmpDob.getText().toString();
        String empSalary = binding.edEmpSalary.getText().toString();

        boolean validation = empValidation(empName, empDept, empDob, empSalary);

        if (validation) {
            setDataToCurrentEmp(empName, empDept, empDob, empSalary);
        } else {
            vibration();
        }
    }

    private void setDataToCurrentEmp(String empName, String empDept, String empDob, String empSalary) {
        currentEmpForAdd.setEmpName(empName);
        currentEmpForAdd.setEmpDept(empDept);
        currentEmpForAdd.setEmpDob(empDob);
        currentEmpForAdd.setEmpSalary(Long.parseLong(empSalary));

        loading(true);
        if (addOrUpdate == 0) {
            currentEmpForAdd.setEmpNo(0);
            insertAddCurrentEmpToDB();
        } else {
            currentEmpForAdd.setEmpNo(empArgs.getEmpNo());
            updateEmployee();
        }
    }

    private void insertAddCurrentEmpToDB() {
        disposable = Completable.fromRunnable(() -> db.employeeDao().insert(currentEmpForAdd))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    // Insertion completed successfully
                    loading(false);
                    showToast("Inserted successful");
                    goToHomeFragment();
                }, throwable -> {
                    // Handle the error
                    loading(false);
                    showToast("Failed to insert try again");
                });

    }

    private void updateEmployee() {
        disposable = Completable.fromRunnable(() -> db.employeeDao().update(currentEmpForAdd))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    // Insertion completed successfully
                    loading(false);
                    showToast("Updated successful");
                    goToEmployeesFragment();
                }, throwable -> {
                    // Handle the error
                    loading(false);
                    showToast("Failed to update try again");
                });
    }

    private void deleteEmployee() {
        disposable = Completable.fromRunnable(() -> db.employeeDao().delete(empArgs))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    // Insertion completed successfully
                    loading(false);
                    showToast("deleted successful");
                    goToEmployeesFragment();
                }, throwable -> {
                    // Handle the error
                    loading(false);
                    showToast("Failed to delete try again");
                });
    }

    private boolean empValidation(String empName, String empDept, String empDob, String empSalary) {
        if (empName.isEmpty()) {
            binding.edEmpName.setError("Require Field");
        }
        if (empDept.isEmpty()) {
            binding.edEmpDept.setError("Require Field");
        }
        if (empDob.isEmpty()) {
            binding.edEmpDob.setError("Require Field");
        }
        if (empSalary.isEmpty()) {
            binding.edEmpSalary.setError("Require Field");
        }

        return !empName.isEmpty() &&
                !empDept.isEmpty() &&
                !empDob.isEmpty() &&
                !empSalary.isEmpty();
    }

    private void loading(boolean loading) {
        if (loading)
            binding.mkLoader.setVisibility(View.VISIBLE);
        else
            binding.mkLoader.setVisibility(View.GONE);
    }

    private void showToast(String text) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void goToHomeFragment() {
        int currentDestinationId = currentDestination != null ? currentDestination.getId() : -1;

        if (currentDestinationId == R.id.addUpdateEmployeeFragment) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_addUpdateEmployeeFragment_to_homeFragment);
        }
    }

    private void goToEmployeesFragment() {
        int currentDestinationId = currentDestination != null ? currentDestination.getId() : -1;

        if (currentDestinationId == R.id.addUpdateEmployeeFragment) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_addUpdateEmployeeFragment_to_employeesFragment);
        }
    }

    private void vibration() {
        Vibrator v = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
                disposable = null;
            }
        } catch (Exception e) {
            Log.d(TAG, "onDestroy: " + e.getMessage());
        }
    }
}