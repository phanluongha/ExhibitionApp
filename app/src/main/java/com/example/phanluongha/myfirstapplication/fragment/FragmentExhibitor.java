package com.example.phanluongha.myfirstapplication.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.phanluongha.myfirstapplication.DetailExhibitionActivity;
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

import okhttp3.MultipartBody;

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
        View v = inflater.inflate(R.layout.fragment_exhibitor, container, false);
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
    public void onItemExhibitionClick(int id, boolean isFavorite) {

    }

    @Override
    public void onItemExhibitionClick(int position) {
        Exhibition e = arrayExhibition.get(position);
        Intent detailExhibition = new Intent(getActivity(), DetailExhibitionActivity.class);
        detailExhibition.putExtra("id", e.getId());
        detailExhibition.putExtra("idEvent", e.getIdEvent());
        detailExhibition.putExtra("isFavorite", e.isFavorite());
        startActivity(detailExhibition);
    }

    @Override
    public void keyClickedIndex(final int position) {
        final Exhibition ex = arrayExhibition.get(position);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        if (!ex.isFavorite()) {
            alertDialogBuilder.setTitle(this
                    .getString(R.string.add_favorite));
            alertDialogBuilder
                    .setMessage(
                            getString(R.string.add_favorite_confirm))
                    .setCancelable(false)
                    .setPositiveButton(
                            getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {
                                    MultipartBody.Builder m = new MultipartBody.Builder();
                                    m.setType(MultipartBody.FORM);
                                    m.addFormDataPart("type", "exhibitor");
                                    m.addFormDataPart("idExhibitor", String.valueOf(ex.getId()));
                                    m.addFormDataPart("idDevice", deviceId);
                                    m.addFormDataPart("token", token);
                                    new AddFavoriteExhibition(position, m).execute();

                                }
                            })

                    .setNegativeButton(
                            getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {

                                    dialog.cancel();
                                }
                            });
        } else {
            alertDialogBuilder.setTitle(this
                    .getString(R.string.remove_favorite));
            alertDialogBuilder
                    .setMessage(
                            getString(R.string.remove_favorite_confirm))
                    .setCancelable(false)
                    .setPositiveButton(
                            getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {
                                    MultipartBody.Builder m = new MultipartBody.Builder();
                                    m.setType(MultipartBody.FORM);
                                    m.addFormDataPart("type", "exhibitor");
                                    m.addFormDataPart("idExhibitor", String.valueOf(ex.getId()));
                                    m.addFormDataPart("idDevice", deviceId);
                                    m.addFormDataPart("token", token);
                                    new RemoveFavoriteExhibition(position, m).execute();
                                }
                            })

                    .setNegativeButton(
                            getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {

                                    dialog.cancel();
                                }
                            });
        }

        AlertDialog alertDialog = alertDialogBuilder
                .create();
        alertDialog.show();
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
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getlistfavoritedexhibitor?idDevice=" + deviceId + "&token=" + token);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
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
                        e.setAddress(eh.getString("Address"));
                        e.setFavorite(eh.getBoolean("isFavorite"));
                        e.setIdEvent(eh.getInt("idEvent"));
                        arrayExhibition.add(e);
                    }
                    exhibitionFavouriteAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class AddFavoriteExhibition extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int position;
        private MultipartBody.Builder m;

        public AddFavoriteExhibition(int position, MultipartBody.Builder m) {
            this.position = position;
            this.m = m;
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
            JSONObject json = jParser.getPostJSONFromUrl("http://188.166.241.242/api/addfavorite", m);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(getActivity(), json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (json.getInt("code") == 200) {
                        arrayExhibition.get(position).setFavorite(true);
                        exhibitionFavouriteAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class RemoveFavoriteExhibition extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int position;
        private MultipartBody.Builder m;

        public RemoveFavoriteExhibition(int position, MultipartBody.Builder m) {
            this.position = position;
            this.m = m;
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
            JSONObject json = jParser.getPostJSONFromUrl("http://188.166.241.242/api/deletefavorite", m);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progressDialog.dismiss();
            try {
                if (json.length() > 0 && json.has("error")) {
                    try {
                        Toast.makeText(getActivity(), json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (json.getInt("code") == 200) {
                        arrayExhibition.get(position).setFavorite(false);
                        exhibitionFavouriteAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
