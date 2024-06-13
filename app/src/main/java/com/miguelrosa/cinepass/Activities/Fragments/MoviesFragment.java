package com.miguelrosa.cinepass.Activities.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miguelrosa.cinepass.Adapters.Movie.MovieAdapter;
import com.miguelrosa.cinepass.Adapters.Movie.MovieSearchAdapter;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.Domain.Models.Movie;
import com.miguelrosa.cinepass.Domain.Responses.MovieResponse;
import com.miguelrosa.cinepass.Domain.Responses.PopularMoviesResponse;
import com.miguelrosa.cinepass.Domain.Responses.TopRatedMoviesResponse;
import com.miguelrosa.cinepass.Domain.Responses.UpComingMoviesResponse;
import com.miguelrosa.cinepass.databinding.FragmentMoviesBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesFragment extends Fragment {
    private FragmentMoviesBinding binding;
    private RecyclerView.Adapter adapterBestMovies, adapterUpComing, adapterTopRated, movieSearchAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMoviesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        String sessionId = getSessionId();

        fetchMainMovies();

        binding.rv1.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rv2.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rv3.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        binding.rvsearch.setLayoutManager(new LinearLayoutManager(requireContext()));
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
        return view;
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
                        adapterBestMovies = new MovieAdapter(movies);
                        binding.rv1.setAdapter(adapterBestMovies);
                        binding.progressBar1.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    binding.progressBar1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                        adapterTopRated = new MovieAdapter(movies);
                        binding.rv2.setAdapter(adapterTopRated);
                        binding.progressBar2.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    binding.progressBar2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TopRatedMoviesResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                        adapterUpComing = new MovieAdapter(movies);
                        binding.rv3.setAdapter(adapterUpComing);
                        binding.progressBar3.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    binding.progressBar3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UpComingMoviesResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

                        if (movieResponse.getResults().toString().equals("[]")) {
                            fetchMainMovies();
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        SharedPreferences preferences = getContext().getSharedPreferences("cinepass_preferences", MODE_PRIVATE);
        return preferences.getString("session_id", "");
    }
}
