package com.example.may.myapplication;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.may.myapplication.model.ModelFirebase;
import com.example.may.myapplication.model.Workshop;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {

    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    ListView listViewWorkshops;
    CompactCalendarView compactCalendar;
    TextView calanderDateTextView;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    List<Workshop> currentWorkshop;
    public static List<Workshop> allWorkshop;

    private List<Workshop> generateWorkshops () {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        List<Workshop> workshopList = new ArrayList<>();
        workshopList.add(new Workshop("1", cal.getTime(), "1", "ביסטריט מרכז", "מתקדמים", 30));
        workshopList.add(new Workshop("2", cal.getTime(), "2", "רעים חולון", "מתחילים", 25));
        workshopList.add(new Workshop("3", cal.getTime(), "2", "ביסטריט חדרה", "מתקדמים", 35));
        return workshopList;
    }

    private void setWorkshopsList(List<Event> workshopsByDate) {

        // TODO: query the currentWorkshop for this date
        // TODO: sort them by hour
        currentWorkshop = new ArrayList<Workshop>();
        for (Event event : workshopsByDate) {
            currentWorkshop.add((Workshop)event.getData());
        }

        WorkshopListViewAdapter workshopsAdapter = new WorkshopListViewAdapter(this, R.layout.listview_workshops, currentWorkshop);

        listViewWorkshops.setAdapter(workshopsAdapter);
    }

    private void loadEvents() {

        // TODO- read from database
        allWorkshop = generateWorkshops();

        ModelFirebase m = new ModelFirebase();

        m.getAllWorkshops(new ModelFirebase.GetAllWorkshopsListener() {
            @Override
            public void onComplete(List<Workshop> workshops) {

                List<Event> events = new ArrayList<Event>();

                for (Workshop workshop : allWorkshop) {
                    events.add(new Event(Color.BLUE, workshop.getDate().getTime(), workshop));
                }

                compactCalendar.addEvents(events);
            }
        });
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        calanderDateTextView = findViewById(R.id.text_calender_date);
        listViewWorkshops = (ListView)findViewById(R.id.workshops);
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);

        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);

        calanderDateTextView.setText(dateFormatForMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));

        toolbar.setTitle("Come to dance");
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerEvents() {

        // Listen to calendar events
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> workshopsByDate = compactCalendar.getEvents(dateClicked);
                setWorkshopsList(workshopsByDate);

                if (workshopsByDate.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Events Planned for that day", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                calanderDateTextView.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        // Listen to workshop list events
        listViewWorkshops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Workshop workshop = currentWorkshop.get(position);

                Intent intent = new Intent(view.getContext(), ViewWorkshop.class);
                startActivity(intent);
            }
        });

        // Listen to button event
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                AddWorkshopFragment addWorkshopFragment = new AddWorkshopFragment();
                addWorkshopFragment.show(fm, "Add workshop Fragment");
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        registerEvents();

        // Load all workshop events from DB to calendar
        loadEvents();
    }
}
