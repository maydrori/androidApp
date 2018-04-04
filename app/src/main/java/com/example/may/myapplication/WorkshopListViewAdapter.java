package com.example.may.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.may.myapplication.model.Member;
import com.example.may.myapplication.model.Workshop;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by May on 3/31/2018.
 */

public class WorkshopListViewAdapter extends ArrayAdapter<Workshop> {

    private int layoutResource;
    private SimpleDateFormat dateFormatForHour = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public WorkshopListViewAdapter(Context context, int layoutResource, List<Workshop> workshop) {
        super(context, layoutResource, workshop);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        Workshop workshop = getItem(position);

        if (workshop != null) {
            TextView hourTextView = (TextView) view.findViewById(R.id.hour);
            TextView teacherTextView = (TextView) view.findViewById(R.id.teacher);
            TextView placeTextView = (TextView) view.findViewById(R.id.place);

            hourTextView.setText(dateFormatForHour.format(workshop.getDate()));
            teacherTextView.setText(getTeacherName(workshop.getTeacherId()));
            placeTextView.setText(workshop.getPlace());
        }

        return view;
    }

    private String getTeacherName(String id) {
        for (Member member : Member.members) {
            if (member.getId().equals(id)) return member.getName();
        }

        return "";
    }
}