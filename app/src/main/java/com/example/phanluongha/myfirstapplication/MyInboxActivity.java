package com.example.phanluongha.myfirstapplication;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.adapter.ExhibitionAdapter;
import com.example.phanluongha.myfirstapplication.adapter.MyCalendarAdapter;
import com.example.phanluongha.myfirstapplication.adapter.MyInboxAdapter;
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.model.Activity;
import com.example.phanluongha.myfirstapplication.model.Inbox;
import com.example.phanluongha.myfirstapplication.request.JsonParser;
import com.example.phanluongha.myfirstapplication.utils.Config;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class MyInboxActivity extends DefaultActivity {

    private MyInboxAdapter adapter;
    private ArrayList<Inbox> inboxs;
    private RecyclerView rcvActivityInbox;
    private EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inbox);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        rcvActivityInbox = (RecyclerView) findViewById(R.id.rcvMyInbox);
        rcvActivityInbox.setLayoutManager(new LinearLayoutManager(this));
        inboxs = new ArrayList<Inbox>();
        adapter = new MyInboxAdapter(this, inboxs);
        rcvActivityInbox.setAdapter(adapter);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        txtSearch.addTextChangedListener(new SearchTextWatcher(adapter));
        new GetInbox().execute();
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

    public class SearchTextWatcher implements TextWatcher {
        private MyInboxAdapter lAdapter;

        public SearchTextWatcher(MyInboxAdapter lAdapter) {
            this.lAdapter = lAdapter;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            lAdapter.getFilter().filter(s.toString().toLowerCase());
        }
    }

    public class GetInbox extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        public GetInbox() {
            progressDialog = new ProgressDialog(MyInboxActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(MyInboxActivity.this);
            JSONObject json = jParser.getJSONFromUrl(Config.SERVER_HOST +"getallnotification?token=" + MyInboxActivity.this.token + "&idDevice=" + MyInboxActivity.this.idDevice);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            if (json.length() > 0 && json.has("error")) {
                try {
                    Toast.makeText(MyInboxActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONArray datas = json.getJSONArray("data");
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject ib = datas.getJSONObject(i);
                        Inbox ibox = new Inbox();
                        ibox.setTitle(ib.getString("Title"));
                        ibox.setDescription(ib.getString("Description"));
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        try {
                            Date date = (Date) formatter.parse(ib.getString("Time"));
                            ibox.setTime(date.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        inboxs.add(ibox);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
