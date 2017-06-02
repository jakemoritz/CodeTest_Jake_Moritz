package com.domain.codetest_jake_moritz.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.domain.codetest_jake_moritz.App;
import com.domain.codetest_jake_moritz.R;
import com.domain.codetest_jake_moritz.activity.MainActivity;
import com.domain.codetest_jake_moritz.dialog.DatePickerFragment;
import com.domain.codetest_jake_moritz.model.Person;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;

public class EditPersonFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    public static final String MODE_EDIT = "EDIT";
    public static final String MODE_ADD = "ADD";

    private MainActivity mainActivity;
    private String mode;

    private TextView dateOfBirthTextView;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText zipCodeEditText;
    private EditText phoneNumberEditText;

    private String personID;
    private long dateOfBirth;
    private String dateOfBirthFormatted;

    private String phoneNumberLastChar = " ";

    public EditPersonFragment() {
        // Required empty public constructor
    }

    public static EditPersonFragment newInstance() {
        EditPersonFragment fragment = new EditPersonFragment();
        fragment.mode = MODE_ADD;
        fragment.setRetainInstance(true);
        fragment.setHasOptionsMenu(true);
        return fragment;
    }

    public static EditPersonFragment newInstance(String personID) {
        EditPersonFragment fragment = new EditPersonFragment();
        fragment.mode = MODE_EDIT;
        fragment.setRetainInstance(true);
        fragment.setHasOptionsMenu(true);
        fragment.personID = personID;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_edit_person, container, false);

        // Set ActionBar title
        ActionBar actionBar = mainActivity.getSupportActionBar();
        String actionBarTitle = "Add a person";

        if (mode.matches(MODE_EDIT)) {
            actionBarTitle = "Edit a person";
        }

        actionBar.setTitle(actionBarTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Instantiate child views from fragment layout
        LinearLayout dateOfBirthLayout = (LinearLayout) fragmentLayout.findViewById(R.id.new_date_of_birth);
        dateOfBirthTextView = (TextView) fragmentLayout.findViewById(R.id.new_date_of_birth_text);
        firstNameEditText = (EditText) fragmentLayout.findViewById(R.id.new_first_name_text);
        lastNameEditText = (EditText) fragmentLayout.findViewById(R.id.new_last_name_text);
        zipCodeEditText = (EditText) fragmentLayout.findViewById(R.id.new_zip_code_text);
        phoneNumberEditText = (EditText) fragmentLayout.findViewById(R.id.new_phone_number_text);

        // Auto-formats the phone number text with hyphens (-)
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = phoneNumberEditText.getText().toString().length();
                if (digits > 1) {
                    phoneNumberLastChar = phoneNumberEditText.getText().toString().substring(digits - 1);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = phoneNumberEditText.getText().toString().length();
                if (!phoneNumberLastChar.equals("-")) {
                    if (digits == 3 || digits == 7) {
                        phoneNumberEditText.append("-");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Open DatePickerDialog when date of birth view clicked
        final DatePickerDialog.OnDateSetListener callback = this;
        dateOfBirthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(callback);
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        return fragmentLayout;
    }

    private void populateFields() {
        if (mode.matches(MODE_EDIT)){
            // Populate text fields if Person exists
            Person person = App.getInstance().getRealm().where(Person.class).equalTo("personID", personID).findFirst();

            firstNameEditText.setText(person.getFirstName());
            lastNameEditText.setText(person.getLastName());
            zipCodeEditText.setText(person.getZipCode());
            phoneNumberEditText.setText(person.getPhoneNumber());
            dateOfBirthTextView.setText(person.getDateOfBirthFormatted());

            dateOfBirth = person.getDateOfBirth();
            dateOfBirthFormatted = person.getDateOfBirthFormatted();
        } else {
            // Restore date of birth TextView values on rotation
            if (dateOfBirth != 0L && dateOfBirthFormatted != null){
                dateOfBirthTextView.setText(dateOfBirthFormatted);
            }
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populateFields();
    }

    private void savePerson() {
        final String firstName = firstNameEditText.getText().toString().trim();
        final String lastName = lastNameEditText.getText().toString().trim();
        final String phoneNumber = phoneNumberEditText.getText().toString().trim();
        final String zipCode = zipCodeEditText.getText().toString().trim();

        // Display errors on fields that are empty
        String errorText = "This field cannot be blank";

        boolean completed = true;

        if (firstName.isEmpty()) {
            firstNameEditText.setError(errorText);
            completed = false;
        }

        if (lastName.isEmpty()) {
            lastNameEditText.setError(errorText);
            completed = false;
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberEditText.setError(errorText);
            completed = false;
        }

        if (zipCode.isEmpty()) {
            zipCodeEditText.setError(errorText);
            completed = false;
        }

        if (dateOfBirthTextView.getText().toString().trim().isEmpty()) {
            dateOfBirthTextView.setError(errorText);
            completed = false;
        }

        // Save Person info if all fields are filled
        if (completed) {
            App.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Person person;

                    if (personID != null && !personID.isEmpty()) {
                        // Save pre-existing Person
                        person = new Person(firstName, lastName, phoneNumber, dateOfBirth, dateOfBirthFormatted, zipCode, personID);
                    } else {
                        // Save new Person
                        person = new Person(firstName, lastName, phoneNumber, dateOfBirth, dateOfBirthFormatted, zipCode);
                    }

                    realm.copyToRealmOrUpdate(person);
                }
            });

            // Return to MainActivity after save
            mainActivity.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Hide soft keyboard when returning to MainActivity
        closeKeyboard();
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(firstNameEditText.getWindowToken(), 0);

        }
    }

    private void deletePerson() {
        App.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Person person = realm.where(Person.class).equalTo("personID", personID).findFirst();
                person.deleteFromRealm();
            }
        });

        mainActivity.onBackPressed();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Displays menu with "Delete" action if editing pre-existing Person
        if (mode.matches(EditPersonFragment.MODE_EDIT)) {
            inflater.inflate(R.menu.edit_person_menu, menu);
        } else {
            inflater.inflate(R.menu.new_person_menu, menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_person:
                savePerson();
                return true;
            case R.id.delete_person:
                deletePerson();
                return true;
            case android.R.id.home:
                mainActivity.onBackPressed();
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Saves date of birth from DatePickerDialog
        DateFormat birthdayFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

        Calendar birthdayCalendar = Calendar.getInstance();
        birthdayCalendar.set(Calendar.YEAR, year);
        birthdayCalendar.set(Calendar.MONTH, month);
        birthdayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        dateOfBirth = birthdayCalendar.getTimeInMillis();
        dateOfBirthFormatted = birthdayFormat.format(birthdayCalendar.getTime());

        dateOfBirthTextView.setText(dateOfBirthFormatted);
        dateOfBirthTextView.setError(null);
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
