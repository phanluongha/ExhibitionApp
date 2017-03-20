package com.example.phanluongha.myfirstapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phanluongha.myfirstapplication.DetailProductActivity;
import com.example.phanluongha.myfirstapplication.ListProductEventActivity;
import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.impl.RcvExhibitionClick;
import com.example.phanluongha.myfirstapplication.impl.RcvProductClick;
import com.example.phanluongha.myfirstapplication.model.Exhibition;
import com.example.phanluongha.myfirstapplication.model.Product;

import java.util.ArrayList;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ExhibitionHolder> {
    private Context context;
    private ArrayList<Product> arrayProduct;
    private ArrayList<Product> filteredItems;
    private RcvProductClick rcvProductClick;
    private ProductAdapter.ItemFilter mFilter = new ProductAdapter.ItemFilter();


    public ProductAdapter(Context context,
                          ArrayList<Product> arrayExhibition, RcvProductClick rcvProductClick) {

        this.context = context;
        this.arrayProduct = arrayExhibition;
        this.rcvProductClick = rcvProductClick;
        this.filteredItems = arrayExhibition;
    }


    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    @Override
    public ExhibitionHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_exhibition, viewGroup, false);

        return new ExhibitionHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ExhibitionHolder holder, final int position) {
        Product ex = filteredItems.get(position);

        Glide
                .with(context)
                .load(ex.getImage())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(holder.img);

        holder.txtName.setText(ex.getName());

//        holder.txtBooth.setText(ex.getBoot_no());
        holder.txtBooth.setVisibility(View.GONE);
        holder.txtDescription.setText(ex.getDescription());
        if (ex.isFavorite()) {
            holder.imgFavotite.setImageResource(R.drawable.love_fill);
        } else {
            holder.imgFavotite.setImageResource(R.drawable.love_empty);
        }
        holder.imgFavotite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcvProductClick.onItemFavoriteProductClick(position);
            }
        });
    }

    public class ExhibitionHolder extends RecyclerView.ViewHolder {

        protected ImageView img;
        protected TextView txtName;
        TextView txtBooth;
        TextView txtDescription;
        ImageView imgFavotite;

        public ExhibitionHolder(View v) {
            super(v);
            img = (ImageView) v.findViewById(R.id.img);
            txtName = (TextView) v.findViewById(R.id.txtName);
            txtBooth = (TextView) v.findViewById(R.id.txtBooth);
            txtDescription = (TextView) v.findViewById(R.id.txtDescription);
            imgFavotite = (ImageView) v.findViewById(R.id.imgFavotite);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rcvProductClick.onItemProductClick(arrayProduct.get(getAdapterPosition()));
                }
            });
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            int count = arrayProduct.size();
            final ArrayList<Product> tempItems = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                Product e = arrayProduct.get(i);
                if (e.getName().toLowerCase().contains(filterString) || e.getBoot_no().toLowerCase().contains(filterString) || e.getDescription().toLowerCase().contains(filterString)) {
                    tempItems.add(arrayProduct.get(i));
                }
            }
            results.values = tempItems;
            results.count = tempItems.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (ArrayList<Product>) results.values;
            notifyDataSetChanged();
        }
    }

    public Filter getFilter() {
        return mFilter;
    }


}
