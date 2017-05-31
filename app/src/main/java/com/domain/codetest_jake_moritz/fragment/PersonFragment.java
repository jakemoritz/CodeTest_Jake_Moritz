package com.domain.codetest_jake_moritz.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.domain.codetest_jake_moritz.App;
import com.domain.codetest_jake_moritz.R;
import com.domain.codetest_jake_moritz.activity.MainActivity;
import com.domain.codetest_jake_moritz.model.Person;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class PersonFragment extends Fragment implements MainActivity.OnPersonClickListener, RealmChangeListener<RealmResults<Person>> {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private MainActivity mainActivity;

    private RecyclerView personRecyclerView;
    private LinearLayout emptyView;

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

    private void setVisibility(RealmResults<Person> persons){
        if (persons.isEmpty()){
            personRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            personRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onChange(RealmResults<Person> persons) {
        setVisibility(persons);
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
                        person.setDateOfBirthFormatted("May 31, 2017");
                        person.setPhoneNumber("555-555-5555");

                        int zipCode = (int) (Math.random() * 99999 + 10000);
                        person.setZipCode(String.valueOf(zipCode));
                    }
                });
            }
        }
    }

    @Override
    public void onPersonClicked(String personID) {
        modifyPerson(personID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_list, container, false);

        personRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        emptyView = (LinearLayout) view.findViewById(R.id.empty_view);

        FloatingActionButton addNewPersonFab = (FloatingActionButton) view.findViewById(R.id.new_person_fab);
        final PersonFragment parentFragment = this;
        addNewPersonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentFragment.addPerson();
            }
        });

        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            personRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            personRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        RealmResults<Person> persons = App.getInstance().getRealm().where(Person.class).findAll();
        persons.addChangeListener(this);
        setVisibility(persons);

        personRecyclerView.setAdapter(new MyPersonRecyclerViewAdapter(mainActivity, persons));

        ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setTitle(App.getInstance().getApplicationName());
        actionBar.setDisplayHomeAsUpEnabled(false);

        return view;
    }

    private void addPerson(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        EditPersonFragment editPersonFragment = EditPersonFragment.newInstance();
        fragmentTransaction.replace(R.id.content_main, editPersonFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    private void modifyPerson(String personID){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        EditPersonFragment editPersonFragment = EditPersonFragment.newInstance(personID);
        fragmentTransaction.replace(R.id.content_main, editPersonFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
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
