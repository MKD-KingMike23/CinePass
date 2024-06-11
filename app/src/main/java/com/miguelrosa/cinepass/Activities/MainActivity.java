package com.miguelrosa.cinepass.Activities;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.miguelrosa.cinepass.Activities.Fragments.DashboardFragment;
import com.miguelrosa.cinepass.Activities.Fragments.ListsFragment;
import com.miguelrosa.cinepass.Activities.Fragments.ProfileFragment;
import com.miguelrosa.cinepass.R;
import com.miguelrosa.cinepass.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new DashboardFragment());
        binding.bottomNavigation.setOnItemSelectedListener(this);

        String fragment = getIntent().getStringExtra("fragment");
        if (fragment != null) {
            if (fragment.equals("listas")) {
                replaceFragment(new ListsFragment());
            } else {
                replaceFragment(new DashboardFragment());
            }
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int IdBotonPulsado = menuItem.getItemId();

        int[] optionMenu = new int[binding.bottomNavigation.getMenu().size()];
        for (int i=0; i<binding.bottomNavigation.getMenu().size(); i++) {
            optionMenu[i]=binding.bottomNavigation.getMenu().getItem(i).getItemId();
        }

        if(optionMenu[0]==IdBotonPulsado){
            replaceFragment(new ProfileFragment());
        }

        if(optionMenu[1]==IdBotonPulsado) {
            replaceFragment(new DashboardFragment());
        }

        if (optionMenu[2]==IdBotonPulsado) {
            replaceFragment(new ListsFragment());
        }

        return true;
    }
}
