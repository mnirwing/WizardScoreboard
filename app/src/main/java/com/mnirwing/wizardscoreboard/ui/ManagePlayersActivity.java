package com.mnirwing.wizardscoreboard.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Player;
import com.mnirwing.wizardscoreboard.ui.AddOrEditPlayerDialog.AddOrEditPlayerDialogListener;
import com.mnirwing.wizardscoreboard.ui.PlayerAdapter.OnPlayerListener;

public class ManagePlayersActivity extends AppCompatActivity implements OnPlayerListener,
        AddOrEditPlayerDialogListener {

    private static final String TAG = "ManagePlayersActivity";

    private DataHolder data;
    PlayerAdapter adapter;
    private MenuItem deleteAction;
    private int currentlyHighlightedRound = -1;

    private int positionOfPlayerLongClicked;
    private boolean modeManagePlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_players);
        getSupportActionBar().setTitle(getString(R.string.manage_players));
        Log.d(TAG, "onCreate: ");
        positionOfPlayerLongClicked = -1;

        Intent intent = getIntent();
        modeManagePlayers = (boolean) intent.getExtras().get("modeManagePlayers");

        data = DataHolder.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_all_players);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new PlayerAdapter(this, modeManagePlayers, this);
        recyclerView.setAdapter(adapter);
        adapter.setPlayers(data.getPlayers());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            AddOrEditPlayerDialog dialog = new AddOrEditPlayerDialog();
            dialog.show(getSupportFragmentManager(), "add player dialog");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_players, menu);
        deleteAction = menu.getItem(0);
        deleteAction.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_delete || currentlyHighlightedRound == -1) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_delete_player)
                .setPositiveButton(R.string.okay, (dialog, id) -> {
                    data.deleteGamesWherePlayerIsInvolved(
                            data.getPlayers().get(currentlyHighlightedRound));
                    adapter.notifyItemRemoved(currentlyHighlightedRound);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    // User cancelled the dialog
                });
        builder.create().show();
        return true;
    }

    /**
     * This method gets called when a player is clicked on if this activity is used to add players
     * to a game.
     *
     * @param position The position of the item that was clicked on.
     */
    @Override
    public void onPlayerClick(int position, boolean selected) {
        Log.d(TAG, "onPlayerClick: " + position);
        if (!modeManagePlayers) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("position", position);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            if (selected) {
                currentlyHighlightedRound = position;
            } else {
                currentlyHighlightedRound = -1;
            }
            deleteAction.setVisible(selected);
        }
    }

    @Override
    public boolean onPlayerLongClick(int position) {
        positionOfPlayerLongClicked = position;
        AddOrEditPlayerDialog dialog = new AddOrEditPlayerDialog(data.getPlayers().get(position).getName(), data.getPlayers().get(position).getNickname());
        dialog.show(getSupportFragmentManager(), "edit player dialog");
        return false;
    }

    /**
     * This method gets called from the dialog if a player was added.
     */
    @Override
    public void applyPlayerData(String name, String nickname, boolean editMode) {
        if((name == null && nickname == null))
            return;
        if((name.isEmpty() && nickname.isEmpty()))
            return;
        if(editMode){
            data.getPlayers().get(positionOfPlayerLongClicked).setName(name);
            data.getPlayers().get(positionOfPlayerLongClicked).setNickname(nickname);
            adapter.notifyItemChanged(positionOfPlayerLongClicked);
            positionOfPlayerLongClicked = -1;
        }
        else {
            data.addPlayer(new Player(name, nickname));
            adapter.notifyPlayerAdded();
        }
    }
}
