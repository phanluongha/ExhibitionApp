package com.example.phanluongha.myfirstapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.adapter.NoteAdapter;
import com.example.phanluongha.myfirstapplication.adapter.ProductAdapter;
import com.example.phanluongha.myfirstapplication.customview.LinedEditText;
import com.example.phanluongha.myfirstapplication.impl.RcvNoteClick;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ListNoteActivity extends AppCompatActivity implements RcvNoteClick {

    private RecyclerView rcvNote;
    private ArrayList<String> arrayNote;
    private NoteAdapter noteAdapter;
    private TextView txtEmpty;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        rcvNote = (RecyclerView) findViewById(R.id.rcvNote);
        arrayNote = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, arrayNote, this);

        rcvNote.setLayoutManager(new LinearLayoutManager(this));
        rcvNote.setAdapter(noteAdapter);
        txtEmpty = (TextView) findViewById(R.id.txtEmpty);
        loadNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_note, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_save:
                Intent note = new Intent(ListNoteActivity.this, NotepadActivity.class);
                startActivityForResult(note, 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        Intent note = new Intent(ListNoteActivity.this, NotepadActivity.class);
        note.putExtra("position", position);
        note.putExtra("note", arrayNote.get(position));
        startActivityForResult(note, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int position = data.getIntExtra("position", -1);
                String result = data.getStringExtra("result");
                if (position == -1) {
                    if (result.length() > 0) {
                        arrayNote.add(result);
                        sharedPreferences.edit().putString("list_note", new Gson().toJson(arrayNote)).commit();
                    }
                } else {
                    if (result.length() > 0)
                        arrayNote.set(position, result);
                    else
                        arrayNote.remove(position);
                    sharedPreferences.edit().putString("list_note", new Gson().toJson(arrayNote)).commit();
                }
                loadNotes();
            }
        }
    }

    private void loadNotes() {
        String list_note = sharedPreferences.getString("list_note", "");
        if (list_note.length() > 0) {
            try {
                JSONArray notes = new JSONArray(list_note);
                arrayNote.clear();
                for (int i = 0; i < notes.length(); i++) {
                    arrayNote.add(notes.getString(i));
                }
                noteAdapter.notifyDataSetChanged();
                if (notes.length() > 0)
                    txtEmpty.setVisibility(View.GONE);
                else
                    txtEmpty.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
