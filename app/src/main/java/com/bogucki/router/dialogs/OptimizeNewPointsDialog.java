package com.bogucki.router.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.bogucki.router.R;
import com.google.firebase.database.DatabaseReference;

public class OptimizeNewPointsDialog extends DialogFragment {


    private DatabaseReference requests;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.optimize_new)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(requests!= null){
                            requests.setValue(true);
                        }
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        return builder.create();
    }

    public void setRequests(DatabaseReference requests) {
        this.requests = requests;
    }
}
