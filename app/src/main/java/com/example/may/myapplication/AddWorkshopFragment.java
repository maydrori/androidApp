package com.example.may.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.may.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by May on 4/3/2018.
 */

public class AddWorkshopFragment extends DialogFragment {

    Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    EditText dateInput;
    EditText timeInput;
//    Dialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.add_workshop);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialogInputs();
    }

    private void initDialogInputs() {
        initSpinner(R.id.genreSpinner, R.array.class_genres);
        initSpinner(R.id.levelSpinner, R.array.class_levels);

        initDatePicker();
        initTimePicker();

        Button saveButton = (Button) getDialog().findViewById(R.id.btnSave);
        // if button is clicked, close the custom dialog
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = dateInput.getText().toString();
                String time = timeInput.getText().toString();
                Spinner genreSpinner = (Spinner)  getDialog().findViewById(R.id.genreSpinner);
                String genre = genreSpinner.getSelectedItem().toString();
                Spinner levelSpinner = (Spinner)  getDialog().findViewById(R.id.levelSpinner);
                String level = levelSpinner.getSelectedItem().toString();
                String place = ((EditText)getDialog().findViewById(R.id.place)).getText().toString();
                int maxParticipants = Integer.parseInt(((EditText)getDialog().findViewById(R.id.maxParticipants)).getText().toString());

                getDialog().dismiss();
            }
        });
    }

    private void initTimePicker() {

        timeInput = (EditText)  getDialog().findViewById(R.id.time);
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

    private void initDatePicker() {

        dateInput = (EditText) getDialog().findViewById(R.id.date);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dateInput.setText(dateFormat.format(new Date(year, monthOfYear, dayOfMonth)));
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

    private void initSpinner(int spinnerId, int items) {
        Spinner spinner = (Spinner) getDialog().findViewById(spinnerId);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                items, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

//                if(spinner.getSelectedItem() == "This is Hint Text")
//                {
//
//                    //Do nothing.
//                }
//                else{
//
//                    Toast.makeText(MainActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
//
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

    }
}
