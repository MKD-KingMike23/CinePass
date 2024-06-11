package com.miguelrosa.cinepass.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.miguelrosa.cinepass.Adapters.FilmListAdapter;
import com.miguelrosa.cinepass.Adapters.MovieSearchAdapter;
import com.miguelrosa.cinepass.Adapters.ViewPagerAdapter;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.Domain.Models.Movie;
import com.miguelrosa.cinepass.Domain.Responses.MovieResponse;
import com.miguelrosa.cinepass.Domain.Responses.PopularMoviesResponse;
import com.miguelrosa.cinepass.Domain.Responses.TopRatedMoviesResponse;
import com.miguelrosa.cinepass.Domain.Responses.UpComingMoviesResponse;
import com.miguelrosa.cinepass.R;
import com.miguelrosa.cinepass.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Películas");
                    break;
                case 1:
                    tab.setText("Series de TV");
                    break;
            }
        }).attach();
    }
}