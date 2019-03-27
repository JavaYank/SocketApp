package com.yank.socketapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(this, MySocketService.class);
            intent.putExtra("name", "подключен к сокету");
            ContextCompat.startForegroundService(this, intent);
        });

        Button btnStop = findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(v -> {
            Intent intent = new Intent(this, MySocketService.class);
            intent.putExtra("name", "stop");
            stopService(intent);
        });

    }
}
