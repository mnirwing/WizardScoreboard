package com.mnirwing.wizardscoreboard.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Game;
import com.mnirwing.wizardscoreboard.data.Player;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class LoadGameAdapter extends RecyclerView.Adapter<LoadGameAdapter.GameHolder> {

    private static final String TAG = "LoadGameAdapter";
    private Context context;
    private List<Game> games;
    private final OnGameListener onGameListener;
    private DataHolder data;

    public LoadGameAdapter(DataHolder data, OnGameListener onGameListener, Context context) {
        this.data = data;
        this.onGameListener = onGameListener;
        this.context = context;
    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_load_game_listitem, parent, false);
        return new GameHolder(itemView, onGameListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GameHolder holder, int position) {
        Game current = games.get(position);
        List<Player> players = new ArrayList<>(data.getPlayersById(current.getPlayerIds()));
        holder.textViewGameDate
                .setText(DateFormat.getDateInstance().format(current.getCreatedAt()));
        holder.textViewPlayerNames.setText(getStringOfPlayerNames(players));
        holder.textViewCurrentRound
                .setText(context.getString(R.string.round_number, current.getRounds().size()));
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    class GameHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private TextView textViewGameDate;
        private TextView textViewPlayerNames;
        private TextView textViewCurrentRound;

        private OnGameListener onGameListener;

        public GameHolder(@NonNull View itemView, OnGameListener onGameListener) {
            super(itemView);
            textViewGameDate = itemView.findViewById(R.id.textViewGameDate);
            textViewPlayerNames = itemView.findViewById(R.id.textViewPlayerNames);
            textViewCurrentRound = itemView.findViewById(R.id.textViewCurrentRound);
            this.onGameListener = onGameListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onGameListener.onGameClick(getAdapterPosition());
        }
    }

    public interface OnGameListener {

        void onGameClick(int position);
    }

    private String getStringOfPlayerNames(List<Player> players) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < players.size(); i++) {
            sb.append(players.get(i).getName()).append(i < players.size() - 1 ? ", " : "");
        }
        return sb.toString();
    }
}
