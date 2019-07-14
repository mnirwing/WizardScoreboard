package com.mnirwing.wizardscoreboard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Player;

public class MainActivity extends AppCompatActivity {


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
            if(data.getPlayers() != null && data.getPlayers().isEmpty()) {
                loadSampleData();
            }
            data.setInitialLoad(true);
        }

        buttonManagePlayers = findViewById(R.id.buttonManagePlayers);
        buttonNewGame = findViewById(R.id.buttonNewGame);

        buttonManagePlayers.setOnClickListener(e -> {
            Intent intent = new Intent(this, ManagePlayersActivity.class);
            this.startActivity(intent);
        });

        buttonNewGame.setOnClickListener(e -> {
            Intent intent  = new Intent(this, CreateGameActivity.class);
            this.startActivity(intent);
        });
    }

    private void loadSampleData() {
        data.addPlayer(new Player("Hans", "Hansi"));
        data.addPlayer(new Player("Dieter", "Dieterchen"));
        data.addPlayer(new Player("Klaus", "Klausi"));
        data.addPlayer(new Player("Annegret", "Anne"));
        data.addPlayer(new Player("Heribert", "Heri"));
        data.addPlayer(new Player("Friedhelm", "Friddi"));
    }
}
