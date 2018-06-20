package com.example.may.myapplication.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.may.myapplication.R;
import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.repositories.UserRepository;
import com.example.may.myapplication.utils.DateFormatter;

import java.util.Calendar;

/**
 * Created by May on 4/3/2018.
 */

public class AddWorkshopFragment extends DialogFragment {

    Workshop workshop;
    Calendar myCalendar = Calendar.getInstance();
    EditText placeInput;
    EditText maxParticipantsInput;
    EditText dateInput;
    EditText startTimeInput;
    EditText endTimeInput;
    Spinner genreInput;
    Spinner levelInput;

    public static AddWorkshopFragment instance(Workshop workshop) {

        AddWorkshopFragment fragment = new AddWorkshopFragment();

        Bundle args = new Bundle();
        args.putSerializable("workshop", workshop);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.add_workshop);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        workshop = (Workshop)getArguments().getSerializable("workshop");

        initDialogInputs();
    }

    private void handleSave() {

        final Button saveButton = (Button) getDialog().findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If all fields are valid, save the workshop
                if (validate()) saveWorkshop();

            }
        });
    }

    private void saveWorkshop() {
        String genre = genreInput.getSelectedItem().toString();
        String level = levelInput.getSelectedItem().toString();
        String place = placeInput.getText().toString();
        int maxParticipants = Integer.parseInt(maxParticipantsInput.getText().toString());
        long startTime = DateFormatter.fullStringToDate(dateInput.getText().toString() + " " + startTimeInput.getText().toString()).getTime();
        long endTime = DateFormatter.fullStringToDate(dateInput.getText().toString() + " " + endTimeInput.getText().toString()).getTime();

        // If we update the workshop- get its id,
        // else- get new id
        String workshopId = (workshop != null) ? workshop.getId() : Model.instance().getNextWorkshopId();

        Workshop workshopToSave = new Workshop(workshopId,
                startTime,
                endTime,
                UserRepository.getCurrentUserId(),
                place,
                genre,
                level,
                maxParticipants);

        Model.instance().saveWorkshop(workshopToSave);

        // Alert the user
        Toast.makeText(getActivity().getApplicationContext(),
                getResources().getString(R.string.alert_workshopSaved), Toast.LENGTH_SHORT)
                .show();

        // Close the dialog
        getDialog().dismiss();
    }

    private boolean validate() {
        return validateEmptyFields() && validateTimeRange();
    }

    private boolean validateEmptyFields() {

        // Validate empty fields and spinners
        return (validateEmptyTextFields(dateInput, startTimeInput, endTimeInput, placeInput, maxParticipantsInput) &&
            validateEmptySpinner((TextView)genreInput.getSelectedView(), "סגנון") &&
            validateEmptySpinner((TextView)levelInput.getSelectedView(), "רמת קושי"));
    }

    private boolean validateEmptySpinner(TextView input, String defaultVal) {
        if (input.getText().toString().equals(defaultVal)) {
            input.setError("יש למלא שדה זה");
            return false;
        }
        else input.setError(null);

        return true;
    }

    private boolean validateTimeRange() {
        // Convert times from text to long
        long startTime = DateFormatter.fullStringToDate(dateInput.getText().toString() + " " + startTimeInput.getText().toString()).getTime();
        long endTime = DateFormatter.fullStringToDate(dateInput.getText().toString() + " " + endTimeInput.getText().toString()).getTime();

        if (endTime < startTime) {
            endTimeInput.setError(getResources().getString(R.string.form_invalidTimes));
            return false;
        }
        else endTimeInput.setError(null);
        return true;
    }

    private boolean validateEmptyTextFields(TextView... inputs) {
        for (TextView input : inputs) {
            if (input.getText().toString().isEmpty()) {
                input.setError("יש למלא שדה זה");
                return false;
            }
            else input.setError(null);
        }

        return true;
    }

    private void setInputsByWorkshop() {
        placeInput.setText(workshop.getPlace());
        maxParticipantsInput.setText(String.valueOf(workshop.getMaxParticipants()));
        dateInput.setText(DateFormatter.toDateFormat(workshop.getStartTime()));
        startTimeInput.setText(DateFormatter.toTimeFormat(workshop.getStartTime()));
        endTimeInput.setText(DateFormatter.toTimeFormat(workshop.getEndTime()));
        genreInput.setSelection(((ArrayAdapter<CharSequence>)genreInput.getAdapter()).getPosition(workshop.getGenre()));
        levelInput.setSelection(((ArrayAdapter<CharSequence>)levelInput.getAdapter()).getPosition(workshop.getLevel()));
    }

    private void initDialogInputs() {
        placeInput = ((EditText)getDialog().findViewById(R.id.place));
        maxParticipantsInput = (EditText)getDialog().findViewById(R.id.maxParticipants);
        dateInput = (EditText)getDialog().findViewById(R.id.date);
        startTimeInput = (EditText)getDialog().findViewById(R.id.startTime);
        endTimeInput = (EditText)getDialog().findViewById(R.id.endTime);
        genreInput = (Spinner) getDialog().findViewById(R.id.genreSpinner);
        levelInput = (Spinner) getDialog().findViewById(R.id.levelSpinner);

        initSpinner(genreInput, R.array.class_genres);
        initSpinner(levelInput, R.array.class_levels);
        initDatePicker(dateInput);
        initTimePicker(startTimeInput);
        initTimePicker(endTimeInput);

        if (workshop != null) setInputsByWorkshop();

        handleSave();
    }

    private void initTimePicker(final EditText timeInput) {

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int var1, int var2) {
                timeInput.setText(var1 + ":" + var2);
            }
        };

        timeInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(v.getContext(),
                        time,
                        myCalendar.get(Calendar.HOUR),
                        myCalendar.get(Calendar.MINUTE),
                        true).show();
            }
        });
    }

    private void initDatePicker(final EditText dateInput) {

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dateInput.setText(DateFormatter.toDateFormat(year, monthOfYear, dayOfMonth));
            }
        };

        dateInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(),
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void initSpinner(Spinner spinner, int items) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                items, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }
}
