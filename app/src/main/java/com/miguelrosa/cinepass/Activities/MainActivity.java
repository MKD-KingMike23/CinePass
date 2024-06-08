package com.miguelrosa.cinepass.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.miguelrosa.cinepass.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkLoginStatus();
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
        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        finish();
    }

    private void navigateToLoginScreen() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
