package com.bogucki.router;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by Michał Bogucki
 */

public class ChooseActionForClientDialog extends DialogFragment {
    private static final String TAG = ChooseActionForClientDialog.class.getSimpleName();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.edit_or_remove_client, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0: //Edit
                                break;
                            case 1: //Remove
                                DialogFragment fragment;
                            default:
                                Log.d(TAG, "onClick: wrong index selected!");
                                break;
                        }
                    }
                });
        return builder.create();
    }
}
