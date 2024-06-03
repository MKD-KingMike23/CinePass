package com.miguelrosa.cinepass.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.miguelrosa.cinepass.Adapters.FilmListAdapter;
import com.miguelrosa.cinepass.Adapters.SliderAdapter;
import com.miguelrosa.cinepass.Domain.AccountDetailsResponse;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.Domain.CreateSessionBody;
import com.miguelrosa.cinepass.Domain.Movie;
import com.miguelrosa.cinepass.Domain.PopularMoviesResponse;
import com.miguelrosa.cinepass.Domain.RequestTokenResponse;
import com.miguelrosa.cinepass.Domain.SessionResponse;
import com.miguelrosa.cinepass.Domain.SliderItem;
import com.miguelrosa.cinepass.Domain.TopRatedMoviesResponse;
import com.miguelrosa.cinepass.Domain.UpComingMoviesResponse;
import com.miguelrosa.cinepass.R;
import com.miguelrosa.cinepass.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView.Adapter adapterBestMovies, adapterUpComing, adapterTopRated;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String sessionId = getSessionId();

        fetchPopularMovies(1);
        fetchUpCommingMovies(1);
        fetchTopRatedMovies(1);
        binding.rv1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.rv2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.rv3.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        binding.favoritos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });
    }
    private void fetchPopularMovies(int page) {
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<PopularMoviesResponse> call = ApiClient.getTmdbApiService().getPopularMovies(apiKey, page, language);

        call.enqueue(new Callback<PopularMoviesResponse>() {
            @Override
            public void onResponse(Call<PopularMoviesResponse> call, retrofit2.Response<PopularMoviesResponse> response) {
                if (response.isSuccessful()) {
                    PopularMoviesResponse popularMoviesResponse = response.body();
                    if (popularMoviesResponse != null) {
                        List<Movie> movies = popularMoviesResponse.getResults();
                        adapterBestMovies = new FilmListAdapter(movies);
                        binding.rv1.setAdapter(adapterBestMovies);
                        binding.progressBar1.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    binding.progressBar1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTopRatedMovies(int page) {
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<TopRatedMoviesResponse> call = ApiClient.getTmdbApiService().getTopRatedMovies(apiKey, page, language);

        call.enqueue(new Callback<TopRatedMoviesResponse>() {
            @Override
            public void onResponse(Call<TopRatedMoviesResponse> call, retrofit2.Response<TopRatedMoviesResponse> response) {
                if (response.isSuccessful()) {
                    TopRatedMoviesResponse topratedMoviesResponse = response.body();
                    if (topratedMoviesResponse != null) {
                        List<Movie> movies = topratedMoviesResponse.getResults();
                        adapterTopRated = new FilmListAdapter(movies);
                        binding.rv2.setAdapter(adapterTopRated);
                        binding.progressBar2.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    binding.progressBar2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TopRatedMoviesResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUpCommingMovies(int page) {
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<UpComingMoviesResponse> call = ApiClient.getTmdbApiService().getUpComingMovies(apiKey, page, language);

        call.enqueue(new Callback<UpComingMoviesResponse>() {
            @Override
            public void onResponse(Call<UpComingMoviesResponse> call, retrofit2.Response<UpComingMoviesResponse> response) {
                if (response.isSuccessful()) {
                    UpComingMoviesResponse upcomingMoviesResponse = response.body();
                    if (upcomingMoviesResponse != null) {
                        List<Movie> movies = upcomingMoviesResponse.getResults();
                        adapterUpComing = new FilmListAdapter(movies);
                        binding.rv3.setAdapter(adapterUpComing);
                        binding.progressBar3.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    binding.progressBar3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UpComingMoviesResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getSessionId() {
        SharedPreferences preferences = getSharedPreferences("CinePassPrefs", MODE_PRIVATE);
        String sessionId = preferences.getString("sessionId", null);
        Log.i("MainActivity", "ID DE SESION CARGADO: " + sessionId);
        return sessionId;
    }
}