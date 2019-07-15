package com.mnirwing.wizardscoreboard.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Game;
import com.mnirwing.wizardscoreboard.data.Move;
import com.mnirwing.wizardscoreboard.data.Player;
import com.mnirwing.wizardscoreboard.data.Round;
import com.mnirwing.wizardscoreboard.ui.BidOrTrickDialog.BidOrTrickDialogListener;
import java.util.List;


public class GameActivity extends AppCompatActivity implements BidOrTrickDialogListener {

    private static final String TAG = "GameActivity";

    private TextView textViewGamePlayer1;
    private TextView textViewGamePlayer2;
    private TextView textViewGamePlayer3;
    private TextView textViewGamePlayer4;
    private TextView textViewGamePlayer5;
    private TextView textViewGamePlayer6;

    private Button buttonGameBid;
    private Button buttonGameTricks;

    private List<Player> playersInGame;
    private Game game;

    GameAdapter adapter;

    private DataHolder data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        data = DataHolder.getInstance();

        game = data.getCurrentGame();
        playersInGame = data.getPlayersById(game.getPlayerIds());

        textViewGamePlayer1 = findViewById(R.id.textViewGamePlayer1);
        textViewGamePlayer2 = findViewById(R.id.textViewGamePlayer2);
        textViewGamePlayer3 = findViewById(R.id.textViewGamePlayer3);
        textViewGamePlayer4 = findViewById(R.id.textViewGamePlayer4);
        textViewGamePlayer5 = findViewById(R.id.textViewGamePlayer5);
        textViewGamePlayer6 = findViewById(R.id.textViewGamePlayer6);

        buttonGameBid = findViewById(R.id.buttonGameBid);
        buttonGameTricks = findViewById(R.id.buttonGameTricks);

        textViewGamePlayer1.setText(playersInGame.get(0).getName());
        textViewGamePlayer2.setText(playersInGame.get(1).getName());
        textViewGamePlayer3.setText(playersInGame.get(2).getName());
        textViewGamePlayer4.setText(playersInGame.get(3).getName());

        if (playersInGame.size() < 5) {
            textViewGamePlayer5.setVisibility(View.GONE);
            textViewGamePlayer6.setVisibility(View.GONE);
        } else if (playersInGame.size() == 5) {
            textViewGamePlayer6.setVisibility(View.GONE);
            textViewGamePlayer5.setText(playersInGame.get(4).getName());
        } else if (playersInGame.size() == 6) {
            textViewGamePlayer5.setText(playersInGame.get(4).getName());
            textViewGamePlayer6.setText(playersInGame.get(5).getName());
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view_game);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new GameAdapter(playersInGame);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        buttonGameBid.setOnClickListener(view -> {
            BidOrTrickDialog bidOrTrickDialog = new BidOrTrickDialog(true, playersInGame);
            bidOrTrickDialog.show(getSupportFragmentManager(), "bid dialog");
        });

        buttonGameTricks.setOnClickListener(view -> {
            BidOrTrickDialog bidOrTrickDialog = new BidOrTrickDialog(false, playersInGame);
            bidOrTrickDialog.show(getSupportFragmentManager(), "trick dialog");
        });
    }

    @Override
    public void applyBidsOrTricks(boolean modeIsBid, List<Integer> values) {
        if (modeIsBid) {
            Move move1 = new Move(playersInGame.get(0).getId(), game.getId(),
                    values.get(0));
            Move move2 = new Move(playersInGame.get(1).getId(), game.getId(),
                    values.get(1));
            Move move3 = new Move(playersInGame.get(2).getId(), game.getId(),
                    values.get(2));
            Move move4 = new Move(playersInGame.get(3).getId(), game.getId(),
                    values.get(3));
            Move move5 = new Move(playersInGame.get(4).getId(), game.getId(),
                    values.get(4));
            Move move6 = new Move(playersInGame.get(5).getId(), game.getId(),
                    values.get(5));
            Round currentRound = new Round();
            currentRound.addMoves(move1, move2, move3, move4, move5, move6);
            data.getCurrentGame().addRound(currentRound);
            adapter.notifyRoundAdded();
        } else {
            for (int i = 0; i < game.getCurrentRound().getMoves().size(); i++) {
                Move currentMove = game.getCurrentRound().getMoves().get(i);
                currentMove.setScore(calculateScore(currentMove.getGuess(), values.get(i)));
            }
            game.calculateCurrentRoundTotalScore();
            adapter.updateCurrentRound();
        }

    }

    private int calculateScore(int guess, int tricks) {
        if (guess == tricks) {
            return tricks * 10 + 20;
        } else {
            return Math.abs(guess - tricks) * -10;
        }
    }
}
