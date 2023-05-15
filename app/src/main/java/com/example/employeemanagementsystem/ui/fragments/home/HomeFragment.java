package com.example.employeemanagementsystem.ui.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private NavDestination currentDestination;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(requireView());
        currentDestination = navController.getCurrentDestination();

        setActions();
    }

    private void setActions() {
        binding.btnAddEmp.setOnClickListener(v -> goToAddUpdateEmpFragment());

        binding.btnShowAllEmp.setOnClickListener(v -> goToEmployeesFragment());

        binding.btnUseDataStructureArray.setOnClickListener(v -> goToArrayDataStructureFragment());

        binding.btnUseDataStructureLinkedList.setOnClickListener(v -> goToLInkedLIstDataStructureFragment());

        handleOnBackPressed();
    }

    private void handleOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });
    }

    private void goToAddUpdateEmpFragment() {
        int currentDestinationId = currentDestination != null ? currentDestination.getId() : -1;

        if (currentDestinationId == R.id.homeFragment) {
            Bundle bundle = new Bundle();
            bundle.putInt("add_or_update", 0);
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_homeFragment_to_addUpdateEmployeeFragment, bundle);
        }
    }

    private void goToEmployeesFragment() {
        int currentDestinationId = currentDestination != null ? currentDestination.getId() : -1;

        if (currentDestinationId == R.id.homeFragment) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_homeFragment_to_employeesFragment);
        }
    }

    private void goToArrayDataStructureFragment() {
        int currentDestinationId = currentDestination != null ? currentDestination.getId() : -1;

        if (currentDestinationId == R.id.homeFragment) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_homeFragment_to_arrayDataStructureFragment);
        }
    }

    private void goToLInkedLIstDataStructureFragment() {
        int currentDestinationId = currentDestination != null ? currentDestination.getId() : -1;

        if (currentDestinationId == R.id.homeFragment) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_homeFragment_to_linkedListDataStructureNFragment);
        }
    }
}