package com.miguelrosa.cinepass.Activities.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.miguelrosa.cinepass.Adapters.TVSeriesAdapter;
import com.miguelrosa.cinepass.Adapters.TVSeriesSearchAdapter;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.Domain.Models.TVSeries;
import com.miguelrosa.cinepass.Domain.Responses.PopularTVSeriesResponse;
import com.miguelrosa.cinepass.Domain.Responses.TVSeriesResponse;
import com.miguelrosa.cinepass.Domain.Responses.TopRatedTVSeriesResponse;
import com.miguelrosa.cinepass.Domain.Responses.UpComingTVSeriesResponse;
import com.miguelrosa.cinepass.databinding.FragmentTvSeriesBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvSeriesFragment extends Fragment {
    private FragmentTvSeriesBinding binding;
    private RecyclerView.Adapter adapterBestTVSeries, adapterUpComing, adapterTopRated, tvSearchAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTvSeriesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        String sessionId = getSessionId();

        fetchMainTVSeries();

        binding.rv1.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rv2.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rv3.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        binding.rvsearch.setLayoutManager(new LinearLayoutManager(requireContext()));
        tvSearchAdapter = new TVSeriesSearchAdapter();
        binding.rvsearch.setAdapter(tvSearchAdapter);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTVSeries(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchTVSeries(newText);

                if (newText.isEmpty()) {
                    fetchMainTVSeries();
                }
                return false;
            }
        });
        return view;
    }

    private void fetchPopularTVSeries(int page) {
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<PopularTVSeriesResponse> call = ApiClient.getTmdbApiService().getPopularTVSeries(apiKey, page, language);

        call.enqueue(new Callback<PopularTVSeriesResponse>() {
            @Override
            public void onResponse(Call<PopularTVSeriesResponse> call, retrofit2.Response<PopularTVSeriesResponse> response) {
                if (response.isSuccessful()) {
                    PopularTVSeriesResponse popularTVSeriesResponse = response.body();
                    if (popularTVSeriesResponse != null) {
                        List<TVSeries> tvSeries = popularTVSeriesResponse.getResults();
                        adapterBestTVSeries = new TVSeriesAdapter(tvSeries);
                        binding.rv1.setAdapter(adapterBestTVSeries);
                        binding.progressBar1.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    binding.progressBar1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PopularTVSeriesResponse> call, Throwable t) {
                Log.e("ERROR",""+ t.getMessage());
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTopRatedTVSeries(int page) {
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<TopRatedTVSeriesResponse> call = ApiClient.getTmdbApiService().getTopRatedTVSeries(apiKey, page, language);

        call.enqueue(new Callback<TopRatedTVSeriesResponse>() {
            @Override
            public void onResponse(Call<TopRatedTVSeriesResponse> call, retrofit2.Response<TopRatedTVSeriesResponse> response) {
                if (response.isSuccessful()) {
                    TopRatedTVSeriesResponse topratedTVSeriesResponse = response.body();
                    if (topratedTVSeriesResponse != null) {
                        List<TVSeries> tvSeries = topratedTVSeriesResponse.getResults();
                        adapterTopRated = new TVSeriesAdapter(tvSeries);
                        binding.rv2.setAdapter(adapterTopRated);
                        binding.progressBar2.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    binding.progressBar2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TopRatedTVSeriesResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUpCommingTVSeries(int page) {
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<UpComingTVSeriesResponse> call = ApiClient.getTmdbApiService().getUpComingTVSeries(apiKey, page, language);

        call.enqueue(new Callback<UpComingTVSeriesResponse>() {
            @Override
            public void onResponse(Call<UpComingTVSeriesResponse> call, retrofit2.Response<UpComingTVSeriesResponse> response) {
                if (response.isSuccessful()) {
                    UpComingTVSeriesResponse upcomingTVSeriesResponse = response.body();
                    if (upcomingTVSeriesResponse != null) {
                        List<TVSeries> tvSeries = upcomingTVSeriesResponse.getResults();
                        adapterUpComing = new TVSeriesAdapter(tvSeries);
                        binding.rv3.setAdapter(adapterUpComing);
                        binding.progressBar3.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    binding.progressBar3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UpComingTVSeriesResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchTVSeries(String query) {
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<TVSeriesResponse> call = ApiClient.getTmdbApiService().searchTVSeries(apiKey, query, language);
        call.enqueue(new Callback<TVSeriesResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVSeriesResponse> call, @NonNull Response<TVSeriesResponse> response) {
                if (response.isSuccessful()) {
                    TVSeriesResponse tvSeriesResponse = response.body();
                    if (tvSeriesResponse != null) {
                        List<TVSeries> searchResults = tvSeriesResponse.getResults();
                        ((TVSeriesSearchAdapter) tvSearchAdapter).updateTVSeries(searchResults);

                        binding.rvsearch.setVisibility(View.VISIBLE);
                        binding.rv1.setVisibility(View.INVISIBLE);
                        binding.rv2.setVisibility(View.INVISIBLE);
                        binding.rv3.setVisibility(View.INVISIBLE);
                        binding.textView9.setVisibility(View.INVISIBLE);
                        binding.textView10.setVisibility(View.INVISIBLE);
                        binding.textView11.setVisibility(View.INVISIBLE);

                        if (tvSeriesResponse.getResults().toString().equals("[]")) {
                            fetchMainTVSeries();
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TVSeriesResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchMainTVSeries() {
        binding.rvsearch.setVisibility(View.INVISIBLE);
        binding.rv1.setVisibility(View.VISIBLE);
        binding.rv2.setVisibility(View.VISIBLE);
        binding.rv3.setVisibility(View.VISIBLE);
        binding.textView9.setVisibility(View.VISIBLE);
        binding.textView10.setVisibility(View.VISIBLE);
        binding.textView11.setVisibility(View.VISIBLE);

        fetchPopularTVSeries(1);
        fetchUpCommingTVSeries(1);
        fetchTopRatedTVSeries(1);
    }

    private String getSessionId() {
        SharedPreferences preferences = getContext().getSharedPreferences("cinepass_preferences", MODE_PRIVATE);
        return preferences.getString("session_id", "");
    }
}