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

public class ChooseActionForMeeting extends DialogFragment {


    private static final String TAG = ChooseActionForMeeting.class.getSimpleName();


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: Dialog created");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.choose_action_for_meeting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogFragment dialogFragment;

                Bundle args = getArguments();
                switch (which) {
                    case 0: //Edit
                        dialogFragment = new AddNewOrEditMeetingDialog();
                        args.putInt(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY,
                                ConstantValues.EDIT_MEETING_BUNDLE_VALUE);
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getActivity().getSupportFragmentManager(), TAG);
                        break;
                    case 1: //Remove
                        dialogFragment = new YesNoDialog();
                        args.putInt(ConstantValues.CHOOSE_ACTION_BUNDLE_KEY,
                                ConstantValues.REMOVE_MEETING_BUNDLE_VALUE);
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getActivity().getSupportFragmentManager(), TAG);
                        break;

                    default:
                        Log.d(TAG, "onClick: wrong index selected! " + which);
                        break;
                }
            }
        });
        return builder.create();
    }
}
