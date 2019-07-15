
package com.mnirwing.wizardscoreboard.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.mnirwing.wizardscoreboard.R;
import org.w3c.dom.Text;

public class TricksDialog extends DialogFragment {

    private EditText editTextDialogTricks1;
    private EditText editTextDialogTricks2;
    private EditText editTextDialogTricks3;
    private EditText editTextDialogTricks4;
    private EditText editTextDialogTricks5;
    private EditText editTextDialogTricks6;

    private TextView textViewDialogTricks1;
    private TextView textViewDialogTricks2;
    private TextView textViewDialogTricks3;
    private TextView textViewDialogTricks4;
    private TextView textViewDialogTricks5;
    private TextView textViewDialogTricks6;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_tricks, null))
                .setPositiveButton(R.string.okay, (dialog, id) -> {
                    // sign in the user ...
                })
                .setNegativeButton(R.string.cancel,
                        (dialog, id) -> TricksDialog.this.getDialog().cancel());
        return builder.create();
    }

}
