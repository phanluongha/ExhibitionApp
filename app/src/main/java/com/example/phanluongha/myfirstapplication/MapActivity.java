package com.example.phanluongha.myfirstapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.phanluongha.myfirstapplication.customview.MyImageView;
import com.example.phanluongha.myfirstapplication.customview.TouchImageView;
import com.example.phanluongha.myfirstapplication.customview.ZoomView;
import com.example.phanluongha.myfirstapplication.model.MapNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.bitmap;
import static android.R.attr.measureAllChildren;

public class MapActivity extends AppCompatActivity {

    private LinearLayout contentLayout;
    private ZoomView zoomView;
    private MyImageView img;
    DisplayMetrics metrics;
    int oldWidth;
    int oldHeight;
    int newWidth;
    int newHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        contentLayout = (LinearLayout) findViewById(R.id.content_map);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_zoom, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        img = (MyImageView) v.findViewById(R.id.img);
        oldWidth = img.getDrawable().getIntrinsicWidth();
        oldHeight = img.getDrawable().getIntrinsicHeight();
        newWidth = metrics.widthPixels;
        newHeight = newWidth * oldHeight / oldWidth;
        img.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        contentLayout.addView(zoomView);
        zoomView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        collectDataMap();
    }

    private void collectDataMap() {
        try {
            JSONObject json = new JSONObject(this.js);
            JSONArray nodes = json.getJSONArray("nodes");
            img.arrayNode = new int[nodes.length()][nodes.length()];
            for (int i = 0; i < nodes.length(); i++) {
                for (int j = 0; j < nodes.length(); j++) {
                    img.arrayNode[i][j] = 0;
                }
            }
            JSONArray paths = json.getJSONArray("paths");
            for (int i = 0; i < paths.length(); i++) {
                JSONObject path = paths.getJSONObject(i);
                img.arrayNode[Integer.parseInt(path.getString("node0"))][Integer.parseInt(path.getString("node1"))] = path.getInt("cost");
                img.arrayNode[Integer.parseInt(path.getString("node1"))][Integer.parseInt(path.getString("node0"))] = path.getInt("cost");
            }
            for (int i = 0; i < nodes.length(); i++) {
                JSONObject node = nodes.getJSONObject(i);
                MapNode mn = new MapNode();
//                int x = node.getInt("x") * newWidth / oldWidth;
//                int y = node.getInt("y") * newHeight / oldHeight;
                int x = node.getInt("x");
                int y = node.getInt("y");

                mn.setX(x);
                mn.setY(y);
                mn.setStore(node.getInt("isStore") == 1);
                img.mapNodes.add(mn);
            }
            img.invalidate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String js = "{\n" +
            "\t\"nodeSize\": {\n" +
            "\t\t\t\"width\": 40,\n" +
            "\t\t\t\"height\": 40\n" +
            "\t},\n" +
            "    \"nodes\": [\n" +
            "              {\n" +
            "              \"x\": 379.000000,\n" +
            "              \"y\": 64.000000,\n" +
            "              \"isStore\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"x\": 481.500000,\n" +
            "              \"y\": 174.500000,\n" +
            "              \"isStore\":1\n" +
            "              },\n" +
            "              {\n" +
            "         \t  \"x\": 534.000000,\n" +
            "              \"y\": 392.000000,\n" +
            "              \"isStore\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"x\": 388.500000,\n" +
            "              \"y\": 531.000000,\n" +
            "              \"isStore\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"x\": 279.000000,\n" +
            "              \"y\": 531.000000,\n" +
            "              \"isStore\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"x\": 127.500000,\n" +
            "              \"y\": 557.500000,\n" +
            "              \"isStore\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"x\": 278.000000,\n" +
            "              \"y\": 561.000000,\n" +
            "              \"isStore\":0\n" +
            "              },\n" +
            "              {\n" +
            "              \"x\": 267.500000,\n" +
            "              \"y\": 275.000000,\n" +
            "              \"isStore\":0\n" +
            "              },\n" +
            "              {\n" +
            "              \"x\": 282.500000,\n" +
            "              \"y\": 314.500000,\n" +
            "              \"isStore\":0\n" +
            "              },\n" +
            "              {\n" +
            "              \"x\": 329.000000,\n" +
            "              \"y\": 405.500000,\n" +
            "              \"isStore\":0\n" +
            "              },\n" +
            "              {\n" +
            "              \"x\": 276.000000,\n" +
            "              \"y\": 404.500000,\n" +
            "              \"isStore\":0\n" +
            "              },\n" +
            "              {\n" +
            "              \"x\": 207.000000,\n" +
            "              \"y\": 144.500000,\n" +
            "              \"isStore\":0\n" +
            "              }\n" +
            "              ],\n" +
            "    \"paths\": [\n" +
            "              {\n" +
            "              \"node0\": \"0\",\n" +
            "              \"node1\": \"11\",\n" +
            "              \"cost\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"node0\": \"11\",\n" +
            "              \"node1\": \"7\",\n" +
            "              \"cost\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"node0\": \"7\",\n" +
            "              \"node1\": \"1\",\n" +
            "              \"cost\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"node0\": \"7\",\n" +
            "              \"node1\": \"8\",\n" +
            "              \"cost\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"node0\": \"8\",\n" +
            "              \"node1\": \"10\",\n" +
            "              \"cost\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"node0\": \"8\",\n" +
            "              \"node1\": \"9\",\n" +
            "              \"cost\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"node0\": \"10\",\n" +
            "              \"node1\": \"9\",\n" +
            "              \"cost\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"node0\": \"9\",\n" +
            "              \"node1\": \"3\",\n" +
            "              \"cost\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"node0\": \"9\",\n" +
            "              \"node1\": \"2\",\n" +
            "              \"cost\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"node0\": \"10\",\n" +
            "              \"node1\": \"4\",\n" +
            "              \"cost\":1\n" +
            "              },\n" +
            "              {\n" +
            "              \"node0\": \"4\",\n" +
            "              \"node1\": \"6\",\n" +
            "              \"cost\":1\n" +
            "              }\n" +
            "              ,\n" +
            "              {\n" +
            "              \"node0\": \"6\",\n" +
            "              \"node1\": \"5\",\n" +
            "              \"cost\":1\n" +
            "              }\n" +
            "\n" +
            "              ]\n" +
            "}\n";
}
