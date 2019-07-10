package com.mnirwing.wizardscoreboard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.Game;
import com.mnirwing.wizardscoreboard.data.Move;
import com.mnirwing.wizardscoreboard.data.Player;
import com.mnirwing.wizardscoreboard.data.Round;


public class GameActivity extends AppCompatActivity {

  private static final String TAG = "GameActivity";

  private TextView textViewGamePlayer1;
  private TextView textViewGamePlayer2;
  private TextView textViewGamePlayer3;
  private TextView textViewGamePlayer4;
  private TextView textViewGamePlayer5;
  private TextView textViewGamePlayer6;

  private Button buttonGameBid;
  private Button buttonGameTricks;


  private Player[] playersInGame;
  private Game game;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game);

    Intent intent = getIntent();
    playersInGame = (Player[]) intent.getExtras().get("players");
    game = (Game) intent.getExtras().get("game");

    textViewGamePlayer1 = findViewById(R.id.textViewGamePlayer1);
    textViewGamePlayer2 = findViewById(R.id.textViewGamePlayer2);
    textViewGamePlayer3 = findViewById(R.id.textViewGamePlayer3);
    textViewGamePlayer4 = findViewById(R.id.textViewGamePlayer4);
    textViewGamePlayer5 = findViewById(R.id.textViewGamePlayer5);
    textViewGamePlayer6 = findViewById(R.id.textViewGamePlayer6);

    buttonGameBid = findViewById(R.id.buttonGameBid);
    buttonGameTricks = findViewById(R.id.buttonGameTricks);

    textViewGamePlayer1.setText(playersInGame[0].getName());
    textViewGamePlayer2.setText(playersInGame[1].getName());
    textViewGamePlayer3.setText(playersInGame[2].getName());
    textViewGamePlayer4.setText(playersInGame[3].getName());

    if(playersInGame.length < 5) {
      textViewGamePlayer5.setVisibility(View.GONE);
      textViewGamePlayer6.setVisibility(View.GONE);
    } else if (playersInGame.length == 5) {
      textViewGamePlayer6.setVisibility(View.GONE);
      textViewGamePlayer5.setText(playersInGame[4].getName());
    } else if(playersInGame.length == 6) {
      textViewGamePlayer5.setText(playersInGame[4].getName());
      textViewGamePlayer6.setText(playersInGame[5].getName());
    }

    RecyclerView recyclerView = findViewById(R.id.recycler_view_game);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setHasFixedSize(true);

    GameAdapter adapter = new GameAdapter(playersInGame);
    recyclerView.setAdapter(adapter);

    // add a dialog to set the player bids
    buttonGameBid.setOnClickListener(view -> {
        createMoves();
    });

    // add a dialog to set the player tricks
    buttonGameTricks.setOnClickListener(view -> {
      Move move1 = new Move(playersInGame[0].getId(), game.getId(), (int) (Math.random() + 1) );
      Move move2 = new Move(playersInGame[1].getId(), game.getId(), (int) (Math.random() + 1) );
      Move move3 = new Move(playersInGame[2].getId(), game.getId(), (int) (Math.random() + 1) );
      Move move4 = new Move(playersInGame[3].getId(), game.getId(), (int) (Math.random() + 1) );
      Move move5 = new Move(playersInGame[4].getId(), game.getId(), (int) (Math.random() + 1) );
      Move move6 = new Move(playersInGame[5].getId(), game.getId(), (int) (Math.random() + 1) );
      Round round = new Round();
      round.addMove(move1, move2, move3, move4, move5, move6);
      game.addRound(round);
      adapter.addRound(round);
    });
  }

  private void createMoves() {

  }
}
