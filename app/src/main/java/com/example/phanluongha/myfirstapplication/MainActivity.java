package com.example.phanluongha.myfirstapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phanluongha.myfirstapplication.model.Product;
import com.example.phanluongha.myfirstapplication.request.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    private ImageView imgLogo;
    private LinearLayout layoutAdvertise;
    private ImageView imgAdvertise;
    private TextView txtAdvertise;
    private TextView txtSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        layoutAdvertise = (LinearLayout) findViewById(R.id.layoutAdvertise);
        imgAdvertise = (ImageView) findViewById(R.id.imgAdvertise);
        txtAdvertise = (TextView) findViewById(R.id.txtAdvertise);
        txtSkip = (TextView) findViewById(R.id.txtSkip);
        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listExhibition = new Intent(MainActivity.this, ListExhibitionActivity.class);
                startActivity(listExhibition);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        final String token = sharedpreferences.getString("token", "");
        final String idDevice = sharedpreferences.getString("idDevice", "");
        if (token.length() > 0 && idDevice.length() > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new GetAd(token, idDevice).execute();
                }
            }, 2000);
        } else {
            String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            new GetToken(android_id).execute();
        }
    }

    public class GetToken extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private String idDevice;

        public GetToken(String idEvent) {
            this.idDevice = idEvent;
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(MainActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/checkdeviceid?idDevice=" + idDevice);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(MainActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    final JSONObject data = json.getJSONObject("data");
                    sharedpreferences.edit().clear()
                            .putString("token", data.getString("token")).putString("idDevice", data.getString("idDevice")).commit();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                new GetAd(data.getString("token"), data.getString("idDevice")).execute();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 2000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class GetAd extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private String token;
        private String idDevice;

        public GetAd(String token, String idDevice) {
            this.token = token;
            this.idDevice = idDevice;
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(MainActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getadvertisedetail?idAdvertise=1&token=" + token + "&idDevice=" + idDevice);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(MainActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject data = json.getJSONArray("data").getJSONObject(0);
                    layoutAdvertise.setVisibility(View.VISIBLE);
                    imgLogo.setVisibility(View.GONE);
                    Glide
                            .with(MainActivity.this)
                            .load(data.getString("Image"))
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .crossFade()
                            .into(imgAdvertise);
                    txtAdvertise.setText(Html.fromHtml(data.getString("Description")));
                    final String link = data.getString("Link");
                    if (link.length() > 0) {
                        imgAdvertise.setOnClickListener(new View.OnClickListener() {
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
