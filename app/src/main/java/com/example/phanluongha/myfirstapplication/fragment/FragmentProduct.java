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

import com.example.phanluongha.myfirstapplication.DetailProductActivity;
import com.example.phanluongha.myfirstapplication.ListProductEventActivity;
import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.adapter.ProductAdapter;
import com.example.phanluongha.myfirstapplication.adapter.ProductFavouriteAdapter;
import com.example.phanluongha.myfirstapplication.impl.RcvProductClick;
import com.example.phanluongha.myfirstapplication.model.Product;
import com.example.phanluongha.myfirstapplication.request.JsonParser;
import com.example.phanluongha.myfirstapplication.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;

/**
 * Created by minhnguyen2 on 3/8/2017.
 */

public class FragmentProduct extends Fragment implements RcvProductClick {
    private RecyclerView rcvProducts;
    private ProductFavouriteAdapter productAdapter;

    private ArrayList<Product> arrayProduct;

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
        arrayProduct = new ArrayList<>();
        productAdapter = new ProductFavouriteAdapter(getActivity(), arrayProduct, this);
        rcvProducts.setAdapter(productAdapter);
        new GetListProductFavourites().execute();
    }

    @Override
    public void onItemProductClick(Product product) {
        Intent detailExhibition = new Intent(getActivity(), DetailProductActivity.class);
        detailExhibition.putExtra("id", product.getId());
        detailExhibition.putExtra("name", product.getName());
        detailExhibition.putExtra("description", product.getDescription());
        detailExhibition.putExtra("isFavorite", product.isFavorite());
        detailExhibition.putExtra("image", product.getImage());
        detailExhibition.putExtra("idEvent", product.getIdEvent());
        startActivity(detailExhibition);
    }

    @Override
    public void onItemFavoriteProductClick(final int position) {
        final Product pr = arrayProduct.get(position);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        if (!pr.isFavorite()) {
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
                                    m.addFormDataPart("type", "product");
                                    m.addFormDataPart("idProduct", String.valueOf(pr.getId()));
                                    m.addFormDataPart("idDevice", deviceId);
                                    m.addFormDataPart("token", token);
                                    new AddFavoriteProduct(position, m).execute();

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
                                    m.addFormDataPart("type", "product");
                                    m.addFormDataPart("idProduct", String.valueOf(pr.getId()));
                                    m.addFormDataPart("idDevice", deviceId);
                                    m.addFormDataPart("token", token);
                                    new RemoveFavoriteProduct(position, m).execute();
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
            JSONObject json = jParser.getJSONFromUrl(Config.SERVER_HOST + "getlistfavoritedproduct?idDevice=" + deviceId + "&token=" + token);
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
                        e.setIdEvent(eh.getInt("idEvent"));
                        arrayProduct.add(e);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class AddFavoriteProduct extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int position;
        private MultipartBody.Builder m;

        public AddFavoriteProduct(int position, MultipartBody.Builder m) {
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
            JSONObject json = jParser.getPostJSONFromUrl(Config.SERVER_HOST + "addfavorite", m);
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
                        arrayProduct.get(position).setFavorite(true);
                        productAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class RemoveFavoriteProduct extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private int position;
        private MultipartBody.Builder m;

        public RemoveFavoriteProduct(int position, MultipartBody.Builder m) {
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
            JSONObject json = jParser.getPostJSONFromUrl(Config.SERVER_HOST + "deletefavorite", m);
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
                        arrayProduct.get(position).setFavorite(false);
                        productAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
