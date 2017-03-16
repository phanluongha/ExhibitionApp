package com.example.phanluongha.myfirstapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.impl.RcvExhibitionClick;
import com.example.phanluongha.myfirstapplication.model.Exhibition;

import java.util.ArrayList;


public class ExhibitionFavouriteAdapter extends RecyclerView.Adapter<ExhibitionFavouriteAdapter.ExhibitionHolder> {
    private Context context;
    private ArrayList<Exhibition> arrayExhibition;
    private RcvExhibitionClick rcvExhibitionClick;

    public ExhibitionFavouriteAdapter(Context context,
                                      ArrayList<Exhibition> arrayExhibition, RcvExhibitionClick rcvExhibitionClick) {
        this.context = context;
        this.arrayExhibition = arrayExhibition;
        this.rcvExhibitionClick = rcvExhibitionClick;
    }


    @Override
    public int getItemCount() {
        return arrayExhibition.size();
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
        Exhibition ex = arrayExhibition.get(position);


        Glide
                .with(context)
                .load(ex.getImage())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(holder.img);

        holder.txtName.setText(ex.getName());
        holder.txtBooth.setText(ex.getBoot_no());
        holder.txtDescription.setText(ex.getAddress());
        if (ex.isFavorite()) {
            holder.imgFavotite.setImageResource(R.drawable.love_fill);
        } else {
            holder.imgFavotite.setImageResource(R.drawable.love_empty);
        }
        holder.imgFavotite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcvExhibitionClick.keyClickedIndex(position);
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
                    rcvExhibitionClick.onItemExhibitionClick(getAdapterPosition());
                }
            });

        }
    }


}
