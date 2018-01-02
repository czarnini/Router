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

import com.bogucki.router.R;
import com.bogucki.router.utils.ConstantValues;
import com.bogucki.router.models.Client;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by MichałBogucki
 */

public class AddNewOrEditClientDialog extends DialogFragment {

    private DatabaseReference clientsReference;
    private EditText nameET;
    private EditText addressET;
    private String name;
    private String address;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final int action = args.getInt(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View rootView = inflater.inflate(R.layout.add_or_edit_client, null) ;

        nameET = (EditText) rootView.findViewById(R.id.client_name_ET);
        addressET = (EditText) rootView.findViewById(R.id.client_address_ET);

        if(action == ConstantValues.EDIT_CLIENT_BUNDLE_VALUE){
            name = args.getString(ConstantValues.CLIENT_NAME_BUNDLE_KEY);
            address = args.getString(ConstantValues.CLIENT_ADDRESS_BUNDLE_KEY);

            nameET.setText(name);
            addressET.setText(address);
        }

        builder.setView(rootView);
        builder.setMessage(action == ConstantValues.ADD_CLIENT_BUNDLE_VALUE? R.string.new_client : R.string.edit)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        clientsReference = FirebaseDatabase.getInstance().getReference().child(ConstantValues.CLIENTS_FIREBASE);
                        if (action == ConstantValues.ADD_CLIENT_BUNDLE_VALUE) {
                            addNewClient();
                        } else {
                            name = nameET.getText().toString();
                            address = addressET.getText().toString();
                            updateClient(args.getString(ConstantValues.CLIENT_ID_BUNDLE_KEY), name, address);
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

    private void addNewClient() {

        if (!"".equals(name) && !"".equals(address)) {
            String pushId = clientsReference.push().getKey();
            clientsReference.child(pushId).setValue(new Client(pushId, name, address));
        }
    }

    private void updateClient(String pushId, String name, String address) {
        if (!"".equals(name) && !"".equals(address)) {
            DatabaseReference clientToUpdate = clientsReference.child(pushId);
            clientToUpdate.setValue(new Client(pushId,name, address));
        }
    }


}
