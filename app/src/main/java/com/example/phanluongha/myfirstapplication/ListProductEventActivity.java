package com.example.phanluongha.myfirstapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.adapter.ListExhibitionAdapter;
import com.example.phanluongha.myfirstapplication.adapter.ListProductAdapter;
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.model.Exhibition;
import com.example.phanluongha.myfirstapplication.model.Product;
import com.example.phanluongha.myfirstapplication.request.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListProductEventActivity extends DefaultActivity {

    private ListView listView;
    private ArrayList<Product> arrayExhibition;
    private ListProductAdapter listProductAdapter;
    private EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        listView = (ListView) findViewById(R.id.listView);
        arrayExhibition = new ArrayList<Product>();
        listProductAdapter = new ListProductAdapter(this,
                R.layout.item_product, arrayExhibition);
        listView.setAdapter(listProductAdapter);

        final Bundle b = getIntent().getExtras();
        if (b != null) {
            final int idEvent = b.getInt("id");
            getListProduct(idEvent);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position,
                                        long id) {
                    Intent detailExhibition = new Intent(ListProductEventActivity.this, DetailProductActivity.class);
                    detailExhibition.putExtra("id", listProductAdapter.getItem(position).getId());
                    detailExhibition.putExtra("name", listProductAdapter.getItem(position).getName());
                    detailExhibition.putExtra("description", listProductAdapter.getItem(position).getDescription());
                    detailExhibition.putExtra("favorite", listProductAdapter.getItem(position).isFavorite());
                    detailExhibition.putExtra("image", listProductAdapter.getItem(position).getImage());
                    detailExhibition.putExtra("idEvent", idEvent);
                    startActivity(detailExhibition);
                }
            });
        }
        txtSearch.addTextChangedListener(new ListProductEventActivity.SearchTextWatcher(listProductAdapter));

    }

    private void getListProduct(int id) {
        new GetListProduct(id).execute();
    }

    public class GetListProduct extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int idEvent;

        public GetListProduct(int idEvent) {
            this.idEvent = idEvent;
            progressDialog = new ProgressDialog(ListProductEventActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(ListProductEventActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getproductlistofevent?idEvent=" + String.valueOf(idEvent) + "&idDevice=" + ListProductEventActivity.this.idDevice + "&token=" + ListProductEventActivity.this.token);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(ListProductEventActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONArray datas = json.getJSONArray("data");
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject eh = datas.getJSONObject(i);
                        Product e = new Product();
                        e.setId(eh.getInt("idProduct"));
                        e.setImage(eh.getString("ImageLink"));
                        e.setName(eh.getString("Name"));
                        e.setBoot_no(eh.getString("BoothNo"));
                        e.setDescription(eh.getString("Description"));
                        e.setFavorite(eh.getBoolean("isFavorite"));
                        arrayExhibition.add(e);
                    }
                    listProductAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class SearchTextWatcher implements TextWatcher {
        private ListProductAdapter lAdapter;

        public SearchTextWatcher(ListProductAdapter lAdapter) {
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

}
