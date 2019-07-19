package com.mnirwing.wizardscoreboard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Game;
import com.mnirwing.wizardscoreboard.data.Move;
import com.mnirwing.wizardscoreboard.data.Player;
import com.mnirwing.wizardscoreboard.data.Round;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button buttonResumeGame;
    private Button buttonNewGame;
    private Button buttonLoadGame;
    private Button buttonManagePlayers;


    private DataHolder data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = DataHolder.getInstance();

        loadSampleData();

        //only load data if no players or games exist
//        if (!data.isLoaded()) {
//            data.load(this);
//            if (data.getPlayers().isEmpty() || data.getGames().isEmpty()) {
//                loadSampleData();
//            }
//            data.setInitialLoad(true);
//        }

        buttonResumeGame = findViewById(R.id.buttonResumeGame);
        buttonNewGame = findViewById(R.id.buttonNewGame);
        buttonLoadGame = findViewById(R.id.buttonLoadGame);
        buttonManagePlayers = findViewById(R.id.buttonManagePlayers);

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

        buttonLoadGame.setOnClickListener(e -> {
            Intent intent = new Intent(this, LoadGamesActivity.class);
            this.startActivity(intent);
        });

        buttonNewGame.setOnClickListener(e -> {
            Intent intent = new Intent(this, CreateGameActivity.class);
            this.startActivity(intent);
        });

        buttonManagePlayers.setOnClickListener(e -> {
            Intent intent = new Intent(this, ManagePlayersActivity.class);
            intent.putExtra("modeManagePlayers", true);
            this.startActivity(intent);
        });


    }

    private void loadSampleData() {
        Log.d(TAG, "loadSampleData: ");
        data.deleteAll();

        data.addPlayer(new Player("Hans", "Hansi"));
        data.addPlayer(new Player("Dieter", "Dieterchen"));
        data.addPlayer(new Player("Klaus", "Klausi"));
        data.addPlayer(new Player("Annegret", "Anne"));
//        data.addPlayer(new Player("Heribert", "Heri"));
//        data.addPlayer(new Player("Friedhelm", "Friddi"));

        Game game = new Game(data.getPlayers());
        for (int i = 0; i < 13; i++) {
            Round round = new Round();
            Move move1 = new Move(data.getPlayers().get(0).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move2 = new Move(data.getPlayers().get(1).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move3 = new Move(data.getPlayers().get(2).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move4 = new Move(data.getPlayers().get(3).getId(), game.getId(),
                    (int) (Math.random() * 4));
//            Move move5 = new Move(data.getPlayers().get(4).getId(), game.getId(),
//                    (int) (Math.random() * 4));
//            Move move6 = new Move(data.getPlayers().get(5).getId(), game.getId(),
//                    (int) (Math.random() * 4));
            move1.setTricksAndCalculateScore((int) (Math.random() * 4));
            move2.setTricksAndCalculateScore((int) (Math.random() * 4));
            move3.setTricksAndCalculateScore((int) (Math.random() * 4));
            move4.setTricksAndCalculateScore((int) (Math.random() * 4));
//            move5.setTricksAndCalculateScore((int) (Math.random() * 4));
//            move6.setTricksAndCalculateScore((int) (Math.random() * 4));
//            round.addMoves(move1, move2, move3, move4, move5, move6);
            round.addMoves(move1, move2, move3, move4);
            game.addRound(round);
        }
        game.calculateAllTotalScores();
        data.addGameAndSetCurrent(game);
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
