package com.bogucki.router.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class DelayedClientsDialog extends DialogFragment {
    private int counter = 0;
    public void setCounter(int counter) {
        this.counter = counter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Liczba klientów, którzy mogą być odwiedzeni zbyt późno: " + counter + ". Skontaktuj się z tymi klientami w celu zmiany terminu")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}
