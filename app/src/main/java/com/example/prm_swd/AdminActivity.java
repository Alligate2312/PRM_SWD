package com.example.prm_swd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().setTitle("Admin Panel");

        Button btnManageUsers = findViewById(R.id.btnManageUsers);
        Button btnManagePlushies = findViewById(R.id.btnManagePlushies);

        btnManageUsers.setOnClickListener(v -> startActivity(new Intent(this, UserManagementActivity.class)));
        btnManagePlushies.setOnClickListener(v -> startActivity(new Intent(this, PlushieManagementActivity.class)));
    }
}