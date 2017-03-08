package com.example.phanluongha.myfirstapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.adapter.ListActivityEventAdapter;
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
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

public class ListActivityEventActivity extends DefaultActivity {

    private ViewPager pager;
    private ArrayList<DayActivity> days;
    private ListActivityEventAdapter adapter;

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

        pager = (ViewPager) findViewById(R.id.pager);
        days = new ArrayList<>();
        adapter = new ListActivityEventAdapter(this, days);
        pager.setAdapter(adapter);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            new GetListActivity(b.getInt("id")).execute();
        }
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
    public class GetListActivity extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int idEvent;

        public GetListActivity(int idEvent) {
            this.idEvent = idEvent;
            progressDialog = new ProgressDialog(ListActivityEventActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(ListActivityEventActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getactivitieslistofevent?idEvent=" + String.valueOf(idEvent) + "&idTypeActivities=1&token=" + ListActivityEventActivity.this.token + "&idDevice=" + ListActivityEventActivity.this.idDevice);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(ListActivityEventActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject datas = json.getJSONObject("data");
                    Iterator<String> keys = datas.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        DayActivity d = new DayActivity();
                        d.setDay(key);
                        JSONArray activities = datas.getJSONArray(key);
                        for (int j = 0; j < activities.length(); j++) {
                            JSONObject activity = activities.getJSONObject(j);
                            Activity a = new Activity();
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
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            d.activities.add(a);
                        }
                        days.add(d);
                    }
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
