package com.mnirwing.wizardscoreboard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Game;
import com.mnirwing.wizardscoreboard.data.Player;

public class CreateGameActivity extends AppCompatActivity {

    private static final String TAG = "CreateGameActivity";

    private Button buttonCreateGame;

    private ListView listViewCreateGame;

    private DataHolder data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategame);
        data = DataHolder.getInstance();

        buttonCreateGame = findViewById(R.id.buttonCreateGame);
        listViewCreateGame = findViewById(R.id.listViewCreateGamePlayers);

        buttonCreateGame.setOnClickListener(e -> {
            buttonCreateGame.setEnabled(false);
            Player[] playersInGame = {data.getPlayers().get(0), data.getPlayers().get(1),data.getPlayers().get(2),data.getPlayers().get(3),data.getPlayers().get(4),data.getPlayers().get(5)};
            Game game = createGame(playersInGame);
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("players" , playersInGame);
            intent.putExtra("game" , game);
            startActivity(intent);
        });
    }

    private Game createGame(Player...players) {
        Game game = new Game(players[0].getId(), players[1].getId(), players[2].getId(), players[3].getId(), players[4].getId(), players[5].getId());
        game.setCurrentGame(true);
        data.addGame(game);
        return game;
    }
}
