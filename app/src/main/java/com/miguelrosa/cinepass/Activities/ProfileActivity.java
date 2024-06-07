package com.miguelrosa.cinepass.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.miguelrosa.cinepass.Domain.AccountDetailsResponse;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.R;
import com.miguelrosa.cinepass.databinding.ActivityProfileBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionId = getSessionId();

        fetchAccountDetails();
        initializebuttons();
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
                        Glide.with(ProfileActivity.this)
                                .load("https://image.tmdb.org/t/p/w500" + avatarPath)
                                .into(binding.avatarImage);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountDetailsResponse> call, @NonNull Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializebuttons() {
        binding.wishlistLayout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        binding.favoritesLayout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        binding.editbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/settings/profile?language=es"));
            startActivity(intent);
        });

        binding.closesessionbtn.setOnClickListener(v -> {
            logout();
        });

        binding.inicio.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });

        binding.favoritos.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });
    }

    private void clearSession() {
        SharedPreferences preferences = getSharedPreferences("CinePassPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("sessionId");
        editor.apply();
    }

    private String getSessionId() {
        SharedPreferences preferences = getSharedPreferences("CinePassPrefs", MODE_PRIVATE);
        return preferences.getString("sessionId", null);
    }
}