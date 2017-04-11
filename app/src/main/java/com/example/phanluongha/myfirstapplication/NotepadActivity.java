package com.example.phanluongha.myfirstapplication;

import android.app.Activity;
import android.content.Intent;
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
    LinedEditText edNotepad;
    private int position = -1;
    private String note = "";

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
        Bundle b = getIntent().getExtras();
        if (b != null) {
            position = b.getInt("position");
            note = b.getString("note");
            edNotepad.setText(note);
            edNotepad.setSelection(note.length());
        }
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
                if (edNotepad.getText().toString().length() > 0) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("position", position);
                    returnIntent.putExtra("result", edNotepad.getText().toString());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toasty.error(this, getString(R.string.save_not_completed), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.action_delete:
                if (position == -1)
                    onBackPressed();
                else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("position", position);
                    returnIntent.putExtra("result", "");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
