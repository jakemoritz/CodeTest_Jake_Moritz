package com.domain.codetest_jake_moritz.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domain.codetest_jake_moritz.R;
import com.domain.codetest_jake_moritz.activities.MainActivity;

public class EditPersonFragment extends Fragment {

    public static final String MODE_EDIT = "EDIT";
    public static final String MODE_ADD = "ADD";

    private MainActivity mainActivity;
    private String mode;

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

        return view;
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
