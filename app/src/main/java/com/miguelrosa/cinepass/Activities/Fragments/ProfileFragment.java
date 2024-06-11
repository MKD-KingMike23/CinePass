package com.miguelrosa.cinepass.Activities.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.miguelrosa.cinepass.Activities.LoginActivity;
import com.miguelrosa.cinepass.Activities.MainActivity;
import com.miguelrosa.cinepass.Domain.Responses.AccountDetailsResponse;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.databinding.FragmentProfileBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private String sessionId;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        sessionId = getSessionId();

        fetchAccountDetails();
        initializebuttons();

        return view;
    }

    private void fetchAccountDetails() {
        String apiKey = ApiClient.getApiKey();

        Call<AccountDetailsResponse> call = ApiClient.getTmdbApiService().getAccountDetails(apiKey, sessionId);

        call.enqueue(new Callback<AccountDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<AccountDetailsResponse> call, @NonNull Response<AccountDetailsResponse> response) {
                if (response.isSuccessful()) {
                    AccountDetailsResponse accountDetails = response.body();
                    if (accountDetails != null) {
                        binding.usernametv.setText(accountDetails.getUsername());

                        String avatarPath = accountDetails.getAvatar().getTmdb().getAvatarPath();
                        Glide.with(ProfileFragment.this)
                                .load("https://image.tmdb.org/t/p/w500" + avatarPath)
                                .into(binding.avatarImage);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountDetailsResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        String apiKey = ApiClient.getApiKey();

        Call<Void> call = ApiClient.getTmdbApiService().deleteSession(apiKey, sessionId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    clearSession();
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializebuttons() {
        binding.watchlistLayout.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.putExtra("lista", "watchlist");
            intent.putExtra("fragment", "listas");
            startActivity(intent);
        });

        binding.favoritesLayout.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.putExtra("lista", "favoritos");
            intent.putExtra("fragment", "listas");
            startActivity(intent);
        });

        binding.editbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/settings/profile?language=es"));
            startActivity(intent);
        });

        binding.closesessionbtn.setOnClickListener(v -> {
            logout();
        });
    }

    private void clearSession() {
        SharedPreferences preferences = getActivity().getSharedPreferences("CinePassPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("sessionId");
        editor.apply();
    }

    private String getSessionId() {
        SharedPreferences preferences = getActivity().getSharedPreferences("CinePassPrefs", MODE_PRIVATE);
        return preferences.getString("sessionId", null);
    }
}