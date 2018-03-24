package com.bogucki.router.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.bogucki.router.R;
import com.bogucki.router.models.Client;
import com.bogucki.router.utils.ConstantValues;
import com.bogucki.router.models.Meeting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Michał Bogucki
 */

public class AddNewOrEditMeetingDialog extends DialogFragment {

    private AutoCompleteTextView nameTV;
    private EditText addressTV;
    private EditText dateTV;
    private TextView reasonTV;
    private String pushId;
    private int action;
    private int meetingOrder = -1;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View rootView = inflater.inflate(R.layout.add_or_edit_meeting, null);


        nameTV = rootView.findViewById(R.id.client_name);
        addressTV = rootView.findViewById(R.id.client_address);
        addressTV.setKeyListener(null);
        dateTV = rootView.findViewById(R.id.date);
        reasonTV = (EditText) rootView.findViewById(R.id.reason);

        final ArrayAdapter<Client> clientAutocompleteArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.client_autocomplete);
        DatabaseReference clients = FirebaseDatabase.getInstance().getReference().child(ConstantValues.CLIENTS_FIREBASE);
        clients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d("New meeting", "onDataChange: Adding client");
                    clientAutocompleteArrayAdapter.add(snapshot.getValue(Client.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(this.getClass().getSimpleName(), "onCancelled: Error while fetching cliets data");
            }
        });

        nameTV.setAdapter(clientAutocompleteArrayAdapter);

        nameTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Client selectedClient = (Client) adapterView.getAdapter().getItem(i);
                addressTV.setText(selectedClient.getAddress());
            }
        });




        handleArgs();

        builder.setView(rootView);
        builder.setMessage("Nowe spotkanie")
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String  client = nameTV.getText().toString(),
                                address = addressTV.getText().toString(),
                                reason = reasonTV.getText().toString(),
                                date = dateTV.getText().toString().replaceAll("\\.", "_");
                        Meeting meeting = new Meeting(pushId, client, address, reason, 0, 23, meetingOrder);

                        if (!"".equals(meeting.getAddress()) && !"".equals(meeting.getReason()) && !"".equals(date)) {
                            if (action == ConstantValues.EDIT_MEETING_BUNDLE_VALUE) {
                                editMeeting(meeting, date);
                            } else {
                                addNewMeeting(meeting, date);
                            }
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
        meetingOrder = args.getInt(ConstantValues.MEETING_ORDER);
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
        nameTV.setText(args.getString(ConstantValues.CLIENT_NAME_BUNDLE_KEY), false);
        addressTV.setText(args.getString(ConstantValues.CLIENT_ADDRESS_BUNDLE_KEY));
        reasonTV.setText("Spotkanie");
        dateTV.setText("2.1.2018");

    }


    /**
     * Action of adding new meeting was invoked from Meetings activity
     *
     * @param args bundle passed to this dialog
     */
    private void handleNewMeetingFromMeeting(Bundle args) {
        dateTV.setText(args.getString(ConstantValues.MEETING_DATE_BUNDLE_KEY).replaceAll("_","."));
    }

    private void addNewMeeting(Meeting meeting, String date) {
            DatabaseReference meetingReference = FirebaseDatabase.getInstance().getReference()
                    .child(ConstantValues.MEETINGS_FIREBASE)
                    .child(date);
            String pushId = meetingReference.push().getKey();
            meeting.setPushId(pushId);
            meetingReference.child(pushId).setValue(meeting);
    }

    private void editMeeting(Meeting meeting, String date) {
            DatabaseReference meetingReference = FirebaseDatabase.getInstance().getReference()
                    .child(ConstantValues.MEETINGS_FIREBASE)
                    .child(date)
                    .child(pushId);
            meetingReference.setValue(meeting);
    }
}
