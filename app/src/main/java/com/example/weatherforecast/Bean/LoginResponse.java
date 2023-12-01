package com.example.weatherforecast.Bean;

public class LoginResponse {
    private String message;
    // 可以根据需要添加更多字段，如 token 或用户信息

    // 构造器
    public LoginResponse(String message) {
        this.message = message;
    }

    // Getter 和 Setter 方法
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
