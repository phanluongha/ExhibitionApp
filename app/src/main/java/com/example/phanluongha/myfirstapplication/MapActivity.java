package com.example.phanluongha.myfirstapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.base.NavigationActivity;
import com.example.phanluongha.myfirstapplication.customview.MyImageView;
import com.example.phanluongha.myfirstapplication.customview.ZoomView;
import com.example.phanluongha.myfirstapplication.model.MapNode;
import com.example.phanluongha.myfirstapplication.request.JsonParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends NavigationActivity {

    private LinearLayout contentLayout;
    private ZoomView zoomView;
    private MyImageView img;
    DisplayMetrics metrics;
    int oldWidth;
    int oldHeight;
    int newWidth;
    int newHeight;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions defaultOptions;

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
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        contentLayout = (LinearLayout) findViewById(R.id.content_map);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            new GetMap(b.getInt("id")).execute();
        }
        initNavigation();
    }

    public class GetMap extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int idEvent;

        public GetMap(int idEvent) {
            this.idEvent = idEvent;
            progressDialog = new ProgressDialog(MapActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(MapActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getmap?idEvent=" + String.valueOf(idEvent) + "&token=" + MapActivity.this.token + "&idDevice=" + MapActivity.this.idDevice);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Log.e("T",json.toString());
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(MapActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject data = json.getJSONObject("data");
                    JSONObject JsonMap = data.getJSONObject("JsonMap");
                    JSONObject mapSize = JsonMap.getJSONObject("mapSize");
                    oldWidth = mapSize.getInt("width");
                    oldHeight = mapSize.getInt("height");
                    newWidth = metrics.widthPixels;
                    newHeight = newWidth * oldHeight / oldWidth;
                    View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_zoom, null, false);
                    v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    img = (MyImageView) v.findViewById(R.id.img);
                    img.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                            MapActivity.this).defaultDisplayImageOptions(
                            defaultOptions).build();
                    imageLoader.init(config);
                    defaultOptions = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.NONE).build();
                    final ProgressDialog spinner = new ProgressDialog(MapActivity.this);
                    spinner.setMessage("Loading...");
                    imageLoader.displayImage(data.getString("ImageLink"), img, defaultOptions, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            spinner.show();
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            spinner.dismiss();
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            spinner.dismiss();
                        }
                    });
                    zoomView = new ZoomView(MapActivity.this);
                    zoomView.addView(v);
                    contentLayout.addView(zoomView);
                    zoomView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    JSONArray nodes = JsonMap.getJSONArray("nodes");
                    img.arrayNode = new int[nodes.length()][nodes.length()];
                    for (int i = 0; i < nodes.length(); i++) {
                        for (int j = 0; j < nodes.length(); j++) {
                            img.arrayNode[i][j] = 0;
                        }
                    }
                    JSONArray paths = JsonMap.getJSONArray("paths");
                    for (int i = 0; i < paths.length(); i++) {
                        JSONObject path = paths.getJSONObject(i);
                        img.arrayNode[Integer.parseInt(path.getString("node0"))][Integer.parseInt(path.getString("node1"))] = path.getInt("cost");
                        img.arrayNode[Integer.parseInt(path.getString("node1"))][Integer.parseInt(path.getString("node0"))] = path.getInt("cost");
                    }
                    for (int i = 0; i < nodes.length(); i++) {
                        JSONObject node = nodes.getJSONObject(i);
                        MapNode mn = new MapNode();
                        int x = node.getInt("x") * newWidth / oldWidth;
                        int y = node.getInt("y") * newHeight / oldHeight;
                        mn.setX(x);
                        mn.setY(y);
                        mn.setStore(node.getInt("isStore") == 1);
                        img.mapNodes.add(mn);
                    }
                    img.invalidate();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
