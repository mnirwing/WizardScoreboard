package com.mnirwing.wizardscoreboard.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private boolean modeManagePlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_players);
        Log.d(TAG, "onCreate: ");

        Intent intent = getIntent();
        modeManagePlayers = (boolean) intent.getExtras().get("modeManagePlayers");

        data = DataHolder.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_all_players);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new PlayerAdapter(modeManagePlayers, this);
        recyclerView.setAdapter(adapter);
        adapter.setPlayers(data.getPlayers());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            AddOrEditPlayerDialog dialog = new AddOrEditPlayerDialog();
            dialog.show(getSupportFragmentManager(), "add player dialog");
        });
    }

    /**
     * This method gets called when a player is clicked on if this activity is used to add players
     * to a game.
     *
     * @param position The position of the item that was clicked on.
     */
    @Override
    public void onPlayerClick(int position) {
        Log.d(TAG, "onPlayerClick: " + position);
        if (!modeManagePlayers) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("position", position);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    /**
     * This method gets called from the dialog if a player was added.
     */
    @Override
    public void applyPlayerData(String name, String nickname) {
        data.addPlayer(new Player(name, nickname));
        adapter.notifyPlayerAdded();
    }
}
