package com.example.phanluongha.myfirstapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.adapter.ActivityEventAdapter;
import com.example.phanluongha.myfirstapplication.adapter.MyCalendarAdapter;
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.impl.RcvMyCalendarClick;
import com.example.phanluongha.myfirstapplication.model.Activity;
import com.example.phanluongha.myfirstapplication.model.DayActivity;
import com.example.phanluongha.myfirstapplication.request.JsonParser;
import com.example.phanluongha.myfirstapplication.utils.Config;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;

public class MyCalendarActivity extends DefaultActivity implements RcvMyCalendarClick {

    TextView tvDate;
    DateFormat dateFormatForMonth = new SimpleDateFormat("dd/MM/yyyy");
    private CompactCalendarView compactCalendarView;
    private Date currentDay = new Date();
    private MyCalendarAdapter adapter;
    private ArrayList<Activity> activities;
    private ArrayList<Activity> activitiesFilter;
    private RecyclerView rcvActivityEvent;
    ArrayList<Event> events = new ArrayList<Event>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        tvDate = (TextView) findViewById(R.id.tvDate);

        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        tvDate.setText(dateFormatForMonth.format(currentDay.getTime()));

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                tvDate.setText(dateFormatForMonth.format(dateClicked));
                filterActivity();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                if (currentDay.getMonth() != firstDayOfNewMonth.getMonth() || currentDay.getYear() != firstDayOfNewMonth.getYear()) {
                    tvDate.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                    filterActivity();
                } else {
                    tvDate.setText(dateFormatForMonth.format(currentDay));
                    filterActivity();
                }
            }
        });

        rcvActivityEvent = (RecyclerView) findViewById(R.id.rcvMyCalendar);
        rcvActivityEvent.setLayoutManager(new LinearLayoutManager(this));
        activities = new ArrayList<Activity>();
        activitiesFilter = new ArrayList<Activity>();
        adapter = new MyCalendarAdapter(MyCalendarActivity.this, activitiesFilter);
        rcvActivityEvent.setAdapter(adapter);
        rcvActivityEvent.setNestedScrollingEnabled(false);
        new GetListActivity().execute();
    }

    private void filterActivity() {
        String currentDay = tvDate.getText().toString();
        activitiesFilter.clear();
        for (int i = 0; i < activities.size(); i++) {
            Activity a = activities.get(i);
            if (a.getDay().contains(currentDay)) {
                activitiesFilter.add(a);
            }
        }
        adapter.notifyDataSetChanged();
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
    public void onItemFavoriteProductClick(final int position) {
        final Activity a = activities.get(position);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        if (!a.isFavorite()) {
            alertDialogBuilder.setTitle(this
                    .getString(R.string.add_favorite));
            alertDialogBuilder
                    .setMessage(
                            getString(R.string.add_favorite_confirm))
                    .setCancelable(false)
                    .setPositiveButton(
                            getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {
                                    MultipartBody.Builder m = new MultipartBody.Builder();
                                    m.setType(MultipartBody.FORM);
                                    m.addFormDataPart("type", "activities");
                                    m.addFormDataPart("idActivities", String.valueOf(a.getId()));
                                    m.addFormDataPart("idDevice", MyCalendarActivity.this.idDevice);
                                    m.addFormDataPart("token", MyCalendarActivity.this.token);
                                    new AddFavoriteActivity(position, m).execute();

                                }
                            })

                    .setNegativeButton(
                            getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {

                                    dialog.cancel();
                                }
                            });
        } else {
            alertDialogBuilder.setTitle(this
                    .getString(R.string.remove_favorite));
            alertDialogBuilder
                    .setMessage(
                            getString(R.string.remove_favorite_confirm))
                    .setCancelable(false)
                    .setPositiveButton(
                            getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {
                                    MultipartBody.Builder m = new MultipartBody.Builder();
                                    m.setType(MultipartBody.FORM);
                                    m.addFormDataPart("type", "activities");
                                    m.addFormDataPart("idActivities", String.valueOf(a.getId()));
                                    m.addFormDataPart("idDevice", MyCalendarActivity.this.idDevice);
                                    m.addFormDataPart("token", MyCalendarActivity.this.token);
                                    new RemoveFavoriteActivity(position, m).execute();
                                }
                            })

                    .setNegativeButton(
                            getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {

                                    dialog.cancel();
                                }
                            });
        }

        AlertDialog alertDialog = alertDialogBuilder
                .create();
        alertDialog.show();
    }

    public class GetListActivity extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        public GetListActivity() {
            progressDialog = new ProgressDialog(MyCalendarActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(MyCalendarActivity.this);
            JSONObject json = jParser.getJSONFromUrl(Config.SERVER_HOST + "getlistfavoritedactivitiesbydate?token=" + MyCalendarActivity.this.token + "&idDevice=" + MyCalendarActivity.this.idDevice);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            if (json.length() > 0 && json.has("error")) {
                try {
                    Toast.makeText(MyCalendarActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONObject datas = json.getJSONObject("data");
                    Iterator<String> keys = datas.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        JSONArray activities = datas.getJSONArray(key);
                        for (int j = 0; j < activities.length(); j++) {
                            JSONObject activity = activities.getJSONObject(j);
                            Activity a = new Activity();
                            a.setId(activity.getInt("idActivities"));
                            a.setName(activity.getString("Title"));
                            a.setPlace(activity.getString("Place"));
                            if (activity.has("isFavorite"))
                                a.setFavorite(activity.getBoolean("isFavorite"));
                            else
                                a.setFavorite(false);
                            String str_date = activity.getString("Time");
                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                            try {
                                Date date = (Date) formatter.parse(str_date);
                                a.setTime(date.getTime());
                                Event e = new Event(Color.argb(255, 169, 68, 65), date.getTime());
                                events.add(e);
                                DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                a.setDay(sdf.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            a.setHosted(activity.getString("Hosted"));
                            a.setDescription(activity.getString("Description"));
                            MyCalendarActivity.this.activities.add(a);
                        }
                    }
                    compactCalendarView.addEvents(events);
                    filterActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class AddFavoriteActivity extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int position;
        private MultipartBody.Builder m;

        public AddFavoriteActivity(int position, MultipartBody.Builder m) {
            this.position = position;
            this.m = m;
            progressDialog = new ProgressDialog(MyCalendarActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(MyCalendarActivity.this);
            JSONObject json = jParser.getPostJSONFromUrl(Config.SERVER_HOST + "addfavorite", m);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(MyCalendarActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (json.getInt("code") == 200) {
                        activities.get(position).setFavorite(true);
                        adapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class RemoveFavoriteActivity extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int position;
        private MultipartBody.Builder m;

        public RemoveFavoriteActivity(int position, MultipartBody.Builder m) {
            this.position = position;
            this.m = m;
            progressDialog = new ProgressDialog(MyCalendarActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(MyCalendarActivity.this);
            JSONObject json = jParser.getPostJSONFromUrl(Config.SERVER_HOST + "deletefavorite", m);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(MyCalendarActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (json.getInt("code") == 200) {
                        activities.get(position).setFavorite(false);
                        adapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

