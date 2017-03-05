package com.example.phanluongha.myfirstapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.model.Exhibition;

import java.util.ArrayList;


public class ListExhibitionAdapter extends ArrayAdapter<Exhibition> {

    private Context context;
    private int textViewResourceId;
    private ArrayList<Exhibition> arrayExhibition;
    private ArrayList<Exhibition> filteredItems;
    private ItemFilter mFilter = new ItemFilter();

    public ListExhibitionAdapter(Context context, int textViewResourceId,
                                 ArrayList<Exhibition> arrayExhibition) {
        super(context, textViewResourceId, arrayExhibition);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.arrayExhibition = arrayExhibition;
        this.filteredItems = arrayExhibition;
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Override
    public Exhibition getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(textViewResourceId, parent, false);
        final Exhibition ex = filteredItems.get(position);
        if (ex != null) {
            ImageView img = (ImageView) v.findViewById(R.id.img);
            Glide
                    .with(context)
                    .load(ex.getImage())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(img);
            TextView txtName = (TextView) v.findViewById(R.id.txtName);
            txtName.setText(ex.getName());
            TextView txtBooth = (TextView) v.findViewById(R.id.txtBooth);
            txtBooth.setText(ex.getBoot_no());
            TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);
            txtDescription.setText(ex.getDescription());
            ImageView imgFavotite = (ImageView) v.findViewById(R.id.imgFavotite);
            if (ex.isFavorite()) {
                imgFavotite.setImageResource(R.drawable.love_fill);
            } else {
                imgFavotite.setImageResource(R.drawable.love_empty);
            }


        }
        return v;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            int count = arrayExhibition.size();
            final ArrayList<Exhibition> tempItems = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                Exhibition e = arrayExhibition.get(i);
                if (e.getName().toLowerCase().contains(filterString) || e.getBoot_no().toLowerCase().contains(filterString) || e.getDescription().toLowerCase().contains(filterString)) {
                    tempItems.add(arrayExhibition.get(i));
                }
            }
            results.values = tempItems;
            results.count = tempItems.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (ArrayList<Exhibition>) results.values;
            notifyDataSetChanged();
        }
    }

    public Filter getFilter() {
        return mFilter;
    }

}
