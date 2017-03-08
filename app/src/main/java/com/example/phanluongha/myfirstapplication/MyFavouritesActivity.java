package com.example.phanluongha.myfirstapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.adapter.ViewPagerAdapter;
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.fragment.FragmentExhibitor;
import com.example.phanluongha.myfirstapplication.fragment.FragmentProduct;

public class MyFavouritesActivity extends DefaultActivity implements View.OnClickListener {
    private TextView txtProducts;
    private TextView txtExhibitors;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtProducts = (TextView) findViewById(R.id.txtProducts);
        txtExhibitors = (TextView) findViewById(R.id.txtExhibitors);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(FragmentProduct.newInstance(MyFavouritesActivity.this.idDevice, MyFavouritesActivity.this.token));
        adapter.addFrag(FragmentExhibitor.newInstance(MyFavouritesActivity.this.idDevice, MyFavouritesActivity.this.token));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    txtProducts
                            .setBackgroundResource(R.drawable.background_profile_gallery_active_style);
                    txtProducts.setTextColor(getResources().getColor(android.R.color.white));

                    txtExhibitors
                            .setBackgroundResource(R.drawable.background_profile_achivement_nomal_style);
                    txtExhibitors.setTextColor(getResources().getColor(
                            android.R.color.black));

                } else {
                    txtProducts
                            .setBackgroundResource(R.drawable.background_profile_gallery_nomal_style);
                    txtProducts.setTextColor(getResources().getColor(
                            android.R.color.black));

                    txtExhibitors
                            .setBackgroundResource(R.drawable.background_profile_achievement_active_style);
                    txtExhibitors.setTextColor(getResources().getColor(android.R.color.white));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        txtExhibitors.setOnClickListener(this);
        txtProducts.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtProducts:
                viewPager.setCurrentItem(0);
                break;
            case R.id.txtExhibitors:
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }
}
