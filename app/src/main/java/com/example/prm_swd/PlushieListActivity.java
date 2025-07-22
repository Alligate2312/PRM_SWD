package com.example.prm_swd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;

import com.example.prm_swd.models.Plushie;

import java.util.ArrayList;

public class PlushieListActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView lvPlushies;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> plushieList;
    private ArrayList<Plushie> plushieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plushie_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);
        lvPlushies = findViewById(R.id.lvPlushies);
        plushieList = new ArrayList<>();
        plushieData = new ArrayList<>();

        loadPlushies();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, plushieList);
        lvPlushies.setAdapter(adapter);

        // Set click listener for ListView items
        lvPlushies.setOnItemClickListener((parent, view, position, id) -> {
            Plushie selectedPlushie = plushieData.get(position);
            Intent intent = new Intent(PlushieListActivity.this, PlushieDetailActivity.class);
            intent.putExtra("name", selectedPlushie.getName());
            intent.putExtra("price", selectedPlushie.getPrice());
            intent.putExtra("description", selectedPlushie.getDescription());
            startActivity(intent);
        });
    }

    private void loadPlushies() {
        plushieList.clear();
        plushieData.clear();
        Cursor cursor = dbHelper.getAllPlushies();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                plushieList.add(name + " - $" + price + " - " + (description != null ? description : ""));
                plushieData.add(new Plushie(name, price, description));
            } while (cursor.moveToNext());
        }
        cursor.close();
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