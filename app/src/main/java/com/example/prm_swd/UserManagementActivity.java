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

public class UserManagementActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView lvUsers;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> userList;
    private ArrayList<Integer> userIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        dbHelper = new DatabaseHelper(this);
        lvUsers = findViewById(R.id.lvUsers);
        Button btnAddUser = findViewById(R.id.btnAddUser);

        userList = new ArrayList<>();
        userIds = new ArrayList<>();
        loadUsers();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        lvUsers.setAdapter(adapter);

        btnAddUser.setOnClickListener(v -> showUserDialog(-1, null, null));

        lvUsers.setOnItemClickListener((parent, view, position, id) -> {
            int userId = userIds.get(position);
            Cursor cursor = dbHelper.getAllUsers();
            if (cursor.moveToPosition(position)) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                showUserDialog(userId, username, password);
            }
            cursor.close();
        });
    }

    private void loadUsers() {
        userList.clear();
        userIds.clear();
        Cursor cursor = dbHelper.getAllUsers();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                userList.add(username);
                userIds.add(id);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void showUserDialog(final int userId, String currentUsername, String currentPassword) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_user, null);
        builder.setView(dialogView);

        EditText usernameEditText = dialogView.findViewById(R.id.usernameEditText);
        EditText passwordEditText = dialogView.findViewById(R.id.passwordEditText);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);

        if (currentUsername != null) {
            usernameEditText.setText(currentUsername);
            passwordEditText.setText(currentPassword);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        AlertDialog dialog = builder.create();
        btnSave.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                if (password.length() < 6) {
                    Toast.makeText(UserManagementActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbHelper.saveUser(userId, username, password);
                loadUsers();
                adapter.notifyDataSetChanged();
                Toast.makeText(UserManagementActivity.this, "User saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserManagementActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        btnDelete.setOnClickListener(v -> {
            if (userId != -1) {
                dbHelper.deleteUser(userId);
                loadUsers();
                adapter.notifyDataSetChanged();
                Toast.makeText(UserManagementActivity.this, "User deleted", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.show();
    }
}