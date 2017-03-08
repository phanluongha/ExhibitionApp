package com.example.phanluongha.myfirstapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.model.Activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListActivityAdapter extends ArrayAdapter<Activity> {

    private Context context;
    private int textViewResourceId;
    private ArrayList<Activity> arrayActivity;

    public ListActivityAdapter(Context context, int textViewResourceId,
                               ArrayList<Activity> arrayActivity) {
        super(context, textViewResourceId, arrayActivity);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.arrayActivity = arrayActivity;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(textViewResourceId, null);
        final Activity activity = arrayActivity.get(position);
        if (activity != null) {
            TextView txtName = (TextView) v.findViewById(R.id.txtName);
            txtName.setText(activity.getName());
            TextView txtBooth = (TextView) v.findViewById(R.id.txtBooth);
            txtBooth.setText(activity.getPlace());
            TextView txtTime = (TextView) v.findViewById(R.id.txtTime);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a");
            Date date = new Date(activity.getTime());
            txtTime.setText(simpleDateFormat.format(date));
            ImageView imgFavotite = (ImageView) v.findViewById(R.id.imgFavotite);
            if (!activity.isFavorite()) {
                imgFavotite.setImageResource(R.drawable.activity_empty_stick);
            } else {
                imgFavotite.setImageResource(R.drawable.activitu_fill_stick);
            }


        }
        return v;
    }
}
