
package com.mnirwing.wizardscoreboard.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.Player;
import com.shawnlin.numberpicker.NumberPicker;
import java.util.ArrayList;
import java.util.List;

public class BidOrTrickDialog extends DialogFragment {

    private NumberPicker editTextDialogTricks1;
    private NumberPicker editTextDialogTricks2;
    private NumberPicker editTextDialogTricks3;
    private NumberPicker editTextDialogTricks4;
    private NumberPicker editTextDialogTricks5;
    private NumberPicker editTextDialogTricks6;

    private TextView textViewDialogTricks1;
    private TextView textViewDialogTricks2;
    private TextView textViewDialogTricks3;
    private TextView textViewDialogTricks4;
    private TextView textViewDialogTricks5;
    private TextView textViewDialogTricks6;

    private List<Player> playersInGame;
    private BidOrTrickDialogListener listener;
    private final boolean dialogInBidMode;
    private final boolean dialogInEditMode;
    private int roundIndex;
    private List<Integer> editValues;

    public BidOrTrickDialog(boolean dialogInBidMode, int roundIndex, List<Player> playersInGame) {
        this.dialogInBidMode = dialogInBidMode;
        this.roundIndex = roundIndex;
        this.dialogInEditMode = false;
        this.playersInGame = playersInGame;
    }

    public BidOrTrickDialog(boolean dialogInBidMode, int roundIndex, List<Player> playersInGame,
            List<Integer> editValues) {
        this.dialogInBidMode = dialogInBidMode;
        this.roundIndex = roundIndex;
        this.dialogInEditMode = true;
        this.playersInGame = playersInGame;
        this.editValues = editValues;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_tricks, null);
        String title;
        if (dialogInEditMode) {
            title = dialogInBidMode ? getString(R.string.dialog_edit_bid_title)
                    : getString(R.string.dialog_edit_tricks_title);
            title += " " + getString(R.string.round) + ": " + (roundIndex + 1);
        } else {
            title = dialogInBidMode ? getString(R.string.dialog_add_bid_title)
                    : getString(R.string.dialog_add_tricks_title);
        }
        builder.setView(view)
                .setTitle(title)
                .setPositiveButton(android.R.string.yes, (dialog, id) -> {
                    List<Integer> values = new ArrayList<>();
                    values.add(editTextDialogTricks1.getValue());
                    values.add(editTextDialogTricks2.getValue());
                    values.add(editTextDialogTricks3.getValue());
                    values.add(editTextDialogTricks4.getValue());
                    values.add(editTextDialogTricks5.getValue());
                    values.add(editTextDialogTricks6.getValue());
                    listener.applyBidsOrTricks(dialogInBidMode, dialogInEditMode, roundIndex,
                            values);
                })
                .setNegativeButton(android.R.string.cancel,
                        (dialog, id) -> BidOrTrickDialog.this.getDialog().cancel());

        editTextDialogTricks1 = view.findViewById(R.id.numberPickerDialog1);
        editTextDialogTricks2 = view.findViewById(R.id.numberPickerDialog2);
        editTextDialogTricks3 = view.findViewById(R.id.numberPickerDialog3);
        editTextDialogTricks4 = view.findViewById(R.id.numberPickerDialog4);
        editTextDialogTricks5 = view.findViewById(R.id.numberPickerDialog5);
        editTextDialogTricks6 = view.findViewById(R.id.numberPickerDialog6);

        textViewDialogTricks1 = view.findViewById(R.id.textViewDialogTricks1);
        textViewDialogTricks2 = view.findViewById(R.id.textViewDialogTricks2);
        textViewDialogTricks3 = view.findViewById(R.id.textViewDialogTricks3);
        textViewDialogTricks4 = view.findViewById(R.id.textViewDialogTricks4);
        textViewDialogTricks5 = view.findViewById(R.id.textViewDialogTricks5);
        textViewDialogTricks6 = view.findViewById(R.id.textViewDialogTricks6);

        textViewDialogTricks1.setText(playersInGame.get(0).getName());
        textViewDialogTricks2.setText(playersInGame.get(1).getName());
        textViewDialogTricks3.setText(playersInGame.get(2).getName());
        textViewDialogTricks4.setText(playersInGame.get(3).getName());
        textViewDialogTricks5.setText(playersInGame.get(4).getName());
        textViewDialogTricks6.setText(playersInGame.get(5).getName());

        if (editValues != null) {
            editTextDialogTricks1.setValue(editValues.get(0));
            editTextDialogTricks2.setValue(editValues.get(1));
            editTextDialogTricks3.setValue(editValues.get(2));
            editTextDialogTricks4.setValue(editValues.get(3));
            editTextDialogTricks5.setValue(editValues.get(4));
            editTextDialogTricks6.setValue(editValues.get(5));
        }
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (BidOrTrickDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() + "must implement BidOrTrickDialogListener");
        }
    }

    public interface BidOrTrickDialogListener {

        void applyBidsOrTricks(boolean dialogWasInBidMode, boolean dialogWasInEditMode,
                int roundIndex, List<Integer> values);
    }
}
