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
import com.example.may.myapplication.repositories.UserRepository;
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

        // Set up workshop's info
        if (workshop != null) {
            TextView hourTextView = (TextView) view.findViewById(R.id.hour);
            TextView teacherTextView = (TextView) view.findViewById(R.id.teacher);
            TextView placeTextView = (TextView) view.findViewById(R.id.place);

            boolean isRegistred = workshop.getRegisteredMembers().contains(UserRepository.getCurrentUserId());
            TextView registerIndicationView = (TextView) view.findViewById(R.id.registered_indication);
            registerIndicationView.setVisibility((isRegistred) ? View.VISIBLE : View.INVISIBLE);

            hourTextView.setText(dateFormatForHour.format(workshop.getStartTime()));
            placeTextView.setText(workshop.getPlace());
            teacherTextView.setText(workshop.getTeacherName());

            setTeacherPhoto(workshop, view);
        }

        return view;
    }

    private void setTeacherPhoto(final Workshop workshop, View view) {
        final ImageView teacherPhoto = view.findViewById(R.id.teacher_pic);
        teacherPhoto.setImageResource(R.drawable.ic_person);

        teacherPhoto.setTag(workshop.getTeacherId());

        viewModel.getImage(workshop.getTeacherId()).observe(owner, new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable Bitmap bitmap) {
                if (teacherPhoto.getTag().equals(workshop.getTeacherId())) {
                    if (bitmap != null) teacherPhoto.setImageBitmap(bitmap);
                }
            }
        });
    }
}