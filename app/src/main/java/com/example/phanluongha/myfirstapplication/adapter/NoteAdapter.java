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
import com.example.phanluongha.myfirstapplication.impl.RcvNoteClick;
import com.example.phanluongha.myfirstapplication.impl.RcvProductClick;
import com.example.phanluongha.myfirstapplication.model.Product;

import java.util.ArrayList;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private Context context;
    private ArrayList<String> arrayNote;
    private RcvNoteClick rcvNoteClick;


    public NoteAdapter(Context context,
                       ArrayList<String> arrayNote, RcvNoteClick rcvNoteClick) {
        this.context = context;
        this.arrayNote = arrayNote;
        this.rcvNoteClick = rcvNoteClick;
    }


    @Override
    public int getItemCount() {
        return arrayNote.size();
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_note, viewGroup, false);
        return new NoteHolder(itemView);
    }


    @Override
    public void onBindViewHolder(NoteHolder holder, final int position) {
        String note = arrayNote.get(position);
        holder.txtDescription.setText(note);
    }

    public class NoteHolder extends RecyclerView.ViewHolder {

        TextView txtDescription;

        public NoteHolder(View v) {
            super(v);
            txtDescription = (TextView) v.findViewById(R.id.txtDescription);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rcvNoteClick.onItemClick(getAdapterPosition());
                }
            });
        }
    }

}
