package com.bogucki.router.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.bogucki.router.R;
import com.bogucki.router.adapters.MeetingsAdapter;
import com.bogucki.router.utils.ConstantValues;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by MichałBogucki
 */

public class ConfirmRemovalDialog extends DialogFragment {

    private  MeetingsAdapter meetingsAdapter;
    private Bundle args;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirm)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        args = getArguments();
                        int actionType = args.getInt(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY);
                        if (ConstantValues.REMOVE_CLIENT_BUNDLE_VALUE == actionType) {
                            deleteClient();
                        } else if (ConstantValues.REMOVE_MEETING_BUNDLE_VALUE == args.getInt(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY)) {
                            deleteMeeting();
                        }
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        meetingsAdapter.notifyDataSetChanged();
                        // Just dismiss dialog box
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    public void setAdapter(MeetingsAdapter meetingsAdapter){
        this.meetingsAdapter = meetingsAdapter;
    }

    private void deleteClient() {
        String pushId = args.getString(ConstantValues.CLIENT_ID_BUNDLE_KEY);
        if (pushId != null) {
            DatabaseReference clientToRemove = FirebaseDatabase.getInstance()
                    .getReference()
                    .child(ConstantValues.CLIENTS_FIREBASE)
                    .child(pushId);

            clientToRemove.removeValue();
        }
    }


    private void deleteMeeting() {

        String pushId = args.getString(ConstantValues.MEETING_ID_BUNDLE_KEY);
        String date   = args.getString(ConstantValues.MEETING_DATE_BUNDLE_KEY);
        if(null != pushId && null != date){
            DatabaseReference meetingToRemove = FirebaseDatabase.getInstance()
                    .getReference()
                    .child(ConstantValues.MEETINGS_FIREBASE)
                    .child(date)
                    .child(pushId);

            meetingToRemove.removeValue();
        }
    }
}
