
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

    private static final String TAG = "BidOrTrickDialog";

    private NumberPicker[] numberPickers = new NumberPicker[6];

    private TextView[] textViewDialogTricks = new TextView[6];

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

//        int layoutId;
//        switch (playersInGame.size()) {
//            case 4:
//                layoutId = R.layout.dialog_tricks;
//                break;
//            case 5:
//                layoutId = R.layout.dialog_tricks;
//                break;
//            default:
//                layoutId = R.layout.dialog_tricks;
//        }

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
                    for (NumberPicker np : numberPickers) {
                        values.add(np.getValue());
                    }
                    listener.applyBidsOrTricks(dialogInBidMode, dialogInEditMode, roundIndex,
                            values);
                })
                .setNegativeButton(android.R.string.cancel,
                        (dialog, id) -> BidOrTrickDialog.this.getDialog().cancel());

        numberPickers[0] = view.findViewById(R.id.numberPickerDialog1);
        numberPickers[1] = view.findViewById(R.id.numberPickerDialog2);
        numberPickers[2] = view.findViewById(R.id.numberPickerDialog3);
        numberPickers[3] = view.findViewById(R.id.numberPickerDialog4);
        numberPickers[4] = view.findViewById(R.id.numberPickerDialog5);
        numberPickers[5] = view.findViewById(R.id.numberPickerDialog6);

        textViewDialogTricks[0] = view.findViewById(R.id.textViewDialogTricks1);
        textViewDialogTricks[1] = view.findViewById(R.id.textViewDialogTricks2);
        textViewDialogTricks[2] = view.findViewById(R.id.textViewDialogTricks3);
        textViewDialogTricks[3] = view.findViewById(R.id.textViewDialogTricks4);
        textViewDialogTricks[4] = view.findViewById(R.id.textViewDialogTricks5);
        textViewDialogTricks[5] = view.findViewById(R.id.textViewDialogTricks6);

        for (int i = 0; i < playersInGame.size(); i++) {
            textViewDialogTricks[i].setText(playersInGame.get(i).getName());
        }

        if (editValues != null) {
            for (int i = 0; i < editValues.size(); i++) {
                numberPickers[i].setValue(editValues.get(i));
            }
        }

        if (playersInGame.size() == 4) {
            numberPickers[4].setVisibility(View.GONE);
            numberPickers[5].setVisibility(View.GONE);
        }
        if (playersInGame.size() == 5) {
            numberPickers[5].setVisibility(View.GONE);
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
