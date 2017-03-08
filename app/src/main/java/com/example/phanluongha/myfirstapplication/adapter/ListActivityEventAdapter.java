package com.example.phanluongha.myfirstapplication.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.model.DayActivity;

import java.util.ArrayList;

public class ListActivityEventAdapter extends PagerAdapter {

    Context context;
    ArrayList<DayActivity> days;

    public ListActivityEventAdapter(Context context, ArrayList<DayActivity> days) {
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.event_activity_list, null);
        RecyclerView rcvActivityEvent = (RecyclerView) v.findViewById(R.id.rcvActivityEvent);
        rcvActivityEvent.setLayoutManager(new LinearLayoutManager(context));
        rcvActivityEvent.setAdapter(new ActivityEventAdapter(context, days.get(position).activities));
        ((ViewPager) collection).addView(v);
        return v;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}