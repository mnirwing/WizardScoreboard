package com.mnirwing.wizardscoreboard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Game;
import com.mnirwing.wizardscoreboard.data.Player;
import java.util.ArrayList;
import java.util.Arrays;
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
        data = DataHolder.getInstance();

        buttonCreateGame = findViewById(R.id.buttonCreateGame);
        buttonAddPlayer = findViewById(R.id.buttonAddPlayer);
        listViewCreateGame = findViewById(R.id.listViewCreateGamePlayers);
        adapter = new ArrayAdapter<>(this, R.layout.layout_listview, getPlayerNames());
        listViewCreateGame.setAdapter(adapter);
//        intent.putExtra("managePlayersMode", false);

        buttonAddPlayer.setOnClickListener(e -> {
            Intent intent = new Intent(this, ManagePlayersActivity.class);
            intent.putExtra("modeManagePlayers", false);
            startActivityForResult(intent, 1);
        });

        buttonCreateGame.setOnClickListener(e -> {
            if (playersInGame.size() != 6) {
                return;
            }
            buttonCreateGame.setEnabled(false);
            Game game = new Game(playersInGame);
            data.addGameAndSetCurrent(game);
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int playerPosition = (int) data.getExtras().get("position");
        this.playersInGame.add(this.data.getPlayers().get(playerPosition));
        Log.d(TAG, "onActivityResult: playersInGame: " + playersInGame);
        adapter.clear();
        adapter.addAll(getPlayerNames());
        //adapter.notifyDataSetChanged();
        Log.d(TAG, "onActivityResult: AdapterCount: " + adapter.getCount());
    }

    private List<String> getPlayerNames() {
        Log.d(TAG, "getPlayerNames: ");
        List<String> playerNames = new ArrayList<>();
        if (playersInGame == null) {
            return playerNames;
        }
        for (Player player : playersInGame) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }
}
