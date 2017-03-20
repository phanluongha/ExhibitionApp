package com.example.phanluongha.myfirstapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phanluongha.myfirstapplication.adapter.ListActivityEventAdapter;
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.base.NavigationActivity;
import com.example.phanluongha.myfirstapplication.customview.TouchImageView;
import com.example.phanluongha.myfirstapplication.impl.RcvActivityClick;
import com.example.phanluongha.myfirstapplication.model.Activity;
import com.example.phanluongha.myfirstapplication.model.DayActivity;
import com.example.phanluongha.myfirstapplication.request.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import okhttp3.MultipartBody;

public class ListActivityEventActivity extends NavigationActivity {

    private TextView txtDate;
    private TextView txtMonth;
    private TextView txtCurrent;
    private TextView txtPre;
    private TextView txtNext;
    private ViewPager pager;
    private ArrayList<DayActivity> days;
    private ListActivityEventAdapter adapter;
    private int idTypeActivities;
    private int idEvent;
    private TouchImageView img;
    DisplayMetrics metrics;

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

//        txtDate = (TextView) findViewById(R.id.txtDate);
//        txtMonth = (TextView) findViewById(R.id.txtMonth);
//        txtCurrent = (TextView) findViewById(R.id.txtCurrent);
//        txtPre = (TextView) findViewById(R.id.txtPre);
//        txtNext = (TextView) findViewById(R.id.txtNext);
//        pager = (ViewPager) findViewById(R.id.pager);
//        days = new ArrayList<>();
//        adapter = new ListActivityEventAdapter(this, days);
//        pager.setAdapter(adapter);
//        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                setDay(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
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
            img = (TouchImageView) findViewById(R.id.img);
            img.setLayoutParams(new LinearLayout.LayoutParams(metrics.widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT));
            Glide
                    .with(this)
                    .load(linkImage)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(img);
        }
//        txtPre.setOnClickListener(this);
//        txtNext.setOnClickListener(this);
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

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.txtPre:
//                if (pager.getCurrentItem() > 0)
//                    pager.setCurrentItem(pager.getCurrentItem() - 1, true);
//                break;
//            case R.id.txtNext:
//                if (pager.getCurrentItem() < pager.getChildCount() - 1)
//                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
//                break;
//        }
//    }
//
//    @Override
//    public void onItemFavoriteProductClick(final int positionParent, final int position) {
//        final Activity a = days.get(positionParent).activities.get(position);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                this);
//        if (!a.isFavorite()) {
//            alertDialogBuilder.setTitle(this
//                    .getString(R.string.add_favorite));
//            alertDialogBuilder
//                    .setMessage(
//                            getString(R.string.add_favorite_confirm))
//                    .setCancelable(false)
//                    .setPositiveButton(
//                            getString(R.string.yes),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(
//                                        DialogInterface dialog,
//                                        int id) {
//                                    MultipartBody.Builder m = new MultipartBody.Builder();
//                                    m.setType(MultipartBody.FORM);
//                                    m.addFormDataPart("type", "activities");
//                                    m.addFormDataPart("idActivities", String.valueOf(a.getId()));
//                                    m.addFormDataPart("idDevice", ListActivityEventActivity.this.idDevice);
//                                    m.addFormDataPart("token", ListActivityEventActivity.this.token);
//                                    new AddFavoriteActivity(positionParent, position, m).execute();
//
//                                }
//                            })
//
//                    .setNegativeButton(
//                            getString(R.string.no),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(
//                                        DialogInterface dialog,
//                                        int id) {
//
//                                    dialog.cancel();
//                                }
//                            });
//        } else {
//            alertDialogBuilder.setTitle(this
//                    .getString(R.string.remove_favorite));
//            alertDialogBuilder
//                    .setMessage(
//                            getString(R.string.remove_favorite_confirm))
//                    .setCancelable(false)
//                    .setPositiveButton(
//                            getString(R.string.yes),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(
//                                        DialogInterface dialog,
//                                        int id) {
//                                    MultipartBody.Builder m = new MultipartBody.Builder();
//                                    m.setType(MultipartBody.FORM);
//                                    m.addFormDataPart("type", "activities");
//                                    m.addFormDataPart("idActivities", String.valueOf(a.getId()));
//                                    m.addFormDataPart("idDevice", ListActivityEventActivity.this.idDevice);
//                                    m.addFormDataPart("token", ListActivityEventActivity.this.token);
//                                    new RemoveFavoriteActivity(positionParent, position, m).execute();
//                                }
//                            })
//
//                    .setNegativeButton(
//                            getString(R.string.no),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(
//                                        DialogInterface dialog,
//                                        int id) {
//
//                                    dialog.cancel();
//                                }
//                            });
//        }
//
//        AlertDialog alertDialog = alertDialogBuilder
//                .create();
//        alertDialog.show();
//    }
//
//    public class GetListActivity extends AsyncTask<String, String, JSONObject> {
//
//        private ProgressDialog progressDialog;
//        private int idEvent;
//        private int idTypeActivities;
//
//        public GetListActivity(int idEvent, int idTypeActivities) {
//            this.idEvent = idEvent;
//            this.idTypeActivities = idTypeActivities;
//            progressDialog = new ProgressDialog(ListActivityEventActivity.this);
//            progressDialog.setMessage("Loading...");
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog.show();
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//            JsonParser jParser = new JsonParser(ListActivityEventActivity.this);
//            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getactivitieslistofevent?idEvent=" + String.valueOf(idEvent) + "&idTypeActivities=" + String.valueOf(idTypeActivities) + "&token=" + ListActivityEventActivity.this.token + "&idDevice=" + ListActivityEventActivity.this.idDevice);
//            return json;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject json) {
//            progressDialog.dismiss();
//            try {
//                if (json.length() > 0 && json.has("error")) {
//                    try {
//                        Toast.makeText(ListActivityEventActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    JSONObject datas = json.getJSONObject("data");
//                    days.clear();
//                    Iterator<String> keys = datas.keys();
//                    while (keys.hasNext()) {
//                        String key = (String) keys.next();
//                        DayActivity d = new DayActivity();
//                        d.setDay(key);
//                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                        try {
//                            Date dt = (Date) format.parse(key);
//                            d.setTime(dt.getTime());
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        JSONArray activities = datas.getJSONArray(key);
//                        for (int j = 0; j < activities.length(); j++) {
//                            JSONObject activity = activities.getJSONObject(j);
//                            Activity a = new Activity();
//                            a.setId(activity.getInt("idActivities"));
//                            a.setName(activity.getString("Title"));
//                            a.setPlace(activity.getString("Place"));
//                            if (activity.has("isFavorite"))
//                                a.setFavorite(activity.getBoolean("isFavorite"));
//                            else
//                                a.setFavorite(false);
//                            String str_date = activity.getString("Time");
//                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
//                            try {
//                                Date date = (Date) formatter.parse(str_date);
//                                a.setTime(date.getTime());
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            d.activities.add(a);
//                        }
//                        days.add(d);
//                    }
//                    adapter.notifyDataSetChanged();
//                    if (days.size() > 0)
//                        setDay(0);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void setDay(int position) {
//        DayActivity da = days.get(position);
//        SimpleDateFormat simpleDateFormatDayOfWeek = new SimpleDateFormat("EEEE");
//        SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MMMM");
//        SimpleDateFormat simpleDateFormatDayOfMonth = new SimpleDateFormat("d");
//        Date date = new Date(da.getTime());
//        txtDate.setText(simpleDateFormatDayOfWeek.format(date));
//        txtMonth.setText(simpleDateFormatMonth.format(date));
//        txtCurrent.setText(simpleDateFormatDayOfMonth.format(date));
//        if (position == 0) {
//            txtPre.setVisibility(View.INVISIBLE);
//        } else {
//            txtPre.setVisibility(View.VISIBLE);
//            txtPre.setText(simpleDateFormatDayOfMonth.format(new Date(days.get(position - 1).getTime())));
//        }
//        if (position == days.size() - 1) {
//            txtNext.setVisibility(View.INVISIBLE);
//        } else {
//            txtNext.setVisibility(View.VISIBLE);
//            txtNext.setText(simpleDateFormatDayOfMonth.format(new Date(days.get(position + 1).getTime())));
//        }
//    }
//
//    public class AddFavoriteActivity extends AsyncTask<String, String, JSONObject> {
//
//        private ProgressDialog progressDialog;
//        private int positionParent;
//        private int position;
//        private MultipartBody.Builder m;
//
//        public AddFavoriteActivity(int positionParent, int position, MultipartBody.Builder m) {
//            this.position = position;
//            this.positionParent = positionParent;
//            this.m = m;
//            progressDialog = new ProgressDialog(ListActivityEventActivity.this);
//            progressDialog.setMessage("Loading...");
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog.show();
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//            JsonParser jParser = new JsonParser(ListActivityEventActivity.this);
//            JSONObject json = jParser.getPostJSONFromUrl("http://188.166.241.242/api/addfavorite", m);
//            return json;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject json) {
//            progressDialog.dismiss();
//            try {
//                if (json.length() > 0 && json.has("error")) {
//                    try {
//                        Toast.makeText(ListActivityEventActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    if (json.getInt("code") == 200) {
//                        days.get(positionParent).activities.get(position).setFavorite(true);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public class RemoveFavoriteActivity extends AsyncTask<String, String, JSONObject> {
//
//        private ProgressDialog progressDialog;
//        private int positionParent;
//        private int position;
//        private MultipartBody.Builder m;
//
//        public RemoveFavoriteActivity(int positionParent, int position, MultipartBody.Builder m) {
//            this.positionParent = positionParent;
//            this.position = position;
//            this.m = m;
//            progressDialog = new ProgressDialog(ListActivityEventActivity.this);
//            progressDialog.setMessage("Loading...");
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog.show();
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//            JsonParser jParser = new JsonParser(ListActivityEventActivity.this);
//            JSONObject json = jParser.getPostJSONFromUrl("http://188.166.241.242/api/deletefavorite", m);
//            return json;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject json) {
//            progressDialog.dismiss();
//            try {
//                if (json.length() > 0 && json.has("error")) {
//                    try {
//                        Toast.makeText(ListActivityEventActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    if (json.getInt("code") == 200) {
//                        days.get(positionParent).activities.get(position).setFavorite(false);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
