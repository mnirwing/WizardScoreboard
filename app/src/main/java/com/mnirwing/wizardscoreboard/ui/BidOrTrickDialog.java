
package com.mnirwing.wizardscoreboard.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Player;
import java.util.ArrayList;
import java.util.List;

public class BidOrTrickDialog extends DialogFragment {

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
                    values.add(Integer.parseInt(editTextDialogTricks1.getText().toString()));
                    values.add(Integer.parseInt(editTextDialogTricks2.getText().toString()));
                    values.add(Integer.parseInt(editTextDialogTricks3.getText().toString()));
                    values.add(Integer.parseInt(editTextDialogTricks4.getText().toString()));
                    values.add(Integer.parseInt(editTextDialogTricks5.getText().toString()));
                    values.add(Integer.parseInt(editTextDialogTricks6.getText().toString()));
                    listener.applyBidsOrTricks(modeIsBid, values);
                })
                .setNegativeButton(R.string.cancel,
                        (dialog, id) -> BidOrTrickDialog.this.getDialog().cancel());

        editTextDialogTricks1 = view.findViewById(R.id.editTextDialogTricks1);
        editTextDialogTricks2 = view.findViewById(R.id.editTextDialogTricks2);
        editTextDialogTricks3 = view.findViewById(R.id.editTextDialogTricks3);
        editTextDialogTricks4 = view.findViewById(R.id.editTextDialogTricks4);
        editTextDialogTricks5 = view.findViewById(R.id.editTextDialogTricks5);
        editTextDialogTricks6 = view.findViewById(R.id.editTextDialogTricks6);

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
