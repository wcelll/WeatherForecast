package com.example.weatherforecast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weatherforecast.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText editTextUsername;
    private EditText editTextPassword;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonRegister = findViewById(R.id.buttonRegister); // 注册按钮

        dbHelper = new DatabaseHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                attemptLogin(username, password);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                registerUser(username, password);
            }
        });
    }

    private void attemptLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});

        if (cursor.moveToFirst()) {
            Log.d(TAG, "Login successful.");
            runOnUiThread(() -> {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                saveLoginState(true);
                navigateToMainActivity();
            });
        } else {
            Log.d(TAG, "Login failed.");
            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "登录失败：用户名或密码错误", Toast.LENGTH_SHORT).show());
        }

        cursor.close();
        db.close();
    }

    private void registerUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        try {
            long userId = db.insertOrThrow("users", null, values);
            if (userId != -1) {
                Log.d(TAG, "Registration successful.");
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Registration failed: " + e.getMessage(), e);
            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "注册失败：用户名可能已存在", Toast.LENGTH_SHORT).show());
        } finally {
            db.close();
        }
    }

    private void saveLoginState(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("WeatherApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        //editor.remove("isLoggedIn"); // 移除保存的登录状态
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

