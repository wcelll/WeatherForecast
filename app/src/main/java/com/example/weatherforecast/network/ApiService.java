package com.example.weatherforecast.network;

import com.example.weatherforecast.Bean.LoginRequest;
import com.example.weatherforecast.Bean.LoginResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import android.util.Log;

public class ApiService {

    private static final String TAG = "ApiService"; // 日志标签
    private static final String JDBC_URL = "jdbc:mysql://198.168.0.110:3306/zsweatherforecast";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public LoginResponse loginUser(LoginRequest loginRequest) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Log.d(TAG, "Attempting to connect to database."); // 日志
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, loginRequest.getUsername());
            statement.setString(2, loginRequest.getPassword());

            Log.d(TAG, "Executing SQL query."); // 日志
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Log.d(TAG, "Login successful."); // 日志
                return new LoginResponse("登录成功");
            } else {
                Log.d(TAG, "Login failed."); // 日志
                return new LoginResponse("登录失败");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during login: " + e.getMessage(), e); // 错误日志
            return new LoginResponse("登录时发生错误");
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    Log.d(TAG, "ResultSet closed."); // 日志
                }
                if (statement != null) {
                    statement.close();
                    Log.d(TAG, "PreparedStatement closed."); // 日志
                }
                if (connection != null) {
                    connection.close();
                    Log.d(TAG, "Connection closed."); // 日志
                }
            } catch (Exception e) {
                Log.e(TAG, "Error closing resources: " + e.getMessage(), e); // 错误日志
            }
        }
    }
}


