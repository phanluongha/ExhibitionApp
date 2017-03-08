package com.example.phanluongha.myfirstapplication.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.adapter.ExhibitionFavouriteAdapter;
import com.example.phanluongha.myfirstapplication.ListExhibitionEventActivity;
import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.impl.RcvExhibitionClick;
import com.example.phanluongha.myfirstapplication.model.Exhibition;
import com.example.phanluongha.myfirstapplication.request.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by minhnguyen2 on 3/8/2017.
 */

public class FragmentExhibitor extends Fragment implements RcvExhibitionClick {

    RecyclerView rcvExhibitors;
    private ExhibitionFavouriteAdapter exhibitionFavouriteAdapter;
    private ArrayList<Exhibition> arrayExhibition;

    private String deviceId;
    private String token;

    public static FragmentExhibitor newInstance(String deviceId, String token) {
        FragmentExhibitor fragmentExhibitor = new FragmentExhibitor();
        fragmentExhibitor.deviceId = deviceId;
        fragmentExhibitor.token = token;
        return fragmentExhibitor;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exhibitor,container,false);
        rcvExhibitors = (RecyclerView) v.findViewById(R.id.rcvExhibitors);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvExhibitors.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrayExhibition = new ArrayList<>();
        exhibitionFavouriteAdapter = new ExhibitionFavouriteAdapter(getActivity(), arrayExhibition, this);
        rcvExhibitors.setAdapter(exhibitionFavouriteAdapter);
        new GetListExhibitionFavorites().execute();
    }

    @Override
    public void onItemExhibitionClick(int id) {

    }

    @Override
    public void keyClickedIndex(int position) {

    }


    public class GetListExhibitionFavorites extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        public GetListExhibitionFavorites() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(getActivity());
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getlistfavoritedexhibitor?idDevice="+deviceId+"&token="+token);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                //Log.d("minh","exhibitor "+ json.toString());
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(getActivity(), json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONArray datas = json.getJSONArray("data");
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject eh = datas.getJSONObject(i);
                        Exhibition e = new Exhibition();
                        e.setId(eh.getInt("idExhibitor"));
                        e.setImage(eh.getString("ImageLink"));
                        e.setName(eh.getString("Name"));
                        e.setBoot_no(eh.getString("BoothNo"));
                        e.setDescription(eh.getString("Description"));
                        e.setFavorite(eh.getBoolean("isFavorite"));
                        arrayExhibition.add(e);
                    }
                    exhibitionFavouriteAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
