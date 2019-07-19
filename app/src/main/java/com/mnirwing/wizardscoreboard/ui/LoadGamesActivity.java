package com.mnirwing.wizardscoreboard.ui;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.ui.LoadGameAdapter.OnGameListener;

public class LoadGamesActivity extends AppCompatActivity implements OnGameListener {

    private static final String TAG = "LoadGamesActivity";
    private DataHolder data;
    LoadGameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);
        Log.d(TAG, "onCreate: ");

        data = DataHolder.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_all_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new LoadGameAdapter(data, this, this);
        recyclerView.setAdapter(adapter);
        adapter.setGames(data.getGames());
    }

    @Override
    public void onGameClick(int position) {
    }
}
