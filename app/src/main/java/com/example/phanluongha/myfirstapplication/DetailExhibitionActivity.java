package com.example.phanluongha.myfirstapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.customview.CircleImageView;
import com.example.phanluongha.myfirstapplication.model.Event;
import com.example.phanluongha.myfirstapplication.model.EventCategory;
import com.example.phanluongha.myfirstapplication.model.Product;
import com.example.phanluongha.myfirstapplication.request.JsonParser;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import fancycoverflow.FancyCoverFlowSampleAdapter;

public class DetailExhibitionActivity extends DefaultActivity {

    private ImageView banner;
    private ImageView imgFavorite;
    private TextView txtName;
    private TextView txtBooth;
    private TextView txtPlace;
    private TextView txtDescription;
    private TextView txtContact;
    private TextView txtEmail;
    private LinearLayout layoutProduct;
    DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_exhibition);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        banner = (ImageView) findViewById(R.id.banner);
        banner.getLayoutParams().width = metrics.widthPixels;
        banner.getLayoutParams().height = 2 * metrics.widthPixels / 3;
        imgFavorite = (ImageView) findViewById(R.id.imgFavorite);
        txtName = (TextView) findViewById(R.id.txtName);
        txtBooth = (TextView) findViewById(R.id.txtBooth);
        txtPlace = (TextView) findViewById(R.id.txtPlace);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtContact = (TextView) findViewById(R.id.txtContact);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        layoutProduct = (LinearLayout) findViewById(R.id.layoutProduct);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            new GetDetailExhibition(b.getInt("id")).execute();
            new GetProductOfExhibition(b.getInt("id"), b.getInt("idEvent")).execute();
        }
    }

    public class GetDetailExhibition extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int idExhibitor;

        public GetDetailExhibition(int idExhibitor) {
            this.idExhibitor = idExhibitor;
            progressDialog = new ProgressDialog(DetailExhibitionActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(DetailExhibitionActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getexhibitordetail?idExhibitor=" + String.valueOf(idExhibitor) + "&token=" + DetailExhibitionActivity.this.token);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(DetailExhibitionActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject ex = json.getJSONObject("data");
                    Glide
                            .with(DetailExhibitionActivity.this)
                            .load(ex.getString("ImageLink"))
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .crossFade()
                            .into(banner);
                    txtName.setText(ex.getString("Name"));
                    txtBooth.setText(ex.getString("BoothNo"));
                    txtPlace.setText(ex.getString("Address"));
                    txtDescription.setText(ex.getString("Description"));
                    txtContact.setText(ex.getString("Phone"));
                    txtEmail.setText(ex.getString("Email"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class GetProductOfExhibition extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int idExhibitor;
        private int idEvent;

        public GetProductOfExhibition(int idExhibitor, int idEvent) {
            this.idExhibitor = idExhibitor;
            this.idEvent = idEvent;
            progressDialog = new ProgressDialog(DetailExhibitionActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(DetailExhibitionActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getproductlistofexhibitor?idExhibitor=" + String.valueOf(idExhibitor) + "&token=" + DetailExhibitionActivity.this.token + "&idDevice=" + DetailExhibitionActivity.this.idDevice + "&idEvent=" + String.valueOf(idEvent));
            Log.e("T", "http://188.166.241.242/api/getproductlistofexhibitor?idExhibitor=" + String.valueOf(idExhibitor) + "&token=" + DetailExhibitionActivity.this.token + "&idDevice=" + DetailExhibitionActivity.this.idDevice + "&idEvent=" + String.valueOf(idEvent));
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(DetailExhibitionActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("T", json.toString());
                    JSONArray products = json.getJSONArray("data");
                    TreeNode root = TreeNode.root();
                    TreeNode nodeCat = new TreeNode(null).setViewHolder(new DetailExhibitionActivity.ProductCategoryHolder(DetailExhibitionActivity.this));
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject product = products.getJSONObject(i);
                        Product productChild = new Product();
                        productChild.setName(product.getString("Name"));
                        productChild.setId(product.getInt("idProduct"));
                        productChild.setImage(product.getString("ImageLink"));
                        productChild.setDescription(product.getString("Description"));
                        productChild.setFavorite(product.getBoolean("isFavorite"));
                        productChild.setIdEvent(product.getInt("idEvent"));
                        TreeNode nodeCatChild = new TreeNode(productChild).setViewHolder(new DetailExhibitionActivity.ProductHolderChild(DetailExhibitionActivity.this));
                        nodeCat.addChildren(nodeCatChild);
                        root.addChild(nodeCat);
                    }
                    AndroidTreeView tView = new AndroidTreeView(DetailExhibitionActivity.this, root);
                    layoutProduct.addView(tView.getView());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class ProductCategoryHolder extends TreeNode.BaseNodeViewHolder<Objects> {

        public ProductCategoryHolder(Context context) {
            super(context);
        }

        @Override
        public View createNodeView(TreeNode node, Objects value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.layout_product_node, null, false);
            view.setLayoutParams(new ViewGroup.LayoutParams(metrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT));
            final ImageView imgStatus = (ImageView) view.findViewById(R.id.imgStatus);
            imgStatus.setImageResource(R.drawable.keyboard_arrow_down);
            node.setClickListener(new TreeNode.TreeNodeClickListener() {
                @Override
                public void onClick(TreeNode node, Object value) {
                    if (imgStatus.getTag() == "0") {
                        imgStatus.setImageResource(R.drawable.keyboard_arrow_down);
                        imgStatus.setTag("1");
                    } else {
                        imgStatus.setImageResource(R.drawable.keyboard_arrow_up);
                        imgStatus.setTag("0");
                    }
                }
            });
            return view;
        }
    }

    public class ProductHolderChild extends TreeNode.BaseNodeViewHolder<Product> {

        public ProductHolderChild(Context context) {
            super(context);
        }

        @Override
        public View createNodeView(TreeNode node, final Product value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.layout_product_node_child, null, false);
            view.setLayoutParams(new ViewGroup.LayoutParams(metrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tvValue = (TextView) view.findViewById(R.id.node_value);
            tvValue.setText(value.getName());
            CircleImageView img = (CircleImageView) view.findViewById(R.id.img);
            Glide
                    .with(DetailExhibitionActivity.this)
                    .load(value.getImage())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(img);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailExhibition = new Intent(DetailExhibitionActivity.this, DetailProductActivity.class);
                    detailExhibition.putExtra("id", value.getId());
                    detailExhibition.putExtra("name", value.getName());
                    detailExhibition.putExtra("description", value.getDescription());
                    detailExhibition.putExtra("favorite", value.isFavorite());
                    detailExhibition.putExtra("image", value.getImage());
                    detailExhibition.putExtra("idEvent", value.getIdEvent());
                    startActivity(detailExhibition);
                }
            });
            return view;
        }
    }

}
