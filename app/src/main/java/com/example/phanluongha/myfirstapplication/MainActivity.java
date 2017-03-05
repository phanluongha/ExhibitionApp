package com.example.phanluongha.myfirstapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent listExhibition = new Intent(MainActivity.this, ListExhibitionActivity.class);
                startActivity(listExhibition);
            }
        }, 3000);
    }
}
