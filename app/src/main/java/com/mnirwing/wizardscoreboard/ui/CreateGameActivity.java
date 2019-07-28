package com.mnirwing.wizardscoreboard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Game;
import com.mnirwing.wizardscoreboard.data.Player;
import java.util.ArrayList;
import java.util.List;

public class CreateGameActivity extends AppCompatActivity {

    private static final String TAG = "CreateGameActivity";

    private Button buttonCreateGame;
    private Button buttonAddPlayer;

    private ListView listViewCreateGame;

    private List<Player> playersInGame = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private DataHolder data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategame);
        getSupportActionBar().setTitle(getString(R.string.create_game));
        data = DataHolder.getInstance();

        buttonCreateGame = findViewById(R.id.buttonCreateGame);
        buttonAddPlayer = findViewById(R.id.buttonAddPlayer);
        listViewCreateGame = findViewById(R.id.listViewCreateGamePlayers);

        buttonCreateGame.setEnabled(enoughPlayersAdded());
        adapter = new ArrayAdapter<>(this, R.layout.layout_listview,
                this.data.getPlayerNames(playersInGame));
        listViewCreateGame.setAdapter(adapter);

        buttonAddPlayer.setOnClickListener(e -> {
            Intent intent = new Intent(this, ManagePlayersActivity.class);
            intent.putExtra("modeManagePlayers", false);
            startActivityForResult(intent, 1);
        });

        buttonCreateGame.setOnClickListener(e -> {
            if (!enoughPlayersAdded()) {
                Toast.makeText(this, getString(R.string.player_amount), Toast.LENGTH_SHORT).show();
                return;
            }
            Game game = new Game(playersInGame);
            data.setGame(game);
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        int playerPosition = (int) data.getExtras().get("position");
        this.playersInGame.add(this.data.getPlayers().get(playerPosition));
        adapter.clear();
        adapter.addAll(this.data.getPlayerNames(playersInGame));
        buttonCreateGame.setEnabled(enoughPlayersAdded());
    }


    private boolean enoughPlayersAdded() {
        return playersInGame.size() >= 3 && playersInGame.size() <= 6;
    }
}
