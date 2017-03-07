package com.example.phanluongha.myfirstapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.model.Product;
import com.example.phanluongha.myfirstapplication.request.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = sharedpreferences.getString("token", "");
        String idDevice = sharedpreferences.getString("idDevice", "");
        if (token.length() > 0 && idDevice.length() > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent listExhibition = new Intent(MainActivity.this, ListExhibitionActivity.class);
                    startActivity(listExhibition);
                }
            }, 3000);
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
                    String token = json.getString("data");
                    sharedpreferences.edit().clear()
                            .putString("token", token).putString("idDevice", idDevice).commit();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent listExhibition = new Intent(MainActivity.this, ListExhibitionActivity.class);
                            startActivity(listExhibition);
                        }
                    }, 3000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
