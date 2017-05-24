package com.domain.codetest_jake_moritz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domain.codetest_jake_moritz.App;
import com.domain.codetest_jake_moritz.R;
import com.domain.codetest_jake_moritz.activities.MainActivity;
import com.domain.codetest_jake_moritz.dialogs.NewPersonDialogFragment;
import com.domain.codetest_jake_moritz.models.Person;

import java.util.UUID;

import io.realm.Realm;

public class PersonFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private MainActivity mainActivity;

    public PersonFragment() {
    }

    @SuppressWarnings("unused")
    public static PersonFragment newInstance(int columnCount) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        generateDummyInfo();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    private void generateDummyInfo() {
        if (App.getInstance().getRealm().where(Person.class).findAll().size() == 0) {
            for (int i = 0; i < 15; i++) {
                App.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Person person = realm.createObject(Person.class, UUID.randomUUID().toString());
                        person.setFirstName("John");
                        person.setLastName("Smith");
                        person.setDateOfBirth(1495577645663L);
                        person.setPhoneNumber("555-555-5555");

                        int zipCode = (int) (Math.random() * 99999 + 10000);
                        person.setZipCode(String.valueOf(zipCode));
                    }
                });
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_list, container, false);

        RecyclerView personRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        FloatingActionButton addNewPersonFab = (FloatingActionButton) view.findViewById(R.id.new_person_fab);
        addNewPersonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPersonDialogFragment.
            }
        });

        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            personRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            personRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        personRecyclerView.setAdapter(new MyPersonRecyclerViewAdapter());

        return view;
    }

    public void showDialog(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment previousFragment = getFragmentManager().findFragmentByTag("dialog");

        if (previousFragment != null){
            fragmentTransaction.remove(previousFragment);
        }

        fragmentTransaction.addToBackStack(null);

        NewPersonDialogFragment newPersonDialogFragment = NewPersonDialogFragment.newInstance();
        newPersonDialogFragment.show(fragmentTransaction, "dialog");
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
