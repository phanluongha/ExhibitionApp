package com.example.phanluongha.myfirstapplication;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.model.Exhibition;
import com.example.phanluongha.myfirstapplication.model.Product;
import com.example.phanluongha.myfirstapplication.request.JsonParser;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class DetailProductActivity extends DefaultActivity {

    private TextView txtProductName;
    private ImageView banner;
    private ImageView imgFavorite;
    private TextView txtDescription;
    private LinearLayout layoutExhibition;
    DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        banner = (ImageView) findViewById(R.id.banner);
        banner.getLayoutParams().width = metrics.widthPixels;
        banner.getLayoutParams().height = 2 * metrics.widthPixels / 3;
        imgFavorite = (ImageView) findViewById(R.id.imgFavorite);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        layoutExhibition = (LinearLayout) findViewById(R.id.layoutExhibition);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            txtProductName.setText(b.getString("name"));
            Glide
                    .with(DetailProductActivity.this)
                    .load(b.getString("image"))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(banner);
            txtDescription.setText(b.getString("description"));
            new GetExhibitionOfProduct(b.getInt("id"), b.getInt("idEvent")).execute();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public class GetExhibitionOfProduct extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int idProduct;
        private int idEvent;

        public GetExhibitionOfProduct(int idProduct, int idEvent) {
            this.idProduct = idProduct;
            this.idEvent = idEvent;
            progressDialog = new ProgressDialog(DetailProductActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(DetailProductActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getexhibitorproduct?idProduct=" + String.valueOf(idProduct) + "&idEvent=" + String.valueOf(idEvent) + "&token=" + DetailProductActivity.this.token + "&idDevice=" + DetailProductActivity.this.idDevice);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(DetailProductActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("T", json.toString());
                    JSONArray exhibitions = json.getJSONArray("data");
                    TreeNode root = TreeNode.root();
                    for (int i = 0; i < exhibitions.length(); i++) {
                        JSONObject exhibition = exhibitions.getJSONObject(i);
                        Exhibition ex = new Exhibition();
                        ex.setName(exhibition.getString("Name"));
                        TreeNode nodeCat = new TreeNode(ex).setViewHolder(new ExhibitionHolder(DetailProductActivity.this));
                        root.addChild(nodeCat);
                    }
                    AndroidTreeView tView = new AndroidTreeView(DetailProductActivity.this, root);
                    layoutExhibition.addView(tView.getView());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class ExhibitionHolder extends TreeNode.BaseNodeViewHolder<Exhibition> {

        public ExhibitionHolder(Context context) {
            super(context);
        }

        @Override
        public View createNodeView(TreeNode node, Exhibition value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.exhibition_detail_product_node, null, false);
            view.setLayoutParams(new ViewGroup.LayoutParams(metrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView node_value = (TextView) view.findViewById(R.id.node_value);
            node_value.setText(value.getName());
            return view;
        }
    }

}
