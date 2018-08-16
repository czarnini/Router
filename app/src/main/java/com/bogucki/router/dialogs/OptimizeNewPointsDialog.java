package com.bogucki.router.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.bogucki.router.R;
import com.bogucki.router.adapters.MeetingsAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OptimizeNewPointsDialog extends DialogFragment {

    private MeetingsAdapter meetingsAdapter;
    private Bundle args;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.optimize_new)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        args = getArguments();
                        String key = args.getString("key");
//                        DatabaseReference requests = FirebaseDatabase.getInstance().getReference().child("requests").child(key);
//                        requests.setValue(true);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        meetingsAdapter.notifyDataSetChanged();
                        // Just dismiss dialog box
                    }
                });
        return builder.create();
    }
}
