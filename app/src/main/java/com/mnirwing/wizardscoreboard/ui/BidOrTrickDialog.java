
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

    List<Integer> values;
    List<Player> playersInGame;
    private BidOrTrickDialogListener listener;
    private final boolean modeIsBid;

    public BidOrTrickDialog(boolean modeIsBid, List<Player> playersInGame) {
        this.modeIsBid = modeIsBid;
        this.playersInGame = playersInGame;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_tricks, null);
        builder.setView(view)
                .setTitle(modeIsBid ? R.string.dialog_bid_title : R.string.dialog_tricks_title)
                .setPositiveButton(R.string.okay, (dialog, id) -> {
                    values = new ArrayList<>();
                    values.add(editTextDialogTricks1.getValue());
                    values.add(editTextDialogTricks2.getValue());
                    values.add(editTextDialogTricks3.getValue());
                    values.add(editTextDialogTricks4.getValue());
                    values.add(editTextDialogTricks5.getValue());
                    values.add(editTextDialogTricks6.getValue());
                    listener.applyBidsOrTricks(modeIsBid, values);
                })
                .setNegativeButton(R.string.cancel,
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

        void applyBidsOrTricks(boolean modeIsBid, List<Integer> tricks);
    }
}