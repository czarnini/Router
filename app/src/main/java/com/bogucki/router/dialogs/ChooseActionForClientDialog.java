package com.bogucki.router.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.bogucki.router.R;
import com.bogucki.router.Utils.ConstantValues;

/**
 * Created by Michał Bogucki
 */

public class ChooseActionForClientDialog extends DialogFragment {


    private static final String TAG = ChooseActionForClientDialog.class.getSimpleName();



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.edit_or_remove_client, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogFragment dialogFragment;

                        Bundle args = getArguments();
                        switch (which){
                            case 0: //Edit
                                dialogFragment = new AddNewOrEditClientDialog();
                                args.putInt(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY,
                                               ConstantValues.EDIT_CLIENT_BUNDLE_VALUE);
                                dialogFragment.setArguments(args);
                                dialogFragment.show(getActivity().getSupportFragmentManager(), TAG);
                                break;
                            case 1: //Remove
                                dialogFragment = new YesNoDialog();
                                args.putInt(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY,
                                               ConstantValues.REMOVE_CLIENT_BUNDLE_VALUE);
                                dialogFragment.setArguments(args);
                                dialogFragment.show(getActivity().getSupportFragmentManager(), TAG);

                            default:
                                Log.d(TAG, "onClick: wrong index selected!");
                                break;
                        }
                    }
                });
        return builder.create();
    }
}
