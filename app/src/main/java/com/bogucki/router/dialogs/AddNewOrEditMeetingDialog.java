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

/**
 * Created by Michał Bogucki
 */

public class AddNewOrEditMeetingDialog extends DialogFragment {

    private EditText nameTV;
    private EditText addressTV;
    private EditText dateTV;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View rootView = inflater.inflate(R.layout.add_or_edit_meeting, null);


        nameTV = (EditText) rootView.findViewById(R.id.client_name);
        addressTV = (EditText) rootView.findViewById(R.id.client_address);
        dateTV = (EditText) rootView.findViewById(R.id.date);

        handleArgs();

        builder.setView(rootView);
        builder.setMessage("Nowe spotkanie")
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

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
        if (ConstantValues.CLIENTS_FIREBASE.equals(args.getString(ConstantValues.FROM_MEETINGS_OR_FROM_CLIENTS_BUNDLE_KEY))) {
            handleActionFromClients(args);
        } else {
            handleActionFromMeetings(args);
        }
    }

    private void handleActionFromClients(Bundle args) {
        nameTV.setText(args.getString(ConstantValues.CLIENT_NAME_BUNDLE_KEY));
        addressTV.setText(args.getString(ConstantValues.CLIENT_ADDRESS_BUNDLE_KEY));
    }


    private void handleActionFromMeetings(Bundle args) {
        dateTV.setText(args.getString(ConstantValues.MEETING_DATE_BUNDLE_KEY));
    }
}
