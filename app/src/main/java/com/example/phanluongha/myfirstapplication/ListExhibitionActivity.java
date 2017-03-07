package com.example.phanluongha.myfirstapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.base.DefaultActivity;
import com.example.phanluongha.myfirstapplication.impl.Animation;
import com.example.phanluongha.myfirstapplication.impl.EventCategoryChildClickListener;
import com.example.phanluongha.myfirstapplication.model.Event;
import com.example.phanluongha.myfirstapplication.model.EventCategory;
import com.example.phanluongha.myfirstapplication.request.JsonParser;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import fancycoverflow.FancyCoverFlowSampleAdapter;

public class ListExhibitionActivity extends DefaultActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, EventCategoryChildClickListener {
    private ImageView btnDrawer;
    private DrawerLayout drawer;
    private LinearLayout layoutCategotyAction;
    private ImageView imgCategotyAction;
    private FancyCoverFlow fancyCoverFlow;
    private LinearLayout listExhibition;
    private FancyCoverFlowSampleAdapter adapter;
    private ArrayList<Event> events;
    DisplayMetrics metrics;
    private EventCategoryChildClickListener eventCategoryChildClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_exhibition);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        eventCategoryChildClickListener = this;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnDrawer = (ImageView) findViewById(R.id.btnDrawer);
        btnDrawer.setOnClickListener(this);
        fancyCoverFlow = (FancyCoverFlow) this.findViewById(R.id.fancyCoverFlow);
        int fancyCoverHeight = 2 * metrics.widthPixels / 3 + pxFromDp(40);
        fancyCoverFlow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, fancyCoverHeight));
        listExhibition = (LinearLayout) findViewById(R.id.listExhibition);
        layoutCategotyAction = (LinearLayout) findViewById(R.id.layoutCategotyAction);
        imgCategotyAction = (ImageView) findViewById(R.id.imgCategotyAction);
        layoutCategotyAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listExhibition.getVisibility() == View.GONE) {
                    listExhibition.setVisibility(View.VISIBLE);
                    imgCategotyAction.setImageResource(R.drawable.arrow_up);
                } else {
                    imgCategotyAction.setImageResource(R.drawable.arrow_down);
                    listExhibition.setVisibility(View.GONE);
                }
            }
        });
        getListEvent(0);
        new GetListEventCategory().execute();

    }

    private void getListEvent(int category) {
        new GetListEvent(category).execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_exhibition, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDrawer:
                drawer.openDrawer(GravityCompat.END);
                break;
        }
    }

    @Override
    public void keyClickedIndex(int id) {
        getListEvent(id);
    }

    public class EventCategoryHolder extends TreeNode.BaseNodeViewHolder<EventCategory> {

        public EventCategoryHolder(Context context) {
            super(context);
        }

        @Override
        public View createNodeView(TreeNode node, EventCategory value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.layout_exhibition_node, null, false);
            view.setLayoutParams(new ViewGroup.LayoutParams(metrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tvValue = (TextView) view.findViewById(R.id.node_value);
            tvValue.setText(value.getName());
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

    public class EventCategoryHolderChild extends TreeNode.BaseNodeViewHolder<EventCategory> {

        public EventCategoryHolderChild(Context context) {
            super(context);
        }

        @Override
        public View createNodeView(TreeNode node, final EventCategory value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.layout_exhibition_node_child, null, false);
            view.setLayoutParams(new ViewGroup.LayoutParams(metrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tvValue = (TextView) view.findViewById(R.id.node_value);
            tvValue.setText(value.getName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventCategoryChildClickListener.keyClickedIndex(value.getId());
                }
            });
            return view;
        }
    }

    public class GetListEvent extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int category;

        public GetListEvent(int category) {
            this.category = category;
            progressDialog = new ProgressDialog(ListExhibitionActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(ListExhibitionActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/geteventlist" + (category > 0 ? "?idCategory=" + String.valueOf(category) + "&token=" + ListExhibitionActivity.this.token : "?token=" + ListExhibitionActivity.this.token));
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(ListExhibitionActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (events == null) {
                        events = new ArrayList<Event>();
                    } else {
                        events.clear();
                    }
                    JSONArray datas = json.getJSONArray("data");
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject ev = datas.getJSONObject(i);
                        Event e = new Event();
                        e.setImage(ev.getString("ImageLink"));
                        e.setId(ev.getInt("idEvent"));
                        events.add(e);
                    }
                    adapter = new FancyCoverFlowSampleAdapter(ListExhibitionActivity.this, events, metrics.widthPixels);
                    ListExhibitionActivity.this.fancyCoverFlow.setAdapter(adapter);
                    ListExhibitionActivity.this.fancyCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent detailEvent = new Intent(ListExhibitionActivity.this, DetailEventActivity.class);
                            detailEvent.putExtra("id", events.get(position).getId());
                            detailEvent.putExtra("banner", events.get(position).getImage());
                            startActivity(detailEvent);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class GetListEventCategory extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        public GetListEventCategory() {
            progressDialog = new ProgressDialog(ListExhibitionActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(ListExhibitionActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getcategorylist?token=" + ListExhibitionActivity.this.token);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            if (json.has("error")) {
                try {
                    Toast.makeText(ListExhibitionActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONArray cats = json.getJSONObject("data").getJSONArray("cat");
                    TreeNode root = TreeNode.root();
                    for (int i = 0; i < cats.length(); i++) {
                        JSONObject cat = cats.getJSONObject(i);
                        EventCategory eventCategory = new EventCategory();
                        eventCategory.setName(cat.getString("Name"));
                        eventCategory.setId(cat.getInt("idCategory"));
                        TreeNode nodeCat = new TreeNode(eventCategory).setViewHolder(new EventCategoryHolder(ListExhibitionActivity.this));
                        JSONArray childCat = cat.getJSONArray("childCat");
                        for (int j = 0; j < childCat.length(); j++) {
                            JSONObject childChildCats = childCat.getJSONObject(j);
                            EventCategory eventCategoryChild = new EventCategory();
                            eventCategoryChild.setName(childChildCats.getString("Name"));
                            eventCategoryChild.setId(childChildCats.getInt("idChildCategory"));
                            TreeNode nodeCatChild = new TreeNode(eventCategoryChild).setViewHolder(new EventCategoryHolderChild(ListExhibitionActivity.this));
                            nodeCat.addChildren(nodeCatChild);
                        }
                        root.addChild(nodeCat);
                    }
                    AndroidTreeView tView = new AndroidTreeView(ListExhibitionActivity.this, root);
                    listExhibition.addView(tView.getView());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int pxFromDp(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
