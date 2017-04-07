package com.example.phanluongha.myfirstapplication.base;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

/**
 * Created by PhanLuongHa on 3/6/2017.
 */

public class DefaultActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    public String token;
    public String idDevice;
    public String laguage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        token = sharedpreferences.getString("token", "");
        idDevice = sharedpreferences.getString("idDevice", "");
        laguage = sharedpreferences.getString("language", "en");
    }
}
