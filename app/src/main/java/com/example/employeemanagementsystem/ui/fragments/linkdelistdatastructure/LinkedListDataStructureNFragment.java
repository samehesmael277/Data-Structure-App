package com.example.employeemanagementsystem.ui.fragments.linkdelistdatastructure;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.data.room.AppDatabase;
import com.example.employeemanagementsystem.databinding.FragmentLinkedListDataStructureNBinding;
import com.example.employeemanagementsystem.datastructure.EmployeeLinkedList;
import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.ui.adapter.EmpAdapter;
import com.example.employeemanagementsystem.ui.adapter.OnEmpClickListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class LinkedListDataStructureNFragment extends Fragment implements OnEmpClickListener {

    private FragmentLinkedListDataStructureNBinding binding;

    private EmployeeLinkedList employeeLInkedList = new EmployeeLinkedList();

    private NavDestination currentDestination;

    private EmpAdapter empAdapter;

    private AppDatabase db;

    private Disposable disposable;

    private static final String TAG = "myTAG";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLinkedListDataStructureNBinding.inflate(inflater, container, false);

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

        binding.btnShuffleReset.setOnClickListener(v -> {
            binding.edNumberForSearch.setText("");
            loading(true);
            shuffleEmpList();
        });

        binding.btnInsertionSort.setOnClickListener(v -> {
            binding.edNumberForSearch.setText("");
            loading(true);
            insertionSort();
        });

        binding.btnHeapSort.setOnClickListener(v -> {
            binding.edNumberForSearch.setText("");
            loading(true);
            heapSort();
        });

        binding.btnLinearSearch.setOnClickListener(v -> {
            loading(true);
            linearSearchValidation();
        });

        binding.btnBinarySearch.setOnClickListener(v -> {
            loading(true);
            binarySearchValidation();
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
                    if (!entities.isEmpty()) {
                        handleListToPassToLinkedListDataStructure(entities);
                    } else {
                        loading(false);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void handleListToPassToLinkedListDataStructure(List<Employee> employees) {
        Completable.fromRunnable(() -> {
                    // Code to be executed on background thread
                    for (Employee emp : employees) {
                        employeeLInkedList.insert(emp);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getAllEmpFromLinkedListAndSetInAdapter);
    }

    @SuppressLint("CheckResult")
    private void shuffleEmpList() {
        Completable.fromRunnable(() -> {
                    // Code to be executed on background thread
                    employeeLInkedList.shuffle();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getAllEmpFromLinkedListAndSetInAdapter);
    }

    @SuppressLint("CheckResult")
    private void insertionSort() {
        Completable.fromRunnable(() -> {
                    // Code to be executed on background thread
                    employeeLInkedList.insertionSort();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getAllEmpFromLinkedListAndSetInAdapter);
    }

    @SuppressLint("CheckResult")
    private void heapSort() {
        Completable.fromRunnable(() -> {
                    // Code to be executed on background thread
                    employeeLInkedList.heapSort();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getAllEmpFromLinkedListAndSetInAdapter);
    }

    private void linearSearchValidation() {
        String empNUmber = binding.edNumberForSearch.getText().toString();
        if (empNUmber.isEmpty()) {
            binding.edNumberForSearch.setError("Require Field");
            loading(false);
            vibration();
            return;
        }
        linearSearch(Integer.parseInt(empNUmber));
    }

    private void binarySearchValidation() {
        String empNUmber = binding.edNumberForSearch.getText().toString();
        if (empNUmber.isEmpty()) {
            binding.edNumberForSearch.setError("Require Field");
            loading(false);
            vibration();
            return;
        }
        binarySearch(Integer.parseInt(empNUmber));
    }

    @SuppressLint("CheckResult")
    private void linearSearch(int empNum) {
        Observable.fromCallable(() -> {
                    // Code to be executed on background thread
                    int result = employeeLInkedList.linearSearch(empNum);
                    if (result == -1) {
                        throw new RuntimeException("Employee not found");
                    } else {
                        return employeeLInkedList.getEmployeeByIndex(result);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    // Code to be executed on the main thread after background work is done
                    if (result != null) {
                        handleResultOfSearch(result);
                    } else {
                        showToast("Employee Not Found");
                        loading(false);
                    }
                }, error -> {
                    // Code to be executed when an error occurs
                    showToast(error.getMessage());
                    loading(false);
                });
    }

    @SuppressLint("CheckResult")
    private void binarySearch(int empNum) {
        Observable.fromCallable(() -> {
                    // Code to be executed on background thread
                    employeeLInkedList.insertionSort();
                    int result = employeeLInkedList.binarySearch(empNum);
                    if (result == -1) {
                        throw new RuntimeException("Employee not found");
                    } else {
                        Employee emp = employeeLInkedList.getEmployeeByIndex(result);
                        if (emp == null) {
                            throw new RuntimeException("Employee not found");
                        } else
                            return emp;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    // Code to be executed on the main thread after background work is done
                    if (result != null) {
                        handleResultOfSearch(result);
                    } else {
                        showToast("Employee Not Found");
                        loading(false);
                    }
                }, error -> {
                    // Code to be executed when an error occurs
                    Log.d(TAG, "binarySearch: " + error.getMessage());
                    showToast(error.getMessage());
                    loading(false);
                });
    }

    private void handleResultOfSearch(Employee empResult) {
        ArrayList<Employee> empResultList = new ArrayList<>();
        empResultList.add(empResult);
        empAdapter.setData(empResultList);
        loading(false);
    }

    @SuppressLint("CheckResult")
    private void getAllEmpFromLinkedListAndSetInAdapter() {
        Observable.fromCallable(() -> {
                    // Code to be executed on background thread
                    return employeeLInkedList.getEmployees();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    // Code to be executed on the main thread after background work is done
                    empAdapter.setData(result);
                    loading(false);
                });
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

        if (currentDestinationId == R.id.linkedListDataStructureNFragment) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_linkedListDataStructureNFragment_to_homeFragment);
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
    public void onEmpClick(Employee emp) {
        showToast(emp.getEmpName());
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