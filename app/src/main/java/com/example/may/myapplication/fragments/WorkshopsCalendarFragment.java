package com.example.may.myapplication.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.may.myapplication.R;
import com.example.may.myapplication.ViewWorkshop;
import com.example.may.myapplication.adapters.WorkshopListViewAdapter;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.utils.DateFormatter;
import com.example.may.myapplication.viewModels.CalendarViewModel;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by May on 4/6/2018.
 */

public class WorkshopsCalendarFragment extends Fragment {
    ListView listViewWorkshops;
    CompactCalendarView compactCalendar;
    TextView calanderDateTextView;
    List<Workshop> currentWorkshop;
    Date currentDate = new Date();

    CalendarViewModel viewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
//        viewModel.init();

        viewModel.getWorkshops().observe(this, new Observer<List<Workshop>>() {
            @Override
            public void onChanged(@Nullable List<Workshop> workshops) {
                loadEventsToCalendar(workshops);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.workshops_calendar_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        registerEvents();
    }

    private void refreshWorkshopsList() {

        List<Event> events = compactCalendar.getEvents(currentDate);

        if (events.size() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "No Events Planned for that day", Toast.LENGTH_SHORT).show();
        }

        // TODO: sort them by hour
        currentWorkshop = new ArrayList<Workshop>();
        for (Event event : events) {
            currentWorkshop.add((Workshop) event.getData());
        }

        WorkshopListViewAdapter workshopsAdapter = new WorkshopListViewAdapter(getActivity(), R.layout.listview_workshops, currentWorkshop);

        listViewWorkshops.setAdapter(workshopsAdapter);
    }

    private void loadEventsToCalendar(List<Workshop> workshops) {

        compactCalendar.removeAllEvents();
        List<Event> events = new ArrayList<Event>();
        for (Workshop workshop : workshops) {
            events.add(new Event(Color.BLUE, workshop.getDate(), workshop));
        }
        compactCalendar.addEvents(events);

        refreshWorkshopsList();
    }

    private void initViews() {
        calanderDateTextView = getView().findViewById(R.id.text_calender_date);
        listViewWorkshops = (ListView)getView().findViewById(R.id.workshops);
        compactCalendar = (CompactCalendarView) getView().findViewById(R.id.compactcalendar_view);

        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);

        calanderDateTextView.setText(DateFormatter.toMonthFormat(compactCalendar.getFirstDayOfCurrentMonth()));
    }

    private void registerEvents()  {

        // Listen to calendar events
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                currentDate = dateClicked;
                refreshWorkshopsList();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                currentDate = firstDayOfNewMonth;
                calanderDateTextView.setText(DateFormatter.toMonthFormat(firstDayOfNewMonth));

                refreshWorkshopsList();
            }
        });

        // Listen to workshop list events
        listViewWorkshops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Workshop workshop = currentWorkshop.get(position);

                Intent intent = new Intent(view.getContext(), ViewWorkshop.class)
                        .putExtra("workshopId", workshop.getId());
                startActivity(intent);
            }
        });

        // Listen to button event
        getView().findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                AddWorkshopFragment addWorkshopFragment = new AddWorkshopFragment();
                addWorkshopFragment.show(fm, "Add workshop Fragment");
            }
        });
    }
}
