package com.domain.codetest_jake_moritz.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.domain.codetest_jake_moritz.App;
import com.domain.codetest_jake_moritz.activities.MainActivity;

import io.realm.Realm;

public class NewPersonDialogFragment extends DialogFragment {

    private MainActivity mainActivity;

    public static NewPersonDialogFragment newInstance(MainActivity mainActivity){
        NewPersonDialogFragment newPersonDialogFragment = new NewPersonDialogFragment();
        newPersonDialogFragment.setRetainInstance(true);
        newPersonDialogFragment.mainActivity = mainActivity;
        return newPersonDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Add a new person")
                .setMessage("message")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                // create person
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

}
