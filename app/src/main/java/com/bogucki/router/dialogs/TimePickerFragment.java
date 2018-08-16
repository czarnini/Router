package com.bogucki.router.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.bogucki.router.R;

import java.util.Calendar;

/**
 * Created by boguc on 07.04.2018.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    private EditText time;
    int hour;
    int minute;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();

        return new TimePickerDialog(getActivity(), this,  hour, minute,true );
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        time.setText(String.format("%02d:%02d", i,i1));
    }

    public void setView(EditText view) {
        this.time = view;
        String[] foo = time.getText().toString().split(":");
        hour = Integer.parseInt(foo[0]);
        minute= Integer.parseInt(foo[1]);
    }
}
