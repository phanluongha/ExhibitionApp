/*
 * Copyright 2013 David Schreiber
 *           2013 John Paul Nalog
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fancycoverflow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phanluongha.myfirstapplication.DetailEventActivity;
import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.model.Event;

import java.util.ArrayList;

import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;


public class FancyCoverFlowSampleAdapter extends FancyCoverFlowAdapter {
    private Context context;
    private ArrayList<Event> events;
    private int screenWidth;

    public FancyCoverFlowSampleAdapter(Context context, ArrayList<Event> events, int screenWidth) {
        this.context = context;
        this.events = events;
        this.screenWidth = screenWidth;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Event getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
        final Event e = events.get(i);
        ImageView imageView = null;
        if (reuseableView != null) {
            imageView = (ImageView) reuseableView;
        } else {
            imageView = new ImageView(viewGroup.getContext());
            imageView.setLayoutParams(new FancyCoverFlow.LayoutParams(screenWidth / 2, 2 * screenWidth / 3));
        }
        imageView.setImageResource(R.drawable.placeholder);
        Glide
                .with(context)
                .load(e.getImage())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(imageView);
        return imageView;
    }
}
