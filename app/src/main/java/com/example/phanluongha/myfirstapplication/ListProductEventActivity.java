
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.adapter.ProductAdapter;
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.base.NavigationActivity;
import com.example.phanluongha.myfirstapplication.impl.RcvProductClick;
import com.example.phanluongha.myfirstapplication.model.Exhibition;
import com.example.phanluongha.myfirstapplication.model.Product;
import com.example.phanluongha.myfirstapplication.request.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;

public class ListProductEventActivity extends NavigationActivity implements RcvProductClick {

    private RecyclerView rcvProduct;
    private ArrayList<Product> arrayProduct;
    private ProductAdapter productAdapter;
    private EditText txtSearch;
    private int idEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        rcvProduct = (RecyclerView) findViewById(R.id.rcvProduct);
        arrayProduct = new ArrayList<>();
        productAdapter = new ProductAdapter(this, arrayProduct, this);

        rcvProduct.setLayoutManager(new LinearLayoutManager(this));
        rcvProduct.setAdapter(productAdapter);

        final Bundle b = getIntent().getExtras();
        if (b != null) {
            idEvent = b.getInt("id");
            getListProduct(idEvent);
        }
        txtSearch.addTextChangedListener(new SearchTextWatcher(productAdapter));
        initNavigation();

    }

    private void getListProduct(int id) {
        new GetListProduct(id).execute();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                getListProduct(idEvent);
            }
        }
    }

    @Override
    public void onItemProductClick(Product product) {
        Intent detailExhibition = new Intent(ListProductEventActivity.this, DetailProductActivity.class);
        detailExhibition.putExtra("id", product.getId());
        detailExhibition.putExtra("name", product.getName());
        detailExhibition.putExtra("description", product.getDescription());
        detailExhibition.putExtra("isFavorite", product.isFavorite());
        detailExhibition.putExtra("image", product.getImage());
        detailExhibition.putExtra("idEvent", idEvent);
        startActivityForResult(detailExhibition, 1);
    }

    @Override
    public void onItemFavoriteProductClick(final int position) {
        final Product pr = arrayProduct.get(position);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        if (!pr.isFavorite()) {
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
                                    m.addFormDataPart("type", "product");
                                    m.addFormDataPart("idProduct", String.valueOf(pr.getId()));
                                    m.addFormDataPart("idDevice", ListProductEventActivity.this.idDevice);
                                    m.addFormDataPart("token", ListProductEventActivity.this.token);
                                    new AddFavoriteProduct(position, m).execute();

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
                                    m.addFormDataPart("type", "product");
                                    m.addFormDataPart("idProduct", String.valueOf(pr.getId()));
                                    m.addFormDataPart("idDevice", ListProductEventActivity.this.idDevice);
                                    m.addFormDataPart("token", ListProductEventActivity.this.token);
                                    new RemoveFavoriteProduct(position, m).execute();
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
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getproductlistofevent?idEvent=" + String.valueOf(idEvent) + "&idDevice=" + com.example.phanluongha.myfirstapplication.ListProductEventActivity.this.idDevice + "&token=" + com.example.phanluongha.myfirstapplication.ListProductEventActivity.this.token);
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
                    arrayProduct.clear();
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject eh = datas.getJSONObject(i);
                        Product e = new Product();
                        e.setId(eh.getInt("idProduct"));
                        e.setImage(eh.getString("ImageLink"));
                        e.setName(eh.getString("Name"));
                        e.setBoot_no(eh.getString("BoothNo"));
                        e.setDescription(eh.getString("Description"));
                        e.setFavorite(eh.getBoolean("isFavorite"));
                        arrayProduct.add(e);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class SearchTextWatcher implements TextWatcher {
        private ProductAdapter lAdapter;

        public SearchTextWatcher(ProductAdapter lAdapter) {
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

    public class AddFavoriteProduct extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int position;
        private MultipartBody.Builder m;

        public AddFavoriteProduct(int position, MultipartBody.Builder m) {
            this.position = position;
            this.m = m;
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
            JSONObject json = jParser.getPostJSONFromUrl("http://188.166.241.242/api/addfavorite", m);
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
                    if (json.getInt("code") == 200) {
                        arrayProduct.get(position).setFavorite(true);
                        productAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class RemoveFavoriteProduct extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int position;
        private MultipartBody.Builder m;

        public RemoveFavoriteProduct(int position, MultipartBody.Builder m) {
            this.position = position;
            this.m = m;
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
            JSONObject json = jParser.getPostJSONFromUrl("http://188.166.241.242/api/deletefavorite", m);
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
                    if (json.getInt("code") == 200) {
                        arrayProduct.get(position).setFavorite(false);
                        productAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
