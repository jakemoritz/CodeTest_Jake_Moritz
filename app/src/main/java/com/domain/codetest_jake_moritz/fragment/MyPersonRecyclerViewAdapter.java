package com.domain.codetest_jake_moritz.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.domain.codetest_jake_moritz.App;
import com.domain.codetest_jake_moritz.R;
import com.domain.codetest_jake_moritz.activity.MainActivity;
import com.domain.codetest_jake_moritz.model.Person;

import io.realm.RealmRecyclerViewAdapter;

class MyPersonRecyclerViewAdapter extends RealmRecyclerViewAdapter<Person, MyPersonRecyclerViewAdapter.ViewHolder> {

    private MainActivity mainActivity;

    MyPersonRecyclerViewAdapter(MainActivity mainActivity) {
        super(App.getInstance().getRealm().where(Person.class).findAll(), true);
        this.mainActivity = mainActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.person = getItem(position);
        holder.nameView.setText(holder.person.getFirstName() + " " + holder.person.getLastName());
        holder.phoneNumberView.setText(holder.person.getPhoneNumber());
        holder.zipCodeView.setText(holder.person.getZipCode());
        holder.dateOfBirthView.setText(String.valueOf(holder.person.getDateOfBirthFormatted()));

        final String personID = holder.person.getPersonID();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clickIntent = new Intent(mainActivity, MainActivity.class);
                clickIntent.putExtra("personID", personID);
                clickIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mainActivity.startActivity(clickIntent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView nameView;
        final TextView phoneNumberView;
        final TextView zipCodeView;
        final TextView dateOfBirthView;
        Person person;

        ViewHolder(View view) {
            super(view);
            mView = view;
            nameView = (TextView) view.findViewById(R.id.person_name);
            phoneNumberView = (TextView) view.findViewById(R.id.person_phone_number);
            zipCodeView = (TextView) view.findViewById(R.id.person_zip_code);
            dateOfBirthView = (TextView) view.findViewById(R.id.person_date_of_birth);
        }
    }
}
