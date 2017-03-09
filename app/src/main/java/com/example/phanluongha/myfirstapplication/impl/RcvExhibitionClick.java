package com.example.phanluongha.myfirstapplication.impl;

/**
 * Created by ng.hoang.minh095gmail.com on 3/8/17.
 */

public interface RcvExhibitionClick {
    void onItemExhibitionClick(int id, boolean isFavorite);

    void onItemExhibitionClick(int position);

    void keyClickedIndex(int position);
}
