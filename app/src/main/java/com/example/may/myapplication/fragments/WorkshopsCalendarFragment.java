package com.example.may.myapplication.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.may.myapplication.R;
import com.example.may.myapplication.activities.ViewWorkshop;
import com.example.may.myapplication.adapters.WorkshopListViewAdapter;
import com.example.may.myapplication.dal.room.daos.WorkshopDao;
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
    List<Workshop> workshopListAdapterData;
    WorkshopListViewAdapter workshopsAdapter;
    Date currentDate;

    CalendarViewModel viewModel;
    ProgressBar progressBarFinishLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         return inflater.inflate(R.layout.workshops_calendar_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBarFinishLoading = getView().findViewById(R.id.progressBarFinishLoading);
        progressBarFinishLoading.setVisibility(View.VISIBLE);

        currentDate = new Date();
        workshopListAdapterData = new ArrayList<>();

        viewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);

        viewModel.getWorkshopsForCalendar().observe(this, new Observer<List<WorkshopDao.WorkshopMini>>() {
            @Override
            public void onChanged(@Nullable List<WorkshopDao.WorkshopMini> workshops) {
                progressBarFinishLoading.setVisibility(View.INVISIBLE);
                loadEventsToCalendar(workshops);
            }
        });

        initViews();
        registerEvents();
    }

    private void refreshWorkshopsList() {

        List<Event> events = compactCalendar.getEvents(currentDate);

        if (events.size() == 0) {
            refreshListNoWorkshops();
        }
        else refershListWithWorkshops(events);
    }

    private void refreshListNoWorkshops () {

        // Alert the user
        String noWorkshopsMsg = this.getResources().getString(R.string.noWorkshopsForDate);
        Toast.makeText(getActivity().getApplicationContext(), noWorkshopsMsg, Toast.LENGTH_SHORT).show();

        workshopListAdapterData.clear();
        workshopsAdapter.notifyDataSetChanged();
    }

    private void refershListWithWorkshops (List<Event> events) {

        // Get the workshops ids
        List<String> wokshopsIds = new ArrayList<>();
        for (Event event : events) {
            wokshopsIds.add((String) event.getData());
        }

        viewModel.getWorkshopsByIds(wokshopsIds).observe(this, new Observer<List<Workshop>>() {
            @Override
            public void onChanged(@Nullable List<Workshop> workshops) {

            workshopListAdapterData.clear();
            workshopListAdapterData.addAll(workshops);
            workshopsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadEventsToCalendar(List<WorkshopDao.WorkshopMini> workshops) {

        compactCalendar.removeAllEvents();
        List<Event> events = new ArrayList<Event>();
        for (WorkshopDao.WorkshopMini workshop : workshops) {
            events.add(new Event(Color.BLUE, workshop.getStartTime(), workshop.getId()));
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

        workshopsAdapter = new WorkshopListViewAdapter(getActivity(),this, viewModel, R.layout.listview_workshops, workshopListAdapterData);
        listViewWorkshops.setAdapter(workshopsAdapter);
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

        // Open the viewWorkshop fragment when clicking on item
        listViewWorkshops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Workshop workshop = workshopListAdapterData.get(position);

                Intent intent = new Intent(getContext(), ViewWorkshop.class)
                        .putExtra("workshopId", workshop.getId());
                startActivity(intent);
            }
        });

        // Listen to button event
        getView().findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddWorkshopFragment addWorkshopFragment = AddWorkshopFragment.instance(null);
                addWorkshopFragment.show(getFragmentManager(), "Add workshop Fragment");
            }
        });
    }
}
