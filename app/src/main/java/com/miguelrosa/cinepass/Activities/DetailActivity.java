package com.miguelrosa.cinepass.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.miguelrosa.cinepass.Adapters.GenreAdapter;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.Domain.FavoriteRequest;
import com.miguelrosa.cinepass.Domain.FavoriteResponse;
import com.miguelrosa.cinepass.Domain.Genre;
import com.miguelrosa.cinepass.Domain.GenreResponse;
import com.miguelrosa.cinepass.Domain.Movie;
import com.miguelrosa.cinepass.Domain.MovieResponse;
import com.miguelrosa.cinepass.R;
import com.miguelrosa.cinepass.databinding.ActivityDetailBinding;
import com.miguelrosa.cinepass.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private int movieId;
    private RecyclerView.Adapter genreAdapter;
    private List<Genre> genreList = new ArrayList<>();
    private boolean isFavorite = false;
    private int accountId = 21244357;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getSharedPreferences("CinePassPrefs", MODE_PRIVATE);
        sessionId = preferences.getString("sessionId", null);

        movieId = getIntent().getIntExtra("id", 0);
        binding.genreRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        genreAdapter = new GenreAdapter(genreList);
        binding.genreRecycler.setAdapter(genreAdapter);

        binding.imageViewBack.setOnClickListener(v -> finish());
        fetchMovieDetails(movieId);

        binding.imageViewFav.setOnClickListener(v -> toggleFavorite());
    }

    private void fetchMovieDetails(int movieId) {
        binding.progressBarDetail.setVisibility(View.VISIBLE);
        binding.scrollViewDetail.setVisibility(View.GONE);
        String apiKey = ApiClient.getApiKey();
        Call<Movie> call = ApiClient.getTmdbApiService().getMovieDetails(movieId, apiKey);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();
                    if (movie != null) {
                        binding.progressBarDetail.setVisibility(View.GONE);
                        binding.scrollViewDetail.setVisibility(View.VISIBLE);

                        binding.movieNameTxt.setText(movie.getTitle());
                        binding.movieSummary.setText(movie.getOverview());
                        binding.movieReleaseDate.setText(movie.getReleaseDate());
                        binding.movieRuntime.setText(String.valueOf(movie.getRuntime())+ " min");
                        binding.movieStar.setText(String.valueOf(movie.getVoteAverage()));
                        binding.movieTime.setText(String.valueOf(movie.getVoteCount()));

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));

                        Glide.with(DetailActivity.this)
                                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                                .apply(requestOptions)
                                .into(binding.picDetail);

                        List<Genre> genres = movie.getGenres();
                        if (genres != null) {
                            genreAdapter = new GenreAdapter(genres);
                            binding.genreRecycler.setAdapter(genreAdapter);
                        } else {
                            Log.i("DetailActivity", "La película no tiene géneros asociados");
                        }
                        checkIfFavorite(movieId, sessionId);
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleFavorite() {
        String apiKey = ApiClient.getApiKey();
        FavoriteRequest request = new FavoriteRequest("movie", movieId, !isFavorite);

        Call<FavoriteResponse> call = ApiClient.getTmdbApiService().markAsFavorite(accountId, apiKey, sessionId, request);
        call.enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(Call<FavoriteResponse> call, Response<FavoriteResponse> response) {
                if (response.isSuccessful()) {
                    isFavorite = !isFavorite;
                    updateFavoriteIcon();
                    Toast.makeText(DetailActivity.this, "Favorite status changed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "Error: 1" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FavoriteResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: 2" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfFavorite(int movieId, String sessionId) {
        String apiKey = ApiClient.getApiKey();
        Call<MovieResponse> call = ApiClient.getTmdbApiService().getFavoriteMovies(accountId, apiKey, sessionId);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        List<Movie> favoriteMovies = movieResponse.getResults();
                        for (Movie movie : favoriteMovies) {
                            if (movie.getId() == movieId) {
                                isFavorite = true;
                                updateFavoriteIcon();
                                Log.i("CHECKEO","EL CHECKEO FUNCIONA: " + isFavorite);
                                return;
                            }
                        }
                    }
                    isFavorite = false;
                    Log.i("CHECKEO","EL CHECKEO FUNCIONA: " + isFavorite);
                    updateFavoriteIcon();
                } else {
                    Toast.makeText(DetailActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavoriteIcon() {
        if (isFavorite) {
            binding.imageViewFav.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            binding.imageViewFav.setImageResource(R.drawable.baseline_favorite_border_24);
        }
    }
}