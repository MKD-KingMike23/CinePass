package com.miguelrosa.cinepass.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.Domain.CreateSessionBody;
import com.miguelrosa.cinepass.Domain.RequestTokenResponse;
import com.miguelrosa.cinepass.Domain.SessionResponse;
import com.miguelrosa.cinepass.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        binding.loginBtn.setOnClickListener(v -> {
            if (binding.edittextusername.getText().toString().isEmpty() || binding.edittextpassword.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "Por favor, rellene los campos usuario y contraseña", Toast.LENGTH_SHORT).show();
            } else {
                fetchRequestToken();
            }
        });
    }

    private void fetchRequestToken() {
        Call<RequestTokenResponse> call = ApiClient.getTmdbApiService().getRequestToken(ApiClient.getApiKey());

        call.enqueue(new Callback<RequestTokenResponse>() {
            @Override
            public void onResponse(Call<RequestTokenResponse> call, Response<RequestTokenResponse> response) {
                if (response.isSuccessful()) {
                    RequestTokenResponse requestTokenResponse = response.body();
                    if (requestTokenResponse != null && requestTokenResponse.isSuccess()) {
                        String requestToken = requestTokenResponse.getRequestToken();
                        authorizeRequestToken(requestToken);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestTokenResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void authorizeRequestToken(String requestToken) {
        String username = binding.edittextusername.getText().toString();
        String password = binding.edittextpassword.getText().toString();

        Call<RequestTokenResponse> call = ApiClient.getTmdbApiService().validateWithLogin(
                ApiClient.getApiKey(), username, password, requestToken);

        call.enqueue(new Callback<RequestTokenResponse>() {
            @Override
            public void onResponse(Call<RequestTokenResponse> call, Response<RequestTokenResponse> response) {
                if (response.isSuccessful()) {
                    RequestTokenResponse requestTokenResponse = response.body();
                    if (requestTokenResponse != null && requestTokenResponse.isSuccess()) {
                        createSession(requestTokenResponse.getRequestToken());
                    } else {
                        Toast.makeText(LoginActivity.this, "Authorization failed.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestTokenResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createSession(String requestToken) {
        CreateSessionBody createSessionBody = new CreateSessionBody(requestToken);
        Call<SessionResponse> call = ApiClient.getTmdbApiService().createSession(createSessionBody, ApiClient.getApiKey());

        call.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, Response<SessionResponse> response) {
                if (response.isSuccessful()) {
                    SessionResponse sessionResponse = response.body();
                    if (sessionResponse != null && sessionResponse.isSuccess()) {
                        String sessionId = sessionResponse.getSessionId();
                        saveSessionId(sessionId);
                        navigateToMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to create session.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSessionId(String sessionId) {
        SharedPreferences preferences = getSharedPreferences("CinePassPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sessionId", sessionId);
        editor.apply();
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
