package com.example.prm_swd;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class PlushieManagementActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView lvPlushies;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> plushieList;
    private ArrayList<Integer> plushieIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plushie_management);

        dbHelper = new DatabaseHelper(this);
        lvPlushies = findViewById(R.id.lvPlushies);
        Button btnAddPlushie = findViewById(R.id.btnAddPlushie);

        plushieList = new ArrayList<>();
        plushieIds = new ArrayList<>();
        loadPlushies();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, plushieList);
        lvPlushies.setAdapter(adapter);

        btnAddPlushie.setOnClickListener(v -> showPlushieDialog(-1, null, 0.0, null));

        lvPlushies.setOnItemClickListener((parent, view, position, id) -> {
            int plushieId = plushieIds.get(position);
            Cursor cursor = dbHelper.getAllPlushies();
            if (cursor.moveToPosition(position)) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                showPlushieDialog(plushieId, name, price, description);
            }
            cursor.close();
        });
    }

    private void loadPlushies() {
        plushieList.clear();
        plushieIds.clear();
        Cursor cursor = dbHelper.getAllPlushies();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                plushieList.add(name + " - $" + price + " - " + (description != null ? description : ""));
                plushieIds.add(id);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void showPlushieDialog(final int plushieId, String currentName, double currentPrice, String currentDescription) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_plushie, null);
        builder.setView(dialogView);

        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        EditText priceEditText = dialogView.findViewById(R.id.priceEditText);
        EditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);

        if (currentName != null) {
            nameEditText.setText(currentName);
            priceEditText.setText(String.valueOf(currentPrice));
            descriptionEditText.setText(currentDescription);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        AlertDialog dialog = builder.create();
        btnSave.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String priceStr = priceEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            if (!name.isEmpty() && !priceStr.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceStr);
                    dbHelper.savePlushie(plushieId, name, price, description);
                    loadPlushies();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(PlushieManagementActivity.this, "Plushie saved", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(PlushieManagementActivity.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PlushieManagementActivity.this, "Please fill name and price", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        btnDelete.setOnClickListener(v -> {
            if (plushieId != -1) {
                dbHelper.deletePlushie(plushieId);
                loadPlushies();
                adapter.notifyDataSetChanged();
                Toast.makeText(PlushieManagementActivity.this, "Plushie deleted", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.show();
    }
}