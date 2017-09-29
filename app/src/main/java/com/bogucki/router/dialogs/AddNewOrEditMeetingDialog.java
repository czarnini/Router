package com.bogucki.router.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bogucki.router.R;
import com.bogucki.router.Utils.ConstantValues;
import com.bogucki.router.models.Meeting;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Michał Bogucki
 */

public class AddNewOrEditMeetingDialog extends DialogFragment {

    private EditText nameTV;
    private EditText addressTV;
    private EditText dateTV;
    private TextView reasonTV;
    private String pushId;
    private int action;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View rootView = inflater.inflate(R.layout.add_or_edit_meeting, null);


        nameTV = (EditText) rootView.findViewById(R.id.client_name);
        addressTV = (EditText) rootView.findViewById(R.id.client_address);
        dateTV = (EditText) rootView.findViewById(R.id.date);
        reasonTV = (EditText) rootView.findViewById(R.id.reason);

        handleArgs();

        builder.setView(rootView);
        builder.setMessage("Nowe spotkanie")
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (action == ConstantValues.EDIT_MEETING_BUNDLE_VALUE) {
                            editMeeting();
                        } else {
                            addNewMeeting();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    private void handleArgs() {
        Bundle args = getArguments();
        action = args.getInt(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY);
        if (action == ConstantValues.EDIT_MEETING_BUNDLE_VALUE) {
            handleEditing(args);
        } else if (ConstantValues.CLIENTS_FIREBASE.equals(args.getString(ConstantValues.FROM_MEETINGS_OR_FROM_CLIENTS_BUNDLE_KEY))) {
            handleNewMeetingFromClient(args);
        } else {
            handleNewMeetingFromMeeting(args);
        }
    }

    private void handleEditing(Bundle args) {
        handleNewMeetingFromMeeting(args);
        handleNewMeetingFromClient(args);
        reasonTV.setText(args.getString(ConstantValues.MEETING_REASON_BUNDLE_KEY));
        pushId = args.getString(ConstantValues.MEETING_ID_BUNDLE_KEY);
    }

    /**
     * Action of adding new meeting was invoked from Clients activity
     *
     * @param args bundle passed to this dialog
     */
    private void handleNewMeetingFromClient(Bundle args) {
        nameTV.setText(args.getString(ConstantValues.CLIENT_NAME_BUNDLE_KEY));
        addressTV.setText(args.getString(ConstantValues.CLIENT_ADDRESS_BUNDLE_KEY));
    }


    /**
     * Action of adding new meeting was invoked from Meetings activity
     *
     * @param args bundle passed to this dialog
     */
    private void handleNewMeetingFromMeeting(Bundle args) {
        dateTV.setText(args.getString(ConstantValues.MEETING_DATE_BUNDLE_KEY));
    }

    private void addNewMeeting() {
        String  client = nameTV.getText().toString(),
                address = addressTV.getText().toString(),
                reason = reasonTV.getText().toString(),
                date = dateTV.getText().toString();

        if (!"".equals(client) && !"".equals(address) && !"".equals(reason) && !"".equals(date)) {
            DatabaseReference meetingReference = FirebaseDatabase.getInstance().getReference()
                    .child(ConstantValues.MEETINGS_FIREBASE)
                    .child(date);
            String pushId = meetingReference.push().getKey();
            meetingReference.child(pushId).setValue(new Meeting(pushId, client, address, reason, "TMP", "TMP2"));
        }
    }

    private void editMeeting() {
    }
}
