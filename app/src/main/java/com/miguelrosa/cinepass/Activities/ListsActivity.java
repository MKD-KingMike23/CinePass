package com.miguelrosa.cinepass.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.miguelrosa.cinepass.Adapters.ListAdapter;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.Domain.Movie;
import com.miguelrosa.cinepass.Domain.MovieResponse;
import com.miguelrosa.cinepass.R;
import com.miguelrosa.cinepass.databinding.ActivityListsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListsActivity extends AppCompatActivity {
    private ActivityListsBinding binding;
    private ListAdapter listAdapter;
    private List<Movie> movieList = new ArrayList<>();
    private Spinner listSelectorSpinner;
    private String sessionId;
    private int accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getSharedPreferences("CinePassPrefs", MODE_PRIVATE);
        sessionId = preferences.getString("sessionId", null);
        accountId = preferences.getInt("accountId", -1);

        binding.favoriteMoviesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listAdapter = new ListAdapter(movieList);
        binding.favoriteMoviesRecycler.setAdapter(listAdapter);

        String lista = getIntent().getStringExtra("lista");
        if (lista != null) {
            if (lista.equals("watchlist")) {
                fetchWatchlistMovies(sessionId);
            } else if (lista.equals("favoritos")) {
                fetchFavoriteMovies(sessionId);
            }
        }

        setupListSelectorSpinner();

        binding.inicio.setOnClickListener(v -> {
            Intent intent = new Intent(ListsActivity.this, DashboardActivity.class);
            startActivity(intent);
        });

        binding.perfil.setOnClickListener(v -> {
            Intent intent = new Intent(ListsActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void setupListSelectorSpinner() {
        listSelectorSpinner = binding.listSelectorSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lists_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listSelectorSpinner.setAdapter(adapter);

        listSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedList = parent.getItemAtPosition(position).toString();
                if (selectedList.equals("Favoritos")) {
                    fetchFavoriteMovies(sessionId);
                } else if (selectedList.equals("Watchlist")) {
                    fetchWatchlistMovies(sessionId);
                } else {
                    binding.favoritesTitle.setText("Lista de Películas");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void fetchFavoriteMovies(String sessionId) {
        binding.progressBarFavorites.setVisibility(View.VISIBLE);
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<MovieResponse> call = ApiClient.getTmdbApiService().getFavoriteMovies(accountId, apiKey, sessionId, language);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                binding.progressBarFavorites.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        binding.favoritesTitle.setText("Películas Favoritas");
                        movieList.clear();
                        movieList.addAll(movieResponse.getResults());
                        listAdapter.notifyDataSetChanged();
                    }
                } else {
                    binding.progressBarFavorites.setVisibility(View.VISIBLE);
                    Toast.makeText(ListsActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                binding.progressBarFavorites.setVisibility(View.VISIBLE);
                Toast.makeText(ListsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchWatchlistMovies(String sessionId) {
        binding.progressBarFavorites.setVisibility(View.VISIBLE);
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<MovieResponse> call = ApiClient.getTmdbApiService().getWatchlistMovies(accountId, apiKey, sessionId, language);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                binding.progressBarFavorites.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        binding.favoritesTitle.setText("Películas Pendientes");
                        movieList.clear();
                        movieList.addAll(movieResponse.getResults());
                        listAdapter.notifyDataSetChanged();
                    }
                } else {
                    binding.progressBarFavorites.setVisibility(View.VISIBLE);
                    Toast.makeText(ListsActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                binding.progressBarFavorites.setVisibility(View.VISIBLE);
                Toast.makeText(ListsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}