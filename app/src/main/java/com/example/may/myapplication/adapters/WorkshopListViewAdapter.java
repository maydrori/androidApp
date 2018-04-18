package com.example.may.myapplication.adapters;

import android.content.Context;
import android.graphics.Bitmap;
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

        // TODO: here we need to get the images... save locally
        // Try to find good library that do this auto!!!

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
            placeTextView.setText(workshop.getPlace());

            setTeacherInfo(workshop.getTeacherId(), view);
        }

        return view;
    }

    private void setTeacherInfo(String id, final View view) {
        Model.instance().getUserById(id, new ModelFirebase.GetDataListener<User>() {
            @Override
            public void onComplete(final User user) {
                ((TextView) view.findViewById(R.id.teacher)).setText(user.getName());

                final ImageView teacherPhoto = view.findViewById(R.id.teacher_pic);

                ImageHelper.loadImageToView(user.getImageUrl(), user.getId(), teacherPhoto);
            }
        });
    }
}