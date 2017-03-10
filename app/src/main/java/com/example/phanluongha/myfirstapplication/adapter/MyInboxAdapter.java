package com.example.phanluongha.myfirstapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.impl.RcvMyCalendarClick;
import com.example.phanluongha.myfirstapplication.model.Activity;
import com.example.phanluongha.myfirstapplication.model.Exhibition;
import com.example.phanluongha.myfirstapplication.model.Inbox;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class MyInboxAdapter extends RecyclerView.Adapter<MyInboxAdapter.InboxHolder> {
    private Context context;
    private ArrayList<Inbox> arrayInbox;
    private ArrayList<Inbox> filteredItems;
    private MyInboxAdapter.ItemFilter mFilter = new MyInboxAdapter.ItemFilter();


    public MyInboxAdapter(Context context,
                          ArrayList<Inbox> arrayInbox) {
        this.context = context;
        this.arrayInbox = arrayInbox;
        this.filteredItems = arrayInbox;
    }


    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    @Override
    public InboxHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_inbox, viewGroup, false);

        return new InboxHolder(itemView);
    }


    @Override
    public void onBindViewHolder(InboxHolder holder, final int position) {
        Inbox inbox = filteredItems.get(position);
        holder.txtName.setText(inbox.getTitle());
        holder.txtDescription.setText(inbox.getDescription());
        holder.txtTime.setText(getTime(context, inbox.getTime()));
    }

    public class InboxHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtDescription;
        TextView txtTime;

        public InboxHolder(View v) {
            super(v);
            txtName = (TextView) v.findViewById(R.id.txtName);
            txtDescription = (TextView) v.findViewById(R.id.txtDescription);
            txtTime = (TextView) v.findViewById(R.id.txtTime);
        }
    }

    public static String getTime(Context context, long time) {
        long currentTime = System.currentTimeMillis() / 1000L;
        if (String.valueOf(time).length() == 13)
            time = time / 1000L;
        int space = (int) (currentTime - time);
        if (space < 60)
            return context.getString(R.string.txt_now);
        space = (int) (space / 60);
        if (space < 60)
            return String.valueOf(space) + context.getString(R.string.txt_mins);
        space = (int) (space / 60);
        if (space < 24) {
            return String.valueOf(space) + context.getString(R.string.txt_hrs);
        }
        Timestamp stamp = new Timestamp(time * 1000L);
        java.sql.Date date = new java.sql.Date(stamp.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String formattedDate = sdf.format(date);
        return formattedDate;

    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            int count = arrayInbox.size();
            final ArrayList<Inbox> tempItems = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                Inbox e = arrayInbox.get(i);
                if (e.getTitle().toLowerCase().contains(filterString) || e.getDescription().toLowerCase().contains(filterString)) {
                    tempItems.add(arrayInbox.get(i));
                }
            }
            results.values = tempItems;
            results.count = tempItems.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (ArrayList<Inbox>) results.values;
            notifyDataSetChanged();
        }
    }

    public Filter getFilter() {
        return mFilter;
    }


}
