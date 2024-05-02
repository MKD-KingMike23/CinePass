package com.miguelrosa.cinepass.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.miguelrosa.cinepass.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private static final String user = "usuario";
    private static final String pass = "usuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
    binding.loginBtn.setOnClickListener(v -> {
        if (binding.edittextusername.getText().toString().isEmpty() || binding.edittextpassword.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Por favor, rellene los campos usuario y contraseña", Toast.LENGTH_SHORT).show();
        } else if (binding.edittextusername.getText().toString().equals(user) || binding.edittextpassword.getText().toString().equals(pass)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }else {
            Toast.makeText(LoginActivity.this, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    });
    }
}
