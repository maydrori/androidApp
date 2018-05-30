package com.example.may.myapplication.adapters;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.may.myapplication.R;
import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.dal.firebase.ModelFirebase;
import com.example.may.myapplication.utils.ImageHelper;
import com.example.may.myapplication.viewModels.CalendarViewModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by May on 3/31/2018.
 */

public class WorkshopListViewAdapter extends ArrayAdapter<Workshop> {

    private CalendarViewModel viewModel;
    private LifecycleOwner owner;
    private int layoutResource;
    private SimpleDateFormat dateFormatForHour = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public WorkshopListViewAdapter(Context context, LifecycleOwner owner, CalendarViewModel viewModel, int layoutResource, List<Workshop> workshop) {
        super(context, layoutResource, workshop);
        this.layoutResource = layoutResource;
        this.viewModel = viewModel;
        this.owner = owner;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        final Workshop workshop = getItem(position);

        if (workshop != null) {
            TextView hourTextView = (TextView) view.findViewById(R.id.hour);
            TextView teacherTextView = (TextView) view.findViewById(R.id.teacher);
            TextView placeTextView = (TextView) view.findViewById(R.id.place);

            hourTextView.setText(dateFormatForHour.format(workshop.getDate()));
            placeTextView.setText(workshop.getPlace());
            teacherTextView.setText(workshop.getTeacherName());

            if (workshop.getTeacherImageUrl() != null) setTeacherPhoto(workshop, view);
        }

        return view;
    }

    private void setTeacherPhoto(final Workshop workshop, View view) {
        final ImageView teacherPhoto = view.findViewById(R.id.teacher_pic);

        teacherPhoto.setTag(workshop.getTeacherImageUrl());

        viewModel.getImage(workshop.getTeacherImageUrl(), workshop.getTeacherId()).observe(owner, new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable Bitmap bitmap) {
                if (teacherPhoto.getTag().equals(workshop.getTeacherImageUrl())) {
                    teacherPhoto.setImageBitmap(bitmap);
                }
            }
        });
    }
}