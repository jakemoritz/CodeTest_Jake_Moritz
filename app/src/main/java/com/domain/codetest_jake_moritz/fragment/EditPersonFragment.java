package com.domain.codetest_jake_moritz.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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

    private View fragmentLayout;
    private TextView dateOfBirthTextView;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView zipCodeTextView;
    private TextView phoneNumberTextView;

    private String personID;
    private long dateOfBirth;
    private String dateOfBirthFormatted;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentLayout = inflater.inflate(R.layout.fragment_edit_person, container, false);

        ActionBar actionBar = mainActivity.getSupportActionBar();
        String actionBarTitle = "Add a person";

        if (mode.matches(MODE_EDIT)) {
            actionBarTitle = "Edit a person";
        }

        actionBar.setTitle(actionBarTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayout dateOfBirthLayout = (LinearLayout) fragmentLayout.findViewById(R.id.new_date_of_birth);
        dateOfBirthTextView = (TextView) fragmentLayout.findViewById(R.id.new_date_of_birth_text);
        firstNameTextView = (EditText) fragmentLayout.findViewById(R.id.new_first_name_text);
        lastNameTextView = (EditText) fragmentLayout.findViewById(R.id.new_last_name_text);
        zipCodeTextView = (EditText) fragmentLayout.findViewById(R.id.new_zip_code_text);
        phoneNumberTextView = (EditText) fragmentLayout.findViewById(R.id.new_phone_number_text);

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

    private void savePerson() {
        final String firstName = firstNameTextView.getText().toString().trim();
        final String lastName = lastNameTextView.getText().toString().trim();
        final String phoneNumber = phoneNumberTextView.getText().toString().trim();
        final String zipCode = zipCodeTextView.getText().toString().trim();

        String errorText = "This field cannot be blank";

        boolean completed = true;

        if (firstName.isEmpty()) {
            firstNameTextView.setError(errorText);
            completed = false;
        }

        if (lastName.isEmpty()) {
            lastNameTextView.setError(errorText);
            completed = false;
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberTextView.setError(errorText);
            completed = false;
        }

        if (zipCode.isEmpty()) {
            zipCodeTextView.setError(errorText);
            completed = false;
        }

        if (dateOfBirthTextView.getText().toString().trim().isEmpty()) {
            dateOfBirthTextView.setError(errorText);
            completed = false;
        }

        if (completed) {
            App.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Person person;

                    if (personID != null && !personID.isEmpty()) {
                        person = new Person(firstName, lastName, phoneNumber, dateOfBirth, dateOfBirthFormatted, zipCode, personID);
                    } else {
                        person = new Person(firstName, lastName, phoneNumber, dateOfBirth, dateOfBirthFormatted, zipCode);
                    }

                    realm.copyToRealmOrUpdate(person);
                }
            });

            mainActivity.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        closeKeyboard();
    }

    private void closeKeyboard(){

//        IBinder focusedWindowToken = mainActivity.getCurrentFocus().getWindowToken();

//        if /(focusedWindowToken != null){
            InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()){
            imm.hideSoftInputFromWindow(firstNameTextView.getWindowToken(), 0);

        }
//        }
    }

    private void deletePerson() {
        App.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Person person = realm.where(Person.class).equalTo("personID", personID).findFirst();
                person.deleteFromRealm();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
