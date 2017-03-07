package com.example.phanluongha.myfirstapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.adapter.ListExhibitionAdapter;
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.impl.ExhibitionFavoriteClickListener;
import com.example.phanluongha.myfirstapplication.model.Event;
import com.example.phanluongha.myfirstapplication.model.Exhibition;
import com.example.phanluongha.myfirstapplication.request.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fancycoverflow.FancyCoverFlowSampleAdapter;

public class ListExhibitionEventActivity extends DefaultActivity implements ExhibitionFavoriteClickListener {

    private ListView listView;
    private ArrayList<Exhibition> arrayExhibition;
    private ListExhibitionAdapter listExhibitionAdapter;
    private EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_exhibition_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        listView = (ListView) findViewById(R.id.listView);
        arrayExhibition = new ArrayList<Exhibition>();
        listExhibitionAdapter = new ListExhibitionAdapter(this,
                R.layout.item_exhibition, arrayExhibition);
        listView.setAdapter(listExhibitionAdapter);

        final Bundle b = getIntent().getExtras();
        if (b != null) {
            getListExhibition(b.getInt("id"));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position,
                                        long id) {
                    Intent detailExhibition = new Intent(ListExhibitionEventActivity.this, DetailExhibitionActivity.class);
                    detailExhibition.putExtra("id", listExhibitionAdapter.getItem(position).getId());
                    detailExhibition.putExtra("idEvent", b.getInt("id"));
                    startActivity(detailExhibition);
                }
            });
        }
        txtSearch.addTextChangedListener(new SearchTextWatcher(listExhibitionAdapter));
    }

    private void getListExhibition(int id) {
        new GetListExhibition(id).execute();
    }

    @Override
    public void keyClickedIndex(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
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
                                Exhibition ex = arrayExhibition.get(position);
                                new AddFavoriteExhibition(position, ex.getId()).execute();
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

        AlertDialog alertDialog = alertDialogBuilder
                .create();
        alertDialog.show();


    }

    public class GetListExhibition extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int idEvent;

        public GetListExhibition(int idEvent) {
            this.idEvent = idEvent;
            progressDialog = new ProgressDialog(ListExhibitionEventActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(ListExhibitionEventActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getexhibitorlist?idEvent=" + String.valueOf(idEvent) + "&token=" + ListExhibitionEventActivity.this.token);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(ListExhibitionEventActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONArray datas = json.getJSONArray("data");
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject eh = datas.getJSONObject(i);
                        Exhibition e = new Exhibition();
                        e.setId(eh.getInt("idExhibitor"));
                        e.setImage(eh.getString("ImageLink"));
                        e.setName(eh.getString("Name"));
                        e.setBoot_no(eh.getString("BoothNo"));
                        e.setDescription(eh.getString("Description"));
                        e.setFavorite(eh.getBoolean("isFavorite"));
                        arrayExhibition.add(e);
                    }
                    listExhibitionAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class SearchTextWatcher implements TextWatcher {
        private ListExhibitionAdapter lAdapter;

        public SearchTextWatcher(ListExhibitionAdapter lAdapter) {
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

    public class AddFavoriteExhibition extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int position;
        private int idExhibitor;

        public AddFavoriteExhibition(int position, int idExhibitor) {
            this.position = position;
            this.idExhibitor = idExhibitor;
            progressDialog = new ProgressDialog(ListExhibitionEventActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(ListExhibitionEventActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getfavorexhibitor?idExhibitor=" + String.valueOf(idExhibitor) + "&idDevice=" + ListExhibitionEventActivity.this.idDevice + "&token=" + ListExhibitionEventActivity.this.token);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(ListExhibitionEventActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("T", json.toString());
                    JSONObject a = json.getJSONObject("data");
//                    listExhibitionAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
