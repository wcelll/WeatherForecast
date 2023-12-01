package com.example.weatherforecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.Bean.DayWeatherBean;
import com.example.weatherforecast.Bean.WeatherBean;
import com.example.weatherforecast.adapter.FutureWeatherAdapter;
import com.example.weatherforecast.util.NetUtil;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppCompatSpinner mSpinner;
    private ArrayAdapter<String> mSpAdapter;
    private String[] mCities;
    private TextView tvWeather, tvTem, tvTemLowHigh, tvWin, tvAir;
    private ImageView ivWeather;
    private RecyclerView rlvFutureWeather;
    private FutureWeatherAdapter mWeatherAdapter;
    private DayWeatherBean todayWeather;
    private Button buttonLogout, buttonTips;
    private boolean isResumedFromBackground = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (isResumedFromBackground) {
            logoutUser();
        }
        isResumedFromBackground = true;
    }

    private int getImageResource(String weaStr) {
        switch (weaStr) {
            case "yin":
                return R.drawable.biz_plugin_weather_yin;
            default:
                return R.drawable.biz_plugin_weather_qing;
        }
    }

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String weatherData = (String) msg.obj;
                Gson gson = new Gson();
                WeatherBean weatherBean = gson.fromJson(weatherData, WeatherBean.class);
                updateUiOfWeather(weatherBean);
            }
        }
    };

    private void updateUiOfWeather(WeatherBean weatherBean) {
        if (weatherBean == null) return;

        List<DayWeatherBean> dayWeathers = weatherBean.getDayWeatherBeans();
        todayWeather = dayWeathers.get(0);
        if (todayWeather == null) return;

        tvTem.setText(todayWeather.getTem());
        tvWeather.setText(todayWeather.getWea() + " " + todayWeather.getDate() + todayWeather.getWeek());
        tvTemLowHigh.setText(todayWeather.getTem2()  + " ~ " + todayWeather.getTem1());
        tvWin.setText(todayWeather.getWin()[0] + " " + todayWeather.getWinSpeed());
        tvAir.setText("空气等级: " + todayWeather.getAir() + " " + todayWeather.getAirLevel() + "\n\n" + todayWeather.getAirTips());
        ivWeather.setImageResource(getImageResource(todayWeather.getWeaImg()));

        dayWeathers.remove(0);
        mWeatherAdapter = new FutureWeatherAdapter(this, dayWeathers);
        rlvFutureWeather.setAdapter(mWeatherAdapter);
        rlvFutureWeather.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isUserLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initViews();
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> logoutUser());

        buttonTips = findViewById(R.id.buttonTips);
        buttonTips.setOnClickListener(v -> navigateToTipsActivity());
    }

    private void initViews() {
        mSpinner = findViewById(R.id.sp_city);
        mCities = getResources().getStringArray(R.array.cities);
        mSpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mCities);
        mSpinner.setAdapter(mSpAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getWeatherOfCity(mCities[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        tvWeather = findViewById(R.id.tv_weather);
        tvTem = findViewById(R.id.tv_tem);
        tvTemLowHigh = findViewById(R.id.tv_tem_low_high);
        tvWin = findViewById(R.id.tv_win);
        tvAir = findViewById(R.id.tv_air);
        ivWeather = findViewById(R.id.iv_weather);
        rlvFutureWeather = findViewById(R.id.rlv_future_weather);
    }

    private void getWeatherOfCity(String selectCity) {
        new Thread(() -> {
            String weatherCity = NetUtil.getWeatherOfCity(selectCity);
            Message message = Message.obtain();
            message.what = 0;
            message.obj = weatherCity;
            mHandler.sendMessage(message);
        }).start();
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("WeatherApp", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("WeatherApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isLoggedIn");
        editor.apply();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void navigateToTipsActivity() {
        Intent intent = new Intent(this, TipsActivity.class);
        // 如果需要传递 todayWeather 对象
        // intent.putExtra("tips", todayWeather);
        startActivity(intent);
    }
}
