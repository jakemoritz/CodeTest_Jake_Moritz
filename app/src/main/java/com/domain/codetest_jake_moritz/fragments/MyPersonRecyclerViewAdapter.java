package com.domain.codetest_jake_moritz.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.domain.codetest_jake_moritz.App;
import com.domain.codetest_jake_moritz.R;
import com.domain.codetest_jake_moritz.fragments.PersonFragment.OnListFragmentInteractionListener;
import com.domain.codetest_jake_moritz.models.Person;

import io.realm.RealmRecyclerViewAdapter;

public class MyPersonRecyclerViewAdapter extends RealmRecyclerViewAdapter<Person, MyPersonRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;

    public MyPersonRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        super(App.getInstance().getRealm().where(Person.class).findAll(), true);
        mListener = listener;
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
        holder.dateOfBirthView.setText(String.valueOf(holder.person.getDateOfBirth()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameView;
        public final TextView phoneNumberView;
        public final TextView zipCodeView;
        public final EditText dateOfBirthView;
        public Person person;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameView = (TextView) view.findViewById(R.id.person_name);
            phoneNumberView = (TextView) view.findViewById(R.id.person_phone_number);
            zipCodeView = (TextView) view.findViewById(R.id.person_zip_code);
            dateOfBirthView = (EditText) view.findViewById(R.id.person_date_of_birth);
        }
    }
}
