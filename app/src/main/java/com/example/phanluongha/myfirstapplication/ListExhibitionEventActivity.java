package com.example.phanluongha.myfirstapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.phanluongha.myfirstapplication.model.Exhibition;

import java.util.ArrayList;

public class ListExhibitionEventActivity extends AppCompatActivity {

//    private ListView listView;
//    private ArrayList<Exhibition> arrayFaceFollowers;
//    private ListContactAdapter listFaceFollowersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_exhibition_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
