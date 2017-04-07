package com.example.phanluongha.myfirstapplication.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Created by PhanLuongHa on 4/7/2017.
 */

public class BaseApplication extends Application {
    SharedPreferences sharedpreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String laguage = sharedpreferences.getString("language", "en");
        Configuration config = new Configuration(getResources().getConfiguration());
        config.locale = new Locale(laguage);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
