package com.example.employeemanagementsystem.ui.fragments.loading;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.databinding.FragmentLoadingBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;

public class LoadingFragment extends Fragment {

    private FragmentLoadingBinding binding;

    private NavDestination currentDestination;

    private Disposable delayDisposable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoadingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(requireView());
        currentDestination = navController.getCurrentDestination();

        loading();
    }

    @SuppressLint("CheckResult")
    private void loading() {
        // Code to be executed on the main thread after delay is done
        Completable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(this::goToHomeFragment);
    }

    private void goToHomeFragment() {
        int currentDestinationId = currentDestination != null ? currentDestination.getId() : -1;

        if (currentDestinationId == R.id.loadingFragment) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_loadingFragment_to_homeFragment);
        }
    }
}