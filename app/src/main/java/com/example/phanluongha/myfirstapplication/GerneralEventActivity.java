package com.example.phanluongha.myfirstapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.base.NavigationActivity;
import com.example.phanluongha.myfirstapplication.model.EventCategory;
import com.example.phanluongha.myfirstapplication.request.JsonParser;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GerneralEventActivity extends NavigationActivity {

    private int id;
    private ImageView banner;
    private TextView txtEventName;
    private TextView txtEventDate;
    private TextView txtEventContent;
    private ImageView imgFacebook;
    private ImageView imgWeb;
    private ImageView imgEmail;
    private ImageView imgPhone;
    DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerneral_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        banner = (ImageView) findViewById(R.id.banner);
        banner.setLayoutParams(new RelativeLayout.LayoutParams(metrics.widthPixels, 2 * metrics.widthPixels / 3));
        txtEventName = (TextView) findViewById(R.id.txtEventName);
        txtEventDate = (TextView) findViewById(R.id.txtEventDate);
        txtEventContent = (TextView) findViewById(R.id.txtEventContent);
        imgFacebook = (ImageView) findViewById(R.id.imgFacebook);
        imgWeb = (ImageView) findViewById(R.id.imgWeb);
        imgEmail = (ImageView) findViewById(R.id.imgEmail);
        imgPhone = (ImageView) findViewById(R.id.imgPhone);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getInt("id");
        }
        initNavigation();
        new GetDetailEvent().execute();
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
    public class GetDetailEvent extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        public GetDetailEvent() {
            progressDialog = new ProgressDialog(GerneralEventActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(GerneralEventActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/geteventdetail?idEvent=" + String.valueOf(id)+"&token=" + GerneralEventActivity.this.token);
            return json;
        }

        @Override
        protected void onPostExecute(final JSONObject json) {
            progressDialog.dismiss();
            if (json.has("error")) {
                try {
                    Toast.makeText(GerneralEventActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    final JSONObject data = json.getJSONObject("data");
                    Glide
                            .with(GerneralEventActivity.this)
                            .load(data.getString("ImageLink2"))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .crossFade()
                            .into(banner);
                    txtEventName.setText(data.getString("Name"));
                    txtEventDate.setText(data.getString("Date"));
                    txtEventContent.setText(data.getString("Description"));
                    imgFacebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getString("FaceBookLink")));
                                startActivity(facebookIntent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    imgWeb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getString("WebLink")));
                                startActivity(facebookIntent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    imgEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto", data.getString("Email"), null));
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    imgPhone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (ActivityCompat.checkSelfPermission(GerneralEventActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.getString("Phone")));
                                    startActivity(intentCall);
                                } else {
                                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        ActivityCompat.requestPermissions(GerneralEventActivity.this,
                                                new String[]{Manifest.permission.CALL_PHONE},
                                                1);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imgPhone.performClick();
                }
                return;
            }
        }
    }
}
