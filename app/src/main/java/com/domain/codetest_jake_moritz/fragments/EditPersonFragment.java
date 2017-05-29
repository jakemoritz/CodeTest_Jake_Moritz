package com.domain.codetest_jake_moritz.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.domain.codetest_jake_moritz.R;
import com.domain.codetest_jake_moritz.activities.MainActivity;
import com.domain.codetest_jake_moritz.dialogs.DatePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditPersonFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    public static final String MODE_EDIT = "EDIT";
    public static final String MODE_ADD = "ADD";

    private MainActivity mainActivity;
    private String mode;

    private TextView dateOfBirthTextView;

    public EditPersonFragment() {
        // Required empty public constructor
    }

    public static EditPersonFragment newInstance(String mode) {
        EditPersonFragment fragment = new EditPersonFragment();
        fragment.mode = mode;
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_person, container, false);

        ActionBar actionBar = mainActivity.getSupportActionBar();
        String actionBarTitle = "Add a person";

        if (mode.matches(MODE_EDIT)){
            actionBarTitle = "Edit a person";
        }

        actionBar.setTitle(actionBarTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayout dateOfBirthLayout = (LinearLayout) view.findViewById(R.id.new_date_of_birth);
        dateOfBirthTextView = (TextView) dateOfBirthLayout.findViewById(R.id.new_date_of_birth_text);

        final DatePickerDialog.OnDateSetListener callback = this;
        dateOfBirthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(callback);
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DateFormat birthdayFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

        Calendar birthdayCalendar = Calendar.getInstance();
        birthdayCalendar.set(Calendar.YEAR, year);
        birthdayCalendar.set(Calendar.MONTH, month);
        birthdayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String dateOfBirthFormatted = birthdayFormat.format(birthdayCalendar.getTime());

        dateOfBirthTextView.setText(dateOfBirthFormatted);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }
}
