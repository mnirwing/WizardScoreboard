package com.mnirwing.wizardscoreboard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Game;
import com.mnirwing.wizardscoreboard.data.Move;
import com.mnirwing.wizardscoreboard.data.Player;
import com.mnirwing.wizardscoreboard.data.Round;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private CardView cardViewNewGame;
    private CardView cardViewResumeGame;
    private CardView cardViewManagePlayers;
    private CardView cardViewStatistics;


    private DataHolder data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = DataHolder.getInstance();
        if (!data.isLoaded()) {
            data.load(this);
            data.setInitialLoad(true);
        }

        cardViewNewGame = findViewById(R.id.cardViewNewGame);
        cardViewResumeGame = findViewById(R.id.cardViewResumeGame);
        cardViewManagePlayers = findViewById(R.id.cardViewManagePlayers);
        cardViewStatistics = findViewById(R.id.cardViewStatistics);

        cardViewResumeGame.setOnClickListener(e -> {
            if (data.getGame() == null) {
                Toast.makeText(this, getString(R.string.no_game_found), Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, GameActivity.class);
            this.startActivity(intent);
        });

        cardViewNewGame.setOnClickListener(e -> {
            Intent intent = new Intent(this, CreateGameActivity.class);
            this.startActivity(intent);
        });

        cardViewManagePlayers.setOnClickListener(e -> {
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
        data.addPlayer(new Player("Heribert", "Heri"));
        data.addPlayer(new Player("Friedhelm", "Friddi"));

        Game game = new Game(data.getPlayers());
        for (int i = 0; i < 9; i++) {
            Round round = new Round();
            Move move1 = new Move(data.getPlayers().get(0).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move2 = new Move(data.getPlayers().get(1).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move3 = new Move(data.getPlayers().get(2).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move4 = new Move(data.getPlayers().get(3).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move5 = new Move(data.getPlayers().get(4).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move6 = new Move(data.getPlayers().get(5).getId(), game.getId(),
                    (int) (Math.random() * 4));
            move1.setTricksAndCalculateScore((int) (Math.random() * 4));
            move2.setTricksAndCalculateScore((int) (Math.random() * 4));
            move3.setTricksAndCalculateScore((int) (Math.random() * 4));
            move4.setTricksAndCalculateScore((int) (Math.random() * 4));
            move5.setTricksAndCalculateScore((int) (Math.random() * 4));
            move6.setTricksAndCalculateScore((int) (Math.random() * 4));
            round.addMoves(move1, move2, move3, move4, move5, move6);
            game.addRound(round);
        }
        game.calculateAllTotalScores();
        data.setGame(game);
        data.save(this);
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
