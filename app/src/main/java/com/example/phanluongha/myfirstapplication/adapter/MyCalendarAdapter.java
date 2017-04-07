package com.example.phanluongha.myfirstapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.impl.RcvMyCalendarClick;
import com.example.phanluongha.myfirstapplication.model.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MyCalendarAdapter extends RecyclerView.Adapter<MyCalendarAdapter.ExhibitionHolder> {
    private Context context;
    private ArrayList<Activity> arrayActivity;
    private RcvMyCalendarClick rcvActivityClick;


    public MyCalendarAdapter(Context context,
                             ArrayList<Activity> arrayActivity) {
        this.context = context;
        this.arrayActivity = arrayActivity;
        this.rcvActivityClick = (RcvMyCalendarClick) context;
    }


    @Override
    public int getItemCount() {
        return arrayActivity.size();
    }

    @Override
    public ExhibitionHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_activity, viewGroup, false);

        return new ExhibitionHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ExhibitionHolder holder, final int position) {
        Activity activity = arrayActivity.get(position);
        holder.txtName.setText(activity.getName());

        holder.txtBooth.setText(activity.getPlace());


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a");
        Date date = new Date(activity.getTime());
        holder.txtTime.setText(simpleDateFormat.format(date));
        Date dateCurrent = new Date();
        if (date.compareTo(dateCurrent) < 0) {
            holder.layoutActive.setBackgroundColor(Color.GRAY);
            holder.txtTime.setTextColor(Color.GRAY);
        }

        if (!activity.isFavorite()) {
            holder.imgFavotite.setImageResource(R.drawable.activity_empty_stick);
        } else {
            holder.imgFavotite.setImageResource(R.drawable.activitu_fill_stick);
        }
        holder.imgFavotite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcvActivityClick.onItemFavoriteProductClick(position);
            }
        });
    }

    public class ExhibitionHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtBooth;
        TextView txtTime;
        ImageView imgFavotite;
        LinearLayout layoutActive;

        public ExhibitionHolder(View v) {
            super(v);
            txtName = (TextView) v.findViewById(R.id.txtName);
            txtBooth = (TextView) v.findViewById(R.id.txtBooth);
            txtTime = (TextView) v.findViewById(R.id.txtTime);
            imgFavotite = (ImageView) v.findViewById(R.id.imgFavotite);
            layoutActive = (LinearLayout) v.findViewById(R.id.layoutActive);
        }
    }


}
