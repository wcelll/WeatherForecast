package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.weatherforecast.Bean.DayWeatherBean;
import com.example.weatherforecast.adapter.TipsAdapter;

public class TipsActivity extends AppCompatActivity {

    private RecyclerView rlvTips;
    private TipsAdapter mTipsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        rlvTips = findViewById(R.id.rlv_tips);

        // 获取从MainActivity传来的天气数据
        Intent intent = getIntent();
        DayWeatherBean weatherBean = (DayWeatherBean) intent.getSerializableExtra("tips");
        if (weatherBean == null) {
            Log.e("TipsActivity", "No weather data received");
            Toast.makeText(this, "没有可用的天气数据", Toast.LENGTH_LONG).show();
            return;
        }

        // 如果有生活小提示的数据，则展示它们
        if (weatherBean.getTipsBean() != null && !weatherBean.getTipsBean().isEmpty()) {
            mTipsAdapter = new TipsAdapter(this, weatherBean.getTipsBean());
            rlvTips.setAdapter(mTipsAdapter);
            rlvTips.setLayoutManager(new LinearLayoutManager(this));
        } else {
            Toast.makeText(this, "没有可用的生活小提示", Toast.LENGTH_LONG).show();
        }
    }

    // 返回主界面的方法
    public void onBackToMain(View view) {
        finish();
    }
}
