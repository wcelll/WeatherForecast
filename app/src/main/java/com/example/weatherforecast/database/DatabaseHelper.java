package com.example.weatherforecast.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WeatherApp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建 statistics 表
        String CREATE_TABLE_STATISTICS = "CREATE TABLE IF NOT EXISTS statistics (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "city TEXT NOT NULL," +
            "average_temperature REAL," +
            "record_date TEXT," +
            "ranking INTEGER);";

        // 创建 user 表
        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS user (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "password TEXT," +
            "username TEXT);";

        // 创建 users 表
        String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT NOT NULL," +
            "password TEXT NOT NULL," +
            "email TEXT," +
            "created_at TEXT);";

        // 创建 weather 表
        String CREATE_TABLE_WEATHER = "CREATE TABLE IF NOT EXISTS weather (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "city TEXT NOT NULL," +
            "date TEXT," +
            "summary TEXT," +
            "temperature REAL," +
            "created_at TEXT);";

        // 执行 SQL 语句以创建表
        db.execSQL(CREATE_TABLE_STATISTICS);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 当数据库需要升级时，例如添加或修改表结构
        // 示例: db.execSQL("DROP TABLE IF EXISTS users");
        // onCreate(db);
    }
}
