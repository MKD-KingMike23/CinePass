package com.miguelrosa.cinepass.Activities.Fragments;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.miguelrosa.cinepass.Adapters.MovieListAdapter;
import com.miguelrosa.cinepass.Adapters.TVSeriesListAdapter;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.Domain.Models.Movie;
import com.miguelrosa.cinepass.Domain.Models.TVSeries;
import com.miguelrosa.cinepass.Domain.Responses.MovieResponse;
import com.miguelrosa.cinepass.Domain.Responses.TVSeriesResponse;
import com.miguelrosa.cinepass.R;
import com.miguelrosa.cinepass.databinding.FragmentListsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListsFragment extends Fragment {
    private FragmentListsBinding binding;
    private MovieListAdapter movieListAdapter;
    private TVSeriesListAdapter seriesListAdapter;
    private List<Movie> movieList = new ArrayList<>();
    private List<TVSeries> tvSeriesList = new ArrayList<>();
    private Spinner listSelectorSpinner;
    private String sessionId;
    private int accountId;

    public static ListsFragment newInstance() {
        ListsFragment fragment = new ListsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferences preferences = getActivity().getSharedPreferences("CinePassPrefs", MODE_PRIVATE);
        sessionId = preferences.getString("sessionId", null);
        accountId = preferences.getInt("accountId", -1);

        binding.favoriteMoviesRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        movieListAdapter = new MovieListAdapter(movieList);
        binding.favoriteMoviesRecycler.setAdapter(movieListAdapter);

        binding.seriesfavoriteRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        seriesListAdapter = new TVSeriesListAdapter(tvSeriesList);
        binding.seriesfavoriteRecycler.setAdapter(seriesListAdapter);

        String lista = getActivity().getIntent().getStringExtra("lista");
        if (lista != null) {
            if (lista.equals("watchlist")) {
                fetchWatchlistMovies(sessionId);
                fetchWatchlistTVSeries(sessionId);
            } else if (lista.equals("favoritos")) {
                fetchFavoriteMovies(sessionId);
                fetchFavoriteTVSeries(sessionId);
            }
        }

        setupListSelectorSpinner();

        return view;
    }

    private void setupListSelectorSpinner() {
        listSelectorSpinner = binding.listSelectorSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.lists_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listSelectorSpinner.setAdapter(adapter);

        listSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedList = parent.getItemAtPosition(position).toString();
                if (selectedList.equals("Favoritos")) {
                    fetchFavoriteTVSeries(sessionId);
                    fetchFavoriteMovies(sessionId);
                } else if (selectedList.equals("Pendientes")) {
                    fetchWatchlistMovies(sessionId);
                    fetchWatchlistTVSeries(sessionId);
                } else {
                    fetchFavoriteMovies(sessionId);
                    fetchFavoriteTVSeries(sessionId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
                        movieListAdapter.notifyDataSetChanged();
                    }
                } else {
                    binding.progressBarFavorites.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                binding.progressBarFavorites.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                        movieListAdapter.notifyDataSetChanged();
                    }
                } else {
                    binding.progressBarFavorites.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                binding.progressBarFavorites.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFavoriteTVSeries(String sessionId) {
        binding.seriesprogressBarFavorites.setVisibility(View.VISIBLE);
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<TVSeriesResponse> call = ApiClient.getTmdbApiService().getFavoriteTVSeries(accountId, apiKey, sessionId, language);

        call.enqueue(new Callback<TVSeriesResponse>() {
            @Override
            public void onResponse(Call<TVSeriesResponse> call, Response<TVSeriesResponse> response) {
                binding.seriesprogressBarFavorites.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    TVSeriesResponse tvSeriesResponse = response.body();
                    if (tvSeriesResponse != null) {
                        binding.seriesfavoritesTitle.setText("Series Favoritas");
                        tvSeriesList.clear();
                        tvSeriesList.addAll(tvSeriesResponse.getResults());
                        seriesListAdapter.notifyDataSetChanged();
                    }
                } else {
                    binding.seriesprogressBarFavorites.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TVSeriesResponse> call, Throwable t) {
                binding.seriesprogressBarFavorites.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchWatchlistTVSeries(String sessionId) {
        binding.seriesprogressBarFavorites.setVisibility(View.VISIBLE);
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<TVSeriesResponse> call = ApiClient.getTmdbApiService().getWatchlistTVSeries(accountId, apiKey, sessionId, language);

        call.enqueue(new Callback<TVSeriesResponse>() {
            @Override
            public void onResponse(Call<TVSeriesResponse> call, Response<TVSeriesResponse> response) {
                binding.seriesprogressBarFavorites.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    TVSeriesResponse tvSeriesResponse = response.body();
                    if (tvSeriesResponse != null) {
                        binding.seriesfavoritesTitle.setText("Series Pendientes");
                        tvSeriesList.clear();
                        tvSeriesList.addAll(tvSeriesResponse.getResults());
                        seriesListAdapter.notifyDataSetChanged();
                    }
                } else {
                    binding.seriesprogressBarFavorites.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TVSeriesResponse> call, Throwable t) {
                binding.seriesprogressBarFavorites.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}