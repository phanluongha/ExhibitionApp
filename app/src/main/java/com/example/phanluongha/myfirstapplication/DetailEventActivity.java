package com.example.phanluongha.myfirstapplication;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phanluongha.myfirstapplication.adapter.ListActivityEventAdapter;

public class DetailEventActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtAbout;
    private TextView txtExhibitor;
    private TextView txtProduct;
    private  TextView txtActivity;
    private ImageView banner;
    DisplayMetrics metrics;
    private int id;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        banner = (ImageView) findViewById(R.id.banner);
        banner.setLayoutParams(new LinearLayout.LayoutParams(metrics.widthPixels, 2 * metrics.widthPixels / 3));
        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getInt("id");
            Glide
                    .with(this)
                    .load(b.getString("banner"))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(banner);
        }
        txtAbout = (TextView) findViewById(R.id.txtAbout);
        txtAbout.setOnClickListener(this);
        txtExhibitor = (TextView) findViewById(R.id.txtExhibitor);
        txtExhibitor.setOnClickListener(this);
        txtProduct = (TextView) findViewById(R.id.txtProduct);
        txtProduct.setOnClickListener(this);
        txtActivity = (TextView)findViewById(R.id.txtActivity);
        txtActivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtAbout:
                Intent general = new Intent(DetailEventActivity.this, GerneralEventActivity.class);
                general.putExtra("id", id);
                startActivity(general);
                break;
            case R.id.txtExhibitor:
                Intent exhibitor = new Intent(DetailEventActivity.this, ListExhibitionEventActivity.class);
                exhibitor.putExtra("id", id);
                startActivity(exhibitor);
                break;
            case R.id.txtProduct:
                Intent product = new Intent(DetailEventActivity.this, ListProductEventActivity.class);
                product.putExtra("id", id);
                startActivity(product);
                break;
            case R.id.txtActivity:
                Intent activity = new Intent(DetailEventActivity.this, ListActivityEventActivity.class);
                activity.putExtra("id", id);
                startActivity(activity);
                break;
        }
    }
}
