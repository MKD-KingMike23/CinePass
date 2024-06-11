package com.miguelrosa.cinepass.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.miguelrosa.cinepass.databinding.ActivityIntroBinding;

public class IntroActivity extends AppCompatActivity {
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.introBtn.setOnClickListener(v -> checkLoginStatus());
    }

    private void checkLoginStatus() {
        SharedPreferences preferences = getSharedPreferences("CinePassPrefs", MODE_PRIVATE);
        String sessionId = preferences.getString("sessionId", null);
        int accountId = preferences.getInt("accountId", -1);

        if (sessionId != null && accountId != -1) {
            navigateToMainScreen();
        } else {
            navigateToLoginScreen();
        }
    }

    private void navigateToMainScreen() {
        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();
    }

    private void navigateToLoginScreen() {
        startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        finish();
    }
}