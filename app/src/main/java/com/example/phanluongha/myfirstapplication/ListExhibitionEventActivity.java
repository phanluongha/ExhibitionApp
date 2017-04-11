package com.example.phanluongha.myfirstapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.adapter.ExhibitionAdapter;
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.base.NavigationActivity;
import com.example.phanluongha.myfirstapplication.impl.RcvExhibitionClick;
import com.example.phanluongha.myfirstapplication.model.Event;
import com.example.phanluongha.myfirstapplication.model.Exhibition;
import com.example.phanluongha.myfirstapplication.request.JsonParser;
import com.example.phanluongha.myfirstapplication.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import fancycoverflow.FancyCoverFlowSampleAdapter;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ListExhibitionEventActivity extends NavigationActivity implements RcvExhibitionClick {


    private RecyclerView rcvExhibitors;

    private ArrayList<Exhibition> arrayExhibition;
    private ExhibitionAdapter exhibitionAdapter;
    private EditText txtSearch;


    private int idPrivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_exhibition_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        rcvExhibitors = (RecyclerView) findViewById(R.id.rcvExhibitors);

        setSupportActionBar(toolbar);
        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        arrayExhibition = new ArrayList<Exhibition>();

        rcvExhibitors.setLayoutManager(new LinearLayoutManager(this));
        exhibitionAdapter = new ExhibitionAdapter(this, arrayExhibition, this);
        rcvExhibitors.setAdapter(exhibitionAdapter);

        final Bundle b = getIntent().getExtras();
        if (b != null) {
            idPrivate = b.getInt("id");
            getListExhibition(idPrivate);
        }
        txtSearch.addTextChangedListener(new SearchTextWatcher(exhibitionAdapter));
        initNavigation();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                getListExhibition(idPrivate);
            }
        }
    }

    public void onItemExhibitionClick(int id, boolean isFavorite) {

        Intent detailExhibition = new Intent(ListExhibitionEventActivity.this, DetailExhibitionActivity.class);
        detailExhibition.putExtra("id", id);
        detailExhibition.putExtra("idEvent", idPrivate);
        detailExhibition.putExtra("isFavorite", isFavorite);
        startActivityForResult(detailExhibition, 1);

    }

    @Override
    public void onItemExhibitionClick(int position) {

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

    private void getListExhibition(int id) {
        new GetListExhibition(id).execute();
    }

    @Override
    public void keyClickedIndex(final int position) {
        final Exhibition ex = arrayExhibition.get(position);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        if (!ex.isFavorite()) {
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
                                    m.addFormDataPart("type", "exhibitor");
                                    m.addFormDataPart("idExhibitor", String.valueOf(ex.getId()));
                                    m.addFormDataPart("idDevice", ListExhibitionEventActivity.this.idDevice);
                                    m.addFormDataPart("token", ListExhibitionEventActivity.this.token);
                                    new AddFavoriteExhibition(position, m).execute();

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
                                    m.addFormDataPart("type", "exhibitor");
                                    m.addFormDataPart("idExhibitor", String.valueOf(ex.getId()));
                                    m.addFormDataPart("idDevice", ListExhibitionEventActivity.this.idDevice);
                                    m.addFormDataPart("token", ListExhibitionEventActivity.this.token);
                                    new RemoveFavoriteExhibition(position, m).execute();
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
            JSONObject json = jParser.getJSONFromUrl(Config.SERVER_HOST +"getexhibitorlist?idEvent=" + String.valueOf(idEvent) + "&token=" + ListExhibitionEventActivity.this.token + "&idDevice=" + ListExhibitionEventActivity.this.idDevice);
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
                    arrayExhibition.clear();
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject eh = datas.getJSONObject(i);
                        Exhibition e = new Exhibition();
                        e.setId(eh.getInt("idExhibitor"));
                        e.setImage(eh.getString("ImageLink"));
                        e.setName(eh.getString("Name"));
                        e.setBoot_no(eh.getString("BoothNo"));
                        e.setFavorite(eh.getBoolean("isFavorite"));
                        e.setAddress(eh.getString("Address"));
                        arrayExhibition.add(e);
                    }
                    exhibitionAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class SearchTextWatcher implements TextWatcher {
        private ExhibitionAdapter lAdapter;

        public SearchTextWatcher(ExhibitionAdapter lAdapter) {
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
        private MultipartBody.Builder m;

        public AddFavoriteExhibition(int position, MultipartBody.Builder m) {
            this.position = position;
            this.m = m;
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
            JSONObject json = jParser.getPostJSONFromUrl(Config.SERVER_HOST +"addfavorite", m);
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
                    if (json.getInt("code") == 200) {
                        arrayExhibition.get(position).setFavorite(true);
                        exhibitionAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class RemoveFavoriteExhibition extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int position;
        private MultipartBody.Builder m;

        public RemoveFavoriteExhibition(int position, MultipartBody.Builder m) {
            this.position = position;
            this.m = m;
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
            JSONObject json = jParser.getPostJSONFromUrl(Config.SERVER_HOST +"deletefavorite", m);
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
                    if (json.getInt("code") == 200) {
                        arrayExhibition.get(position).setFavorite(false);
                        exhibitionAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
