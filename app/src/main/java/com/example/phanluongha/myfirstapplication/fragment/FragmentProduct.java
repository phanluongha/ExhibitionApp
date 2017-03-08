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

import com.example.phanluongha.myfirstapplication.ListProductEventActivity;
import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.adapter.ProductAdapter;
import com.example.phanluongha.myfirstapplication.adapter.ProductFavouriteAdapter;
import com.example.phanluongha.myfirstapplication.impl.RcvProductClick;
import com.example.phanluongha.myfirstapplication.model.Product;
import com.example.phanluongha.myfirstapplication.request.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by minhnguyen2 on 3/8/2017.
 */

public class FragmentProduct extends Fragment implements RcvProductClick {
    private RecyclerView rcvProducts;
    private ProductFavouriteAdapter productAdapter;

    private ArrayList<Product> arrayExhibition;

    private String deviceId;
    private String token;

    public static FragmentProduct newInstance(String deviceId, String token) {
        FragmentProduct fragmentProduct = new FragmentProduct();
        fragmentProduct.deviceId = deviceId;
        fragmentProduct.token = token;
        return fragmentProduct;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);
        rcvProducts = (RecyclerView) v.findViewById(R.id.rcvProduct);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrayExhibition = new ArrayList<>();
        productAdapter = new ProductFavouriteAdapter(getActivity(), arrayExhibition, this);
        rcvProducts.setAdapter(productAdapter);
        new GetListProductFavourites().execute();
    }

    @Override
    public void onItemProductClick(Product product) {

    }

    public class GetListProductFavourites extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        public GetListProductFavourites() {
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
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getlistfavoritedproduct?idDevice=" + deviceId + "&token=" + token);
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
                        Product e = new Product();
                        e.setId(eh.getInt("idProduct"));
                        e.setImage(eh.getString("ImageLink"));
                        e.setName(eh.getString("Name"));
                        e.setBoot_no(eh.getString("BoothNo"));
                        e.setDescription(eh.getString("Description"));
                        e.setFavorite(eh.getBoolean("isFavorite"));
                        arrayExhibition.add(e);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
