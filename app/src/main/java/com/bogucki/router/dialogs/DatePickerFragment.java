package com.bogucki.router.dialogs;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bogucki.router.activities.Meetings;
import com.bogucki.router.utils.ConstantValues;

import java.util.Calendar;

/**
 * Created by MichałBogucki
 */

public class DatePickerFragment  extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private boolean calledFromMainActivity = false;
    private EditText time;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerFragment and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        if(calledFromMainActivity) {
            Intent intent = new Intent(getContext(), Meetings.class);
            intent.putExtra(ConstantValues.MEETING_DATE_BUNDLE_KEY, String.format("%02d.%02d.%02d", day,month+1,year));
            startActivity(intent);
        } else{
            time.setText(String.format("%02d.%02d.%02d", day,month+1,year));
        }

    }

    public void isCalledFromMainActivity() {
        calledFromMainActivity = true;
    }

    public void setTime(EditText time){
        this.time = time;
    }
}
