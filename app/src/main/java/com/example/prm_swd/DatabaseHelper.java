package com.example.prm_swd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PlushieStore.db";
    private static final int DATABASE_VERSION = 2;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Plushies table
    private static final String TABLE_PLUSHIES = "plushies";
    private static final String COLUMN_PLUSHIE_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        // Create plushies table
        String createPlushiesTable = "CREATE TABLE " + TABLE_PLUSHIES + " (" +
                COLUMN_PLUSHIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(createPlushiesTable);

        // Add default admin account
        ContentValues adminValues = new ContentValues();
        adminValues.put(COLUMN_USERNAME, "admin");
        adminValues.put(COLUMN_PASSWORD, "admin");
        db.insert(TABLE_USERS, null, adminValues);

        // Add sample plushies
        ContentValues plushie1 = new ContentValues();
        plushie1.put(COLUMN_NAME, "Teddy Bear");
        plushie1.put(COLUMN_PRICE, 19.99);
        plushie1.put(COLUMN_DESCRIPTION, "A soft brown teddy bear.");
        db.insert(TABLE_PLUSHIES, null, plushie1);

        ContentValues plushie2 = new ContentValues();
        plushie2.put(COLUMN_NAME, "Bunny Plushie");
        plushie2.put(COLUMN_PRICE, 15.50);
        plushie2.put(COLUMN_DESCRIPTION, "A cute white bunny with floppy ears.");
        db.insert(TABLE_PLUSHIES, null, plushie2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_PLUSHIES + " ADD COLUMN " + COLUMN_DESCRIPTION + " TEXT");
        }
    }

    // Check login
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Get all users
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, null, null, null, null, null);
    }

    // Create or update user
    public boolean saveUser(int id, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        if (id == -1) {
            long result = db.insert(TABLE_USERS, null, values);
            db.close();
            return result != -1;
        } else {
            int result = db.update(TABLE_USERS, values, COLUMN_USER_ID + "=?", new String[]{String.valueOf(id)});
            db.close();
            return result > 0;
        }
    }

    // Delete user
    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_USERS, COLUMN_USER_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // Create or update plushie
    public boolean savePlushie(int id, String name, double price, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_DESCRIPTION, description);
        if (id == -1) {
            long result = db.insert(TABLE_PLUSHIES, null, values);
            db.close();
            return result != -1;
        } else {
            int result = db.update(TABLE_PLUSHIES, values, COLUMN_PLUSHIE_ID + "=?", new String[]{String.valueOf(id)});
            db.close();
            return result > 0;
        }
    }

    // Get all plushies
    public Cursor getAllPlushies() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PLUSHIES, null, null, null, null, null, null);
    }

    // Delete plushie
    public boolean deletePlushie(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PLUSHIES, COLUMN_PLUSHIE_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }
}