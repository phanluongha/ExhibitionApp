package com.example.phanluongha.myfirstapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.impl.RcvPlaceClick;
import com.example.phanluongha.myfirstapplication.impl.RcvProductClick;
import com.example.phanluongha.myfirstapplication.model.MapNode;
import com.example.phanluongha.myfirstapplication.model.Product;

import java.util.ArrayList;


public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceHolder> {
    private Context context;
    private ArrayList<MapNode> arrayPlace;
    private ArrayList<MapNode> filteredItems;
    private RcvPlaceClick rcvProductClick;
    private PlaceAdapter.ItemFilter mFilter = new PlaceAdapter.ItemFilter();


    public PlaceAdapter(Context context,
                        ArrayList<MapNode> arrayPlace, RcvPlaceClick rcvProductClick) {

        this.context = context;
        this.arrayPlace = arrayPlace;
        this.rcvProductClick = rcvProductClick;
        this.filteredItems = arrayPlace;
    }


    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    @Override
    public PlaceHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_place, viewGroup, false);

        return new PlaceHolder(itemView);
    }


    @Override
    public void onBindViewHolder(PlaceHolder holder, final int position) {
        MapNode ex = filteredItems.get(position);
        holder.txtName.setText(ex.getName());
        holder.txtBooth.setText(ex.getBooth());
    }

    public class PlaceHolder extends RecyclerView.ViewHolder {

        protected ImageView img;
        protected TextView txtName;
        TextView txtBooth;
        TextView txtDescription;
        ImageView imgFavotite;

        public PlaceHolder(View v) {
            super(v);
            img = (ImageView) v.findViewById(R.id.img);
            txtName = (TextView) v.findViewById(R.id.txtName);
            txtBooth = (TextView) v.findViewById(R.id.txtBooth);
            txtDescription = (TextView) v.findViewById(R.id.txtDescription);
            imgFavotite = (ImageView) v.findViewById(R.id.imgFavotite);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rcvProductClick.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            int count = arrayPlace.size();
            final ArrayList<MapNode> tempItems = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                MapNode e = arrayPlace.get(i);
                if (e.getName().toLowerCase().contains(filterString) || e.getBooth().toLowerCase().contains(filterString)) {
                    tempItems.add(arrayPlace.get(i));
                }
            }
            results.values = tempItems;
            results.count = tempItems.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (ArrayList<MapNode>) results.values;
            notifyDataSetChanged();
        }
    }

    public Filter getFilter() {
        return mFilter;
    }


}
