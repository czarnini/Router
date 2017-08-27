package com.bogucki.router.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.bogucki.router.R;
import com.bogucki.router.database.DbHelper;

/**
 * Created by MichałBogucki
 */

public class AddNewClientDialog extends DialogFragment {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View rootView = inflater.inflate(R.layout.add_or_edit_client, null) ;
        builder.setView(rootView);
        builder.setMessage(R.string.new_client)
                .setPositiveButton(R.string.save_client, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText nameET = (EditText) rootView.findViewById(R.id.client_name_ET);
                        EditText addressET = (EditText) rootView.findViewById(R.id.client_address_ET);

                        String name = nameET.getText().toString();
                        String address = addressET.getText().toString();
                        if (!"".equals(name) && !"".equals(address)) {
                            DbHelper helper = new DbHelper(getContext());
                            helper.addClient(name, address);
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


}
