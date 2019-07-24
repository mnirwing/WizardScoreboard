package com.mnirwing.wizardscoreboard.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.mnirwing.wizardscoreboard.R;

public class AddOrEditPlayerDialog extends DialogFragment {

    private EditText editTextDialogAddPlayerName;
    private EditText editTextDialogAddPlayerNickname;

    private AddOrEditPlayerDialogListener listener;

    private String name;
    private String nickname;

    private boolean editMode;

    public AddOrEditPlayerDialog() {
    }

    public AddOrEditPlayerDialog(String name, String nickname) {
        this.name = name;
        this.nickname = nickname;
        editMode = true;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_add_or_edit_player, null);
        builder.setView(view)
                .setTitle(name == null ? R.string.dialog_add_player_title
                        : R.string.dialog_edit_player_title)
                .setPositiveButton(android.R.string.yes, (dialog, id) -> {
                    name = editTextDialogAddPlayerName.getText().toString();
                    nickname = editTextDialogAddPlayerNickname.getText().toString();
                    listener.applyPlayerData(name, nickname, editMode);
                })
                .setNegativeButton(android.R.string.cancel, null);

        editTextDialogAddPlayerName = view.findViewById(R.id.editTextDialogAddPlayerName);
        editTextDialogAddPlayerNickname = view.findViewById(R.id.editTextDialogAddPlayerNickname);

        if (name != null && nickname != null) {
            editTextDialogAddPlayerName.setText(name);
            editTextDialogAddPlayerNickname.setText(nickname);
        }
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddOrEditPlayerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() + "must implement AddOrEditDialogListener");
        }
    }

    public interface AddOrEditPlayerDialogListener {

        void applyPlayerData(String name, String nickname, boolean editMode);
    }
}
