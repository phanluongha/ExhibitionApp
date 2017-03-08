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
    DateFormat dateFormatForMonth = new SimpleDateFormat("dd/MM/yyyy");
    private CompactCalendarView compactCalendarView;
    private Date currentDay = new Date();


    private List<Event> getEvents() {
        return Arrays.asList(new Event(Color.argb(255, 169, 68, 65), 1488692537390L)
                , new Event(Color.argb(255, 169, 68, 65), 1489642535000L));
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
        tvDate.setText(dateFormatForMonth.format(currentDay.getTime()));

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                tvDate.setText(dateFormatForMonth.format(dateClicked));
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                if (currentDay.getMonth() != firstDayOfNewMonth.getMonth() || currentDay.getYear() != firstDayOfNewMonth.getYear())
                    tvDate.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                else
                    tvDate.setText(dateFormatForMonth.format(currentDay));
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
