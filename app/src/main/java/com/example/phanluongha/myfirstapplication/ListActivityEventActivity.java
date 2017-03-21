package com.example.phanluongha.myfirstapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.example.phanluongha.myfirstapplication.adapter.ListActivityEventAdapter;
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.base.NavigationActivity;
import com.example.phanluongha.myfirstapplication.customview.MyImageView;
import com.example.phanluongha.myfirstapplication.customview.TouchImageView;
import com.example.phanluongha.myfirstapplication.customview.ZoomView;
import com.example.phanluongha.myfirstapplication.impl.RcvActivityClick;
import com.example.phanluongha.myfirstapplication.model.Activity;
import com.example.phanluongha.myfirstapplication.model.DayActivity;
import com.example.phanluongha.myfirstapplication.request.JsonParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import okhttp3.MultipartBody;

public class ListActivityEventActivity extends NavigationActivity {

    private LinearLayout contentLayout;
    private ZoomView zoomView;
    private int idTypeActivities;
    private int idEvent;
    private ImageView img;
    DisplayMetrics metrics;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions defaultOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            idTypeActivities = b.getInt("idTypeActivities");
            String linkImage = "";
            if (idTypeActivities == 1) {
                linkImage = "http://188.166.241.242/upload/activities.png";
            } else {
                linkImage = "ttp://188.166.241.242/upload/conference.png";
            }
            idEvent = b.getInt("id");
            contentLayout = (LinearLayout) findViewById(R.id.content_map);
            View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_zoom, null, false);
            v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            img = (MyImageView) v.findViewById(R.id.img);
            img.setLayoutParams(new LinearLayout.LayoutParams(metrics.widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT));
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    ListActivityEventActivity.this).defaultDisplayImageOptions(
                    defaultOptions).build();
            imageLoader.init(config);
            defaultOptions = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.NONE).build();
            final ProgressDialog spinner = new ProgressDialog(ListActivityEventActivity.this);
            spinner.setMessage("Loading...");
            imageLoader.displayImage(linkImage, img, defaultOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.show();
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    spinner.dismiss();
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.dismiss();
                }
            });
            zoomView = new ZoomView(ListActivityEventActivity.this);
            zoomView.addView(v);
            contentLayout.addView(zoomView);
            zoomView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivityEventActivity.this, NotepadActivity.class);
                startActivity(intent);
            }
        });
        initNavigation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
