package com.domain.codetest_jake_moritz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.domain.codetest_jake_moritz.R;
import com.domain.codetest_jake_moritz.fragment.PersonListFragment;

public class MainActivity extends AppCompatActivity {

    private OnPersonClickListener onPersonClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            // Open default fragment
            PersonListFragment personListFragment = PersonListFragment.newInstance(1);
            onPersonClickListener = personListFragment;

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, personListFragment)
                    .commit();
        } else {
            // Restore OnPersonClickListener on rotation
            onPersonClickListener = (OnPersonClickListener) getSupportFragmentManager().getFragment(savedInstanceState, "onPersonClickListener");
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save OnPersonClickListener on rotation
        getSupportFragmentManager().putFragment(outState, "onPersonClickListener", (PersonListFragment) onPersonClickListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Intent received when Person list item clicked, pass Person ID on to listener
        if (intent.hasExtra("personID") && !intent.getStringExtra("personID").isEmpty()){
            String personID = intent.getStringExtra("personID");
            onPersonClickListener.onPersonClicked(personID);
        }
    }

    // Interface for Person list item callback
    public interface OnPersonClickListener {
        void onPersonClicked(String personID);
    }
}
