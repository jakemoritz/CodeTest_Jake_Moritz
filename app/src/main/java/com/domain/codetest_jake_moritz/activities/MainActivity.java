package com.domain.codetest_jake_moritz.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.domain.codetest_jake_moritz.R;
import com.domain.codetest_jake_moritz.fragments.PersonFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PersonFragment personFragment = PersonFragment.newInstance(1);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, personFragment)
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}
