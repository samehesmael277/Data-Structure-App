package com.example.employeemanagementsystem.ui.fragments.employees;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.data.room.AppDatabase;
import com.example.employeemanagementsystem.databinding.FragmentEmployeesBinding;
import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.ui.adapter.EmpAdapter;
import com.example.employeemanagementsystem.ui.adapter.OnEmpClickListener;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class EmployeesFragment extends Fragment implements OnEmpClickListener {

    private FragmentEmployeesBinding binding;

    private NavDestination currentDestination;

    private EmpAdapter empAdapter;

    private AppDatabase db;

    private Disposable disposable;

    private static final String TAG = "myTAG";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEmployeesBinding.inflate(inflater, container, false);

        db = AppDatabase.getInstance(requireContext());
        empAdapter = new EmpAdapter(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(requireView());
        currentDestination = navController.getCurrentDestination();

        setActions();
        setupRecView();
        loading(true);
        getAllEmp();
    }

    private void setActions() {
        binding.imgBtnBack.setOnClickListener(v -> goToHomeFragment());

        binding.imgBtnDeleteAll.setOnClickListener(v -> confirmDeleteAllEmployee());

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    loading(true);
                    searchByEmpNumber(query);
                } else {
                    getAllEmp();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    loading(true);
                    searchByEmpNumber(newText);
                } else {
                    getAllEmp();
                }
                return true;
            }
        });
    }

    private void setupRecView() {
        binding.recViewEmp.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(empAdapter);
        alphaInAnimationAdapter.setDuration(200);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setFirstOnly(false);
        binding.recViewEmp.setAdapter(new ScaleInAnimationAdapter(alphaInAnimationAdapter));
    }

    private void getAllEmp() {
        disposable = Single.fromCallable(() -> db.employeeDao().getAllEmployees())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entities -> {
                    // Update the UI with the entities
                    loading(false);
                    if (!entities.isEmpty()) {
                        empAdapter.setData(entities);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void searchByEmpNumber(String empName) {
        Observable.fromCallable(() -> {
                    // Code to be executed on background thread
                    return db.employeeDao().searchOnEmployees("%" + empName + "%");
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    // Code to be executed on the main thread after background work is done
                    if (result != null) {
                        empAdapter.setData(result);
                    }
                    loading(false);
                });
    }

    private void confirmDeleteAllEmployee() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete All Employee")
                .setMessage("Are you sure to delete all data");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Code to execute when the "Yes" button is clicked.
                DeleteAllEmployee();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Code to execute when the "No" button is clicked.
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressLint("CheckResult")
    private void DeleteAllEmployee() {
        Completable.fromRunnable(() -> {
                    // Code to be executed on background thread
                    db.employeeDao().deleteAll();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            empAdapter.setData(new ArrayList<Employee>());
                        }
                );
    }

    private void showToast(String text) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void loading(boolean loading) {
        if (loading)
            binding.mkLoader.setVisibility(View.VISIBLE);
        else
            binding.mkLoader.setVisibility(View.GONE);
    }

    private void goToHomeFragment() {
        int currentDestinationId = currentDestination != null ? currentDestination.getId() : -1;

        if (currentDestinationId == R.id.employeesFragment) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_employeesFragment_to_homeFragment);
        }
    }

    private void goToUpdateFragment(Employee emp) {
        int currentDestinationId = currentDestination != null ? currentDestination.getId() : -1;

        if (currentDestinationId == R.id.employeesFragment) {
            Bundle bundle = new Bundle();
            bundle.putInt("add_or_update", 1);
            bundle.putParcelable("employee", emp);
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_employeesFragment_to_addUpdateEmployeeFragment, bundle);
        }
    }

    @Override
    public void onEmpClick(Employee emp) {
        goToUpdateFragment(emp);
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