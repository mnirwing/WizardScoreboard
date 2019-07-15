package com.mnirwing.wizardscoreboard.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.ui.PlayerAdapter.OnPlayerListener;

public class ManagePlayersActivity extends AppCompatActivity implements OnPlayerListener {

    private static final String TAG = "ManagePlayersActivity";

    private DataHolder data;

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

        PlayerAdapter adapter = new PlayerAdapter(modeManagePlayers, this);
        recyclerView.setAdapter(adapter);
        adapter.setPlayers(data.getPlayers());
    }

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
}
