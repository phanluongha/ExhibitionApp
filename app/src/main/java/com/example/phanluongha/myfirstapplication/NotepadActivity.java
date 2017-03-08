package com.example.phanluongha.myfirstapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.customview.LinedEditText;

import es.dmoral.toasty.Toasty;

public class NotepadActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    LinedEditText edNotepad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        edNotepad = (LinedEditText) findViewById(R.id.edNotepad);
        setSupportActionBar(toolbar);
        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = getSharedPreferences("note", MODE_PRIVATE);
        String resultNotepad = sharedPreferences.getString("my_note", "");
        edNotepad.setText(resultNotepad);
        edNotepad.setSelection(resultNotepad.length());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notepad, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_save:
                if (edNotepad.getText().toString().trim().length() > 0) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("my_note", edNotepad.getText().toString());
                    editor.apply();
                    Toasty.success(this, "Save complete", Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.error(this, "Please check content again", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
