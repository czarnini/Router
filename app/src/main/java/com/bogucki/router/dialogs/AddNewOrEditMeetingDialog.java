package com.bogucki.router.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bogucki.router.R;
import com.bogucki.router.models.Client;
import com.bogucki.router.models.Meeting;
import com.bogucki.router.utils.ConstantValues;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * Created by Michał Bogucki
 */

public class AddNewOrEditMeetingDialog extends DialogFragment {

    private AutoCompleteTextView nameTV;
    private EditText addressTV;
    private EditText dateTV;
    private TextView reasonTV;
    private EditText etpTV;
    private EditText ltpTV;
    private CrystalRangeSeekbar timeWindowSeekBar;

    private String pushId;
    private int action;
    private int meetingOrder = -1;
    private int planedTimeOfVisit = 0;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View rootView = inflater.inflate(R.layout.add_or_edit_meeting, null);


        nameTV = rootView.findViewById(R.id.client_name);
        addressTV = rootView.findViewById(R.id.client_address);
        addressTV.setKeyListener(null);
        dateTV = rootView.findViewById(R.id.date);
        reasonTV = (EditText) rootView.findViewById(R.id.reason);
        etpTV = rootView.findViewById(R.id.earliest_hour);
        ltpTV = rootView.findViewById(R.id.latest_hour);
        timeWindowSeekBar = rootView.findViewById(R.id.timeWindowsSeekBar);


        final ArrayAdapter<Client> clientAutocompleteArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.client_autocomplete);
        DatabaseReference clients = FirebaseDatabase.getInstance().getReference().child(ConstantValues.CLIENTS_FIREBASE);
        clients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    clientAutocompleteArrayAdapter.add(snapshot.getValue(Client.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(this.getClass().getSimpleName(), "onCancelled: Error while fetching cliets data" + databaseError.getDetails());
            }
        });


        nameTV.setAdapter(clientAutocompleteArrayAdapter);

        nameTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Client selectedClient = (Client) adapterView.getAdapter().getItem(i);
                addressTV.setText(selectedClient.getAddress());
                reasonTV.requestFocus();
            }
        });



        handleArgs();

        builder.setView(rootView);
        builder.setMessage(action == ConstantValues.EDIT_MEETING_BUNDLE_VALUE ? "Edycja spotkania" : "Nowe spotkanie")
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null);

        final Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String client = nameTV.getText().toString(),
                                address = addressTV.getText().toString(),
                                reason = reasonTV.getText().toString(),
                                date = dateTV.getText().toString().replaceAll("\\.", "_");
                        Calendar calendar = Calendar.getInstance();

                        String[] dateSplit = dateTV.getText().toString().split("\\.");
                        String[] etpSplit = etpTV.getText().toString().split(":");
                        String[] ltpSplit = ltpTV.getText().toString().split(":");

                        calendar.set(Integer.parseInt(dateSplit[2]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[0]), Integer.parseInt(etpSplit[0]), Integer.parseInt(etpSplit[1]));
                        long earliestTimePossible = calendar.getTimeInMillis();

                        calendar.set(Integer.parseInt(dateSplit[2]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[0]), Integer.parseInt(ltpSplit[0]), Integer.parseInt(ltpSplit[1]));
                        long latestTimePossible = calendar.getTimeInMillis();


                        Meeting meeting = new Meeting(pushId, client, address, reason, earliestTimePossible, latestTimePossible, meetingOrder, planedTimeOfVisit);

                        if (!isClientInList(client, address, clientAutocompleteArrayAdapter) || "".equals(meeting.getReason()) && !"".equals(date)) {
                            if (clientAutocompleteArrayAdapter.getPosition(new Client(client, address)) == -1) {
                                handleClientColor(getResources().getColor(R.color.colorMistake));
                            } else {
                                handleClientColor(getResources().getColor(R.color.colorAccent));
                            }
                            if ("".equals(meeting.getReason()) && !"".equals(date)) {
                                handleReasonColor(getResources().getColor(R.color.colorMistake));
                            } else {
                                handleReasonColor(getResources().getColor(R.color.colorAccent));
                            }
                        } else {
                            date = date.replaceAll("\\.", "_");
                            if (action == ConstantValues.EDIT_MEETING_BUNDLE_VALUE) {
                                editMeeting(meeting, date);
                            } else {
                                addNewMeeting(meeting, date);
                            }
                            dialog.dismiss();
                        }

                    }
                });
            }
        });
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment fragment = new DatePickerFragment();
                fragment.setTime(dateTV);
                fragment.show(getActivity().getSupportFragmentManager(), "NewMeeting");
            }
        });

        timeWindowSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                etpTV.setText(convertIntToHHMMString( minValue.intValue()));
                ltpTV.setText(convertIntToHHMMString( maxValue.intValue()));
            }

            private String convertIntToHHMMString(int intervalValue) {
                int m = 15 * (intervalValue % 4);
                int h = (intervalValue - intervalValue % 4) / 4 + 6;
                return String.format("%02d", h) + ":" + String.format("%02d",m);
            }
        });

        return dialog;
    }

    private boolean isClientInList(String client, String address, ArrayAdapter<Client> clientAutocompleteArrayAdapter) {
        return clientAutocompleteArrayAdapter.getPosition(new Client(client, address)) != -1;
    }

    private void handleReasonColor(int color) {
        ViewCompat.setBackgroundTintList(reasonTV, ColorStateList.valueOf(color));

    }

    private void handleClientColor(int color) {
        Log.d("handle", "handleClientColor: " + color);
        ViewCompat.setBackgroundTintList(nameTV, ColorStateList.valueOf(color));
        ViewCompat.setBackgroundTintList(addressTV, ColorStateList.valueOf(color));
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

        planedTimeOfVisit = args.getInt(ConstantValues.PLANED_TIME_OF_VISIT);
        meetingOrder = args.getInt(ConstantValues.MEETING_ORDER);
        handleNewMeetingFromMeeting(args);
        handleNewMeetingFromClient(args);
        reasonTV.setText(args.getString(ConstantValues.MEETING_REASON_BUNDLE_KEY));
        pushId = args.getString(ConstantValues.MEETING_ID_BUNDLE_KEY);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(args.getLong(ConstantValues.EARLIEST_TIME_BUNDLE_KEY));
        etpTV.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        calendar.setTimeInMillis(args.getLong(ConstantValues.LATEST_TIME_BUNDLE_KEY));
        ltpTV.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
    }

    /**
     * Action of adding new meeting was invoked from Clients activity
     *
     * @param args bundle passed to this dialog
     */
    private void handleNewMeetingFromClient(Bundle args) {
        nameTV.setText(args.getString(ConstantValues.CLIENT_NAME_BUNDLE_KEY), false);
        addressTV.setText(args.getString(ConstantValues.CLIENT_ADDRESS_BUNDLE_KEY));
        reasonTV.requestFocus();
        Calendar c = Calendar.getInstance();
        dateTV.setText(String.format("%02d.%02d.%02d", c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)));

    }

    /**
     * Action of adding new meeting was invoked from Meetings activity
     *
     * @param args bundle passed to this dialog
     */
    private void handleNewMeetingFromMeeting(Bundle args) {
        dateTV.setText(args.getString(ConstantValues.MEETING_DATE_BUNDLE_KEY).replaceAll("_", "."));
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
