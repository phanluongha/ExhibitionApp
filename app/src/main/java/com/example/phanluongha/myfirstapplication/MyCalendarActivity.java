package com.example.phanluongha.myfirstapplication;

import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MyCalendarActivity extends AppCompatActivity {

    TextView tvDate;
    DateFormat dateFormatForMonth = new SimpleDateFormat("MM/dd/yyyy");
    private CompactCalendarView compactCalendarView;
    private java.util.Calendar currentCalender = java.util.Calendar.getInstance(Locale.getDefault());




    private List<Event> getEvents() {
        return Arrays.asList(new Event(Color.argb(255, 169, 68, 65), 1488692537390L, "Event 1" + new Date(1488692537390L))
                ,new Event(Color.argb(255, 169, 68, 65), 1489642535000L, "Event 1" + new Date(1489642535000L)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        tvDate = (TextView) findViewById(R.id.tvDate);

        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        tvDate.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                tvDate.setText(dateFormatForMonth.format(dateClicked));
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                tvDate.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });



        List<Event> events = getEvents();
        compactCalendarView.addEvents(events);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
