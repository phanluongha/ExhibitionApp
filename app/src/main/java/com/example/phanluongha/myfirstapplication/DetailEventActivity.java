package com.example.phanluongha.myfirstapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phanluongha.myfirstapplication.adapter.ListActivityEventAdapter;
import com.example.phanluongha.myfirstapplication.base.NavigationActivity;
import com.example.phanluongha.myfirstapplication.request.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailEventActivity extends NavigationActivity implements View.OnClickListener {

    private TextView txtEventName;
    private TextView txtAbout;
    private TextView txtExhibitor;
    private TextView txtProduct;
    private TextView txtActivity;
    private TextView txtConference;
    private TextView txtPlace;
    private ImageView banner;
    private ImageView imgAd;
    private TextView txtAdvertise;
    DisplayMetrics metrics;
    private int id;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        banner = (ImageView) findViewById(R.id.banner);
        banner.setLayoutParams(new LinearLayout.LayoutParams(metrics.widthPixels, 2 * metrics.widthPixels / 3));
        txtEventName = (TextView) findViewById(R.id.txtEventName);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getInt("id");
            Glide
                    .with(this)
                    .load(b.getString("banner"))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(banner);
            txtEventName.setText(b.getString("name"));
        }
        txtAbout = (TextView) findViewById(R.id.txtAbout);
        txtAbout.setOnClickListener(this);
        txtExhibitor = (TextView) findViewById(R.id.txtExhibitor);
        txtExhibitor.setOnClickListener(this);
        txtProduct = (TextView) findViewById(R.id.txtProduct);
        txtProduct.setOnClickListener(this);
        txtActivity = (TextView)findViewById(R.id.txtActivity);
        txtActivity.setOnClickListener(this);
        txtConference = (TextView) findViewById(R.id.txtConference);
        txtConference.setOnClickListener(this);
        txtPlace = (TextView) findViewById(R.id.txtPlace);
        txtPlace.setOnClickListener(this);
        imgAd = (ImageView) findViewById(R.id.imgAd);
        imgAd.setLayoutParams(new RelativeLayout.LayoutParams(metrics.widthPixels, metrics.widthPixels / 3));
        txtAdvertise = (TextView) findViewById(R.id.txtAdvertise);
        initNavigation();
        new GetAd().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtAbout:
                Intent general = new Intent(DetailEventActivity.this, GerneralEventActivity.class);
                general.putExtra("id", id);
                startActivity(general);
                break;
            case R.id.txtExhibitor:
                Intent exhibitor = new Intent(DetailEventActivity.this, ListExhibitionEventActivity.class);
                exhibitor.putExtra("id", id);
                startActivity(exhibitor);
                break;
            case R.id.txtProduct:
                Intent product = new Intent(DetailEventActivity.this, ListProductEventActivity.class);
                product.putExtra("id", id);
                startActivity(product);
                break;
            case R.id.txtActivity:
                Intent activity = new Intent(DetailEventActivity.this, ListActivityEventActivity.class);
                activity.putExtra("id", id);
                activity.putExtra("idTypeActivities", 1);
                startActivity(activity);
                break;
            case R.id.txtConference:
                Intent conference = new Intent(DetailEventActivity.this, ListActivityEventActivity.class);
                conference.putExtra("id", id);
                conference.putExtra("idTypeActivities", 2);
                startActivity(conference);
                break;
            case R.id.txtPlace:
                Intent map = new Intent(DetailEventActivity.this, MapActivity.class);
                map.putExtra("id", id);
                startActivity(map);
                break;
        }
    }
    public class GetAd extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        public GetAd() {
            progressDialog = new ProgressDialog(DetailEventActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(DetailEventActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getadvertisedetail?idAdvertise=3&token=" + DetailEventActivity.this.token + "&idDevice=" + DetailEventActivity.this.idDevice);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(DetailEventActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject data = json.getJSONArray("data").getJSONObject(0);
                    Glide
                            .with(DetailEventActivity.this)
                            .load(data.getString("Image"))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .crossFade()
                            .centerCrop()
                            .into(imgAd);
                    txtAdvertise.setText(Html.fromHtml(data.getString("Description")));
                    final String link = data.getString("Link");
                    if (link.length() > 0) {
                        imgAd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                startActivity(browserIntent);
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
