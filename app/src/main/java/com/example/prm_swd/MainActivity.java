package com.example.prm_swd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.btnLogin);
        Button registerButton = findViewById(R.id.btnRegister);

        loginButton.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        registerButton.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}