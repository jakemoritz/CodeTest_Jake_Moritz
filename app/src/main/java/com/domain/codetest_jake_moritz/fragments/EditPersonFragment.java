package com.domain.codetest_jake_moritz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domain.codetest_jake_moritz.R;

public class EditPersonFragment extends Fragment {

    public EditPersonFragment() {
        // Required empty public constructor
    }

    public static EditPersonFragment newInstance() {
        EditPersonFragment fragment = new EditPersonFragment();
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
        return view;
    }

}
