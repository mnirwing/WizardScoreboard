package com.mnirwing.wizardscoreboard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Player;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button buttonResumeGame;

    private Button buttonManagePlayers;

    private Button buttonNewGame;

    private DataHolder data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = DataHolder.getInstance();
        if (!data.isLoaded()) {
            data.load(this);
            if (data.getPlayers().isEmpty()) {
                loadSampleData();
            }
            data.setInitialLoad(true);
        }

        buttonResumeGame = findViewById(R.id.buttonResumeGame);
        buttonManagePlayers = findViewById(R.id.buttonManagePlayers);
        buttonNewGame = findViewById(R.id.buttonNewGame);

        buttonResumeGame.setOnClickListener(e -> {
            if (data.getCurrentGame() == null) {
                new AlertDialog.Builder(this)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, null)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            Intent intent = new Intent(this, GameActivity.class);
            this.startActivity(intent);
        });

        buttonManagePlayers.setOnClickListener(e -> {
            Intent intent = new Intent(this, ManagePlayersActivity.class);
            intent.putExtra("modeManagePlayers", true);
            this.startActivity(intent);
        });

        buttonNewGame.setOnClickListener(e -> {
            Intent intent  = new Intent(this, CreateGameActivity.class);
            this.startActivity(intent);
        });
    }

    private void loadSampleData() {
        Log.d(TAG, "loadSampleData: ");
        data.addPlayer(new Player("Hans", "Hansi"));
        data.addPlayer(new Player("Dieter", "Dieterchen"));
        data.addPlayer(new Player("Klaus", "Klausi"));
        data.addPlayer(new Player("Annegret", "Anne"));
        data.addPlayer(new Player("Heribert", "Heri"));
        data.addPlayer(new Player("Friedhelm", "Friddi"));
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        data.save(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        data.save(this);
        super.onDestroy();
    }
}
