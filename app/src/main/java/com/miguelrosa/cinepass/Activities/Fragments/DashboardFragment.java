package com.miguelrosa.cinepass.Activities.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.miguelrosa.cinepass.R;
import com.miguelrosa.cinepass.databinding.FragmentDashboardBinding;
public class DashboardFragment extends Fragment {

    FragmentDashboardBinding binding;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initial fragment to be displayed
        replaceFragment(new MoviesFragment());

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Películas"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Series"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        replaceFragment(new MoviesFragment());
                        break;
                    case 1:
                        replaceFragment(new TvSeriesFragment());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No-op
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No-op
            }
        });

        if (savedInstanceState == null) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0));
        }

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_dashboard, fragment);
        transaction.commit();
    }
}