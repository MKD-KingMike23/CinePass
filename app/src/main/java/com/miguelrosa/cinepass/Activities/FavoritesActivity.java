package com.miguelrosa.cinepass.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.miguelrosa.cinepass.Adapters.FilmListAdapter;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.Domain.CreateSessionBody;
import com.miguelrosa.cinepass.Domain.Movie;
import com.miguelrosa.cinepass.Domain.MovieResponse;
import com.miguelrosa.cinepass.Domain.RequestTokenResponse;
import com.miguelrosa.cinepass.Domain.SessionResponse;
import com.miguelrosa.cinepass.R;
import com.miguelrosa.cinepass.databinding.ActivityFavoritesBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {
    private ActivityFavoritesBinding binding;
    private FilmListAdapter filmListAdapter;
    private List<Movie> favoriteMovies = new ArrayList<>();
    private int accountId = 21244357;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getSharedPreferences("CinePassPrefs", MODE_PRIVATE);
        sessionId = preferences.getString("sessionId", null);

        binding.favoriteMoviesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        filmListAdapter = new FilmListAdapter(favoriteMovies);
        binding.favoriteMoviesRecycler.setAdapter(filmListAdapter);

        fetchFavoriteMovies(sessionId);
    }

    private void fetchFavoriteMovies(String sessionId) {
        binding.progressBarFavorites.setVisibility(View.VISIBLE);
        String apiKey = ApiClient.getApiKey();
        Call<MovieResponse> call = ApiClient.getTmdbApiService().getFavoriteMovies(accountId, apiKey, sessionId);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                binding.progressBarFavorites.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        favoriteMovies.clear();
                        favoriteMovies.addAll(movieResponse.getResults());
                        filmListAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(FavoritesActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                binding.progressBarFavorites.setVisibility(View.GONE);
                Toast.makeText(FavoritesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}