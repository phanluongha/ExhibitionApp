package com.example.phanluongha.myfirstapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.model.Event;
import com.example.phanluongha.myfirstapplication.request.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import fancycoverflow.FancyCoverFlowSampleAdapter;

public class SignInActivity extends DefaultActivity implements View.OnClickListener {

    EditText edUsername;
    EditText edPassword;
    Button btnForgotPassword;
    TextView btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edUsername = (EditText) findViewById(R.id.edUsername);
        edPassword = (EditText) findViewById(R.id.edPassword);

        findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.btnSignUp).setOnClickListener(this);
        findViewById(R.id.btnForgotPassword).setOnClickListener(this);
        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
        new GetLinkForgotPass().execute();
        new GetLinkSignUp().execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                if (username.trim().length() == 0 || password.trim().length() == 0) {
                    Toasty.error(this, "Please check again ID or Password", Toast.LENGTH_SHORT).show();
                } else {
                    new CheckUserLogin(username, password).execute();
                }
                break;
        }
    }


    private class CheckUserLogin extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private String username;
        private String password;

        CheckUserLogin(String username, String password) {
            this.username = username;
            this.password = password;
            progressDialog = new ProgressDialog(SignInActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(SignInActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/checkuserlogin?username=" + username + "&password=" + password + "&token=" + SignInActivity.this.token);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            if (json.length() > 0 && json.has("error")) {
                try {
                    Toast.makeText(SignInActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    int result = json.getInt("code");
                    String resultText = json.getString("message");
                    if (result == 200) {
                        Toasty.success(SignInActivity.this, resultText, Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("check_login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("is_login", true);
                        editor.putString("username", username);
                        editor.apply();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("username", username);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Toasty.error(SignInActivity.this, resultText, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class GetLinkForgotPass extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private String username;
        private String password;

        GetLinkForgotPass() {
            progressDialog = new ProgressDialog(SignInActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(SignInActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getforgotpasswordlink?token=" + SignInActivity.this.token);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            if (json.length() > 0 && json.has("error")) {
                try {
                    Toast.makeText(SignInActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    int result = json.getInt("code");
                    final String resultText = json.getString("data");
                    if (result == 200) {
                        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent forgotIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultText));
                                startActivity(forgotIntent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class GetLinkSignUp extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private String username;
        private String password;

        GetLinkSignUp() {
            progressDialog = new ProgressDialog(SignInActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(SignInActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getsignuplink?token=" + SignInActivity.this.token);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            if (json.length() > 0 && json.has("error")) {
                try {
                    Toast.makeText(SignInActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    int result = json.getInt("code");
                    final String resultText = json.getString("data");
                    if (result == 200) {
                        btnSignUp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent signupIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultText));
                                startActivity(signupIntent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
