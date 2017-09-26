package com.bogucki.router.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.bogucki.router.R;
import com.bogucki.router.Utils.ConstantValues;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by MichałBogucki
 */

public class YesNoDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirm)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle args = getArguments();
                        String actionType = args.getString(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY);
                        if(ConstantValues.REMOVE_CLIENT_BUNDLE_VALUE.equals(actionType)){
                            String pushId = args.getString(ConstantValues.CLIENT_ID_BUNDLE_KEY);
                            DatabaseReference clientToRemove = null;
                            if (pushId != null) {
                                clientToRemove = FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child(ConstantValues.CLIENTS_FIREBASE)
                                        .child(pushId);

                                clientToRemove.removeValue();
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Just dismiss dialog box
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
