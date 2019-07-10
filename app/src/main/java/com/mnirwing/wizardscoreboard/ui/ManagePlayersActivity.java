package com.mnirwing.wizardscoreboard.ui;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;

public class ManagePlayersActivity extends AppCompatActivity {

    private static final String TAG = "ManagePlayersActivity";

    private DataHolder data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_players);
        Log.d(TAG, "onCreate: ");

        data = DataHolder.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_all_players);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        PlayerAdapter adapter = new PlayerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setPlayers(data.getPlayers());
    }
}
