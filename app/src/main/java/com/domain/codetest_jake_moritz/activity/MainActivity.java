package com.domain.codetest_jake_moritz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.domain.codetest_jake_moritz.R;
import com.domain.codetest_jake_moritz.fragment.PersonFragment;

public class MainActivity extends AppCompatActivity {

    private OnItemClick onItemClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PersonFragment personFragment = PersonFragment.newInstance(1);
        onItemClick = personFragment;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, personFragment)
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra("personID") && !intent.getStringExtra("personID").isEmpty()){
            String personID = intent.getStringExtra("personID");
            onItemClick.onItemClicked(personID);
        }
    }

    public interface OnItemClick {
        void onItemClicked(String personID);
    }
}
