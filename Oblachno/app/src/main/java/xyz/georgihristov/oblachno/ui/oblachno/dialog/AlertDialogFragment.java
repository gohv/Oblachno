package xyz.georgihristov.oblachno.ui.oblachno.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by gohv on 07.09.16.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Oops!, Sorry")
                .setMessage("There was an Error, please try again!")
                .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
