package com.example.phanluongha.myfirstapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phanluongha.myfirstapplication.base.NavigationActivity;
import com.example.phanluongha.myfirstapplication.impl.EventCategoryChildClickListener;
import com.example.phanluongha.myfirstapplication.model.Event;
import com.example.phanluongha.myfirstapplication.model.EventCategory;
import com.example.phanluongha.myfirstapplication.request.JsonParser;
import com.example.phanluongha.myfirstapplication.utils.Config;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import fancycoverflow.FancyCoverFlowSampleAdapter;

public class ListExhibitionActivity extends NavigationActivity implements EventCategoryChildClickListener {

    private ScrollView scrollView;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout layoutCategotyAction;
    private ImageView imgCategotyAction;
    private FancyCoverFlow fancyCoverFlow;
    private LinearLayout listExhibition;
    private FancyCoverFlowSampleAdapter adapter;
    private ArrayList<Event> events;
    DisplayMetrics metrics;


    private EventCategoryChildClickListener eventCategoryChildClickListener;
    private ImageView imgAd;
    private TextView txtAdvertise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_exhibition);
        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scrollView = (ScrollView) findViewById(R.id.content_list_exhibition);
        listExhibition = (LinearLayout) findViewById(R.id.listExhibition);
        layoutCategotyAction = (LinearLayout) findViewById(R.id.layoutCategotyAction);
        imgCategotyAction = (ImageView) findViewById(R.id.imgCategotyAction);

        fancyCoverFlow = (FancyCoverFlow) this.findViewById(R.id.fancyCoverFlow);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        eventCategoryChildClickListener = this;


        int fancyCoverHeight = 2 * metrics.widthPixels / 3 + pxFromDp(40);
        fancyCoverFlow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, fancyCoverHeight));

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
        imgAd = (ImageView) findViewById(R.id.imgAd);
        imgAd.setLayoutParams(new RelativeLayout.LayoutParams(metrics.widthPixels, metrics.widthPixels / 3));
        txtAdvertise = (TextView) findViewById(R.id.txtAdvertise);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        fancyCoverFlow.setOnTouchListener(new View.OnTouchListener() {
            float oldTouchValueX = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN: {
                        swipeLayout.setEnabled(false);
                        oldTouchValueX = event.getX();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE: {
                        float currentX = event.getX();
                        if (Math.abs(oldTouchValueX - currentX) > 30) {
                            swipeLayout.setEnabled(false);
                        } else {
                            swipeLayout.setEnabled(true);
                        }
                        break;
                    }
                }
                return false;
            }
        });
        initNavigation();
        getListEvent(0);
        new GetListEventCategory().execute();
        new GetAd().execute();
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                getListEvent(0);
                new GetListEventCategory().execute();
            }
        });


    }

    private void getListEvent(int category) {
        new GetListEvent(category).execute();
    }

    @Override
    public void keyClickedIndex(int id) {
        scrollView.smoothScrollTo(0, 0);
        getListEvent(id);
    }


    private class EventCategoryHolder extends TreeNode.BaseNodeViewHolder<EventCategory> {

        EventCategoryHolder(Context context) {
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

    private class EventCategoryHolderChild extends TreeNode.BaseNodeViewHolder<EventCategory> {

        EventCategoryHolderChild(Context context) {
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

    private class GetListEvent extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int category;

        GetListEvent(int category) {
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
            JSONObject json = jParser.getJSONFromUrl(Config.SERVER_HOST + "geteventlist" + "?idDevice=" + ListExhibitionActivity.this.idDevice + "&token=" + ListExhibitionActivity.this.token + (category > 0 ? "&idChildCategory=" + String.valueOf(category) : ""));
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Log.e("T", json.toString());
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
                        e.setImage2(ev.getString("ImageLink2"));
                        e.setName(ev.getString("Name"));
                        e.setId(ev.getInt("idEvent"));
                        e.setOpen(ev.getInt("isOpen") == 1);
                        e.setWebLink(ev.getString("WebLink"));
                        events.add(e);
                    }
                    adapter = new FancyCoverFlowSampleAdapter(ListExhibitionActivity.this, events, metrics.widthPixels);
                    ListExhibitionActivity.this.fancyCoverFlow.setAdapter(adapter);
                    ListExhibitionActivity.this.fancyCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (events.get(position).isOpen()) {
                                Intent detailEvent = new Intent(ListExhibitionActivity.this, DetailEventActivity.class);
                                detailEvent.putExtra("id", events.get(position).getId());
                                detailEvent.putExtra("banner", events.get(position).getImage2());
                                detailEvent.putExtra("name", events.get(position).getName());
                                startActivity(detailEvent);
                            } else {
                                Intent general = new Intent(ListExhibitionActivity.this, GerneralEventActivity.class);
                                general.putExtra("id", events.get(position).getId());
                                startActivity(general);
                            }
                        }
                    });
                    if (datas.length() > 1)
                        ListExhibitionActivity.this.fancyCoverFlow.setSelection(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetListEventCategory extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        GetListEventCategory() {
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
            JSONObject json = jParser.getJSONFromUrl(Config.SERVER_HOST + "getcategorylist" + "?idDevice=" + ListExhibitionActivity.this.idDevice + "&token=" + ListExhibitionActivity.this.token);
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
                    listExhibition.removeAllViews();
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


    public class GetAd extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        public GetAd() {
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
            JSONObject json = jParser.getJSONFromUrl(Config.SERVER_HOST + "getadvertisedetail?idAdvertise=2&token=" + ListExhibitionActivity.this.token + "&idDevice=" + ListExhibitionActivity.this.idDevice);
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
                    JSONObject data = json.getJSONArray("data").getJSONObject(0);
                    Glide
                            .with(ListExhibitionActivity.this)
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

    public int pxFromDp(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
