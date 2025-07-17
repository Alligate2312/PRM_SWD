package com.example.prm_swd;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import java.util.ArrayList;

public class PlushieListActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView lvPlushies;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> plushieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plushie_list);

        dbHelper = new DatabaseHelper(this);
        lvPlushies = findViewById(R.id.lvPlushies);
        plushieList = new ArrayList<>();

        loadPlushies();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, plushieList);
        lvPlushies.setAdapter(adapter);
    }

    private void loadPlushies() {
        plushieList.clear();
        Cursor cursor = dbHelper.getAllPlushies();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                plushieList.add(name + " - $" + price + " - " + (description != null ? description : ""));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}