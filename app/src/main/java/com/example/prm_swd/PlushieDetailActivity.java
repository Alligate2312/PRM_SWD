package com.example.prm_swd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PlushieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plushie_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView nameText = findViewById(R.id.detailName);
        TextView priceText = findViewById(R.id.detailPrice);
        TextView descriptionText = findViewById(R.id.detailDescription);
        Button purchaseButton = findViewById(R.id.purchaseButton);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price", 0.0);
        String description = intent.getStringExtra("description");

        nameText.setText(name);
        priceText.setText("$" + price);
        descriptionText.setText(description);

        purchaseButton.setOnClickListener(v -> {
            Toast.makeText(PlushieDetailActivity.this, name + " purchased!", Toast.LENGTH_SHORT).show();
            finish(); // Return to UserDashboardActivity
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Clear SharedPreferences and redirect to LoginActivity
            SharedPreferences prefs = getSharedPreferences("PlushieMarketPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
