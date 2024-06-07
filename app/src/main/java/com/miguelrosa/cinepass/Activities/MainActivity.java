package com.miguelrosa.cinepass.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.miguelrosa.cinepass.Adapters.FilmListAdapter;
import com.miguelrosa.cinepass.Adapters.MovieSearchAdapter;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.Domain.Movie;
import com.miguelrosa.cinepass.Domain.MovieResponse;
import com.miguelrosa.cinepass.Domain.PopularMoviesResponse;
import com.miguelrosa.cinepass.Domain.TopRatedMoviesResponse;
import com.miguelrosa.cinepass.Domain.UpComingMoviesResponse;
import com.miguelrosa.cinepass.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterBestMovies, adapterUpComing, adapterTopRated, movieSearchAdapter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String sessionId = getSessionId();

        fetchMainMovies();

        binding.rv1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rv2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rv3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.perfil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        binding.favoritos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListsActivity.class);
            startActivity(intent);
        });

        binding.rvsearch.setLayoutManager(new LinearLayoutManager(this));
        movieSearchAdapter = new MovieSearchAdapter();
        binding.rvsearch.setAdapter(movieSearchAdapter);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMovies(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchMovies(newText);

                if (newText.isEmpty()) {
                    fetchMainMovies();
                }
                return false;
            }
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

    private void searchMovies(String query) {
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<MovieResponse> call = ApiClient.getTmdbApiService().searchMovies(apiKey, query, language);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        List<Movie> searchResults = movieResponse.getResults();
                        ((MovieSearchAdapter) movieSearchAdapter).updateMovies(searchResults);

                        binding.rvsearch.setVisibility(View.VISIBLE);
                        binding.rv1.setVisibility(View.INVISIBLE);
                        binding.rv2.setVisibility(View.INVISIBLE);
                        binding.rv3.setVisibility(View.INVISIBLE);
                        binding.textView9.setVisibility(View.INVISIBLE);
                        binding.textView10.setVisibility(View.INVISIBLE);
                        binding.textView11.setVisibility(View.INVISIBLE);

                        Log.i("RESULTADOS FILTRADOS", movieResponse.getResults().toString());

                        if (movieResponse.getResults().toString().equals("[]")) {
                            fetchMainMovies();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchMainMovies() {
        binding.rvsearch.setVisibility(View.INVISIBLE);
        binding.rv1.setVisibility(View.VISIBLE);
        binding.rv2.setVisibility(View.VISIBLE);
        binding.rv3.setVisibility(View.VISIBLE);
        binding.textView9.setVisibility(View.VISIBLE);
        binding.textView10.setVisibility(View.VISIBLE);
        binding.textView11.setVisibility(View.VISIBLE);

        fetchPopularMovies(1);
        fetchUpCommingMovies(1);
        fetchTopRatedMovies(1);
    }

    private String getSessionId() {
        SharedPreferences preferences = getSharedPreferences("cinepass_preferences", MODE_PRIVATE);
        return preferences.getString("session_id", "");
    }
}