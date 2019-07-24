package com.mnirwing.wizardscoreboard.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.Player;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerHolder> {

    private static final String TAG = "PlayerAdapter";

    private List<Player> players;

    private final boolean modeManagePlayers;

    private final OnPlayerListener onPlayerListener;

    public PlayerAdapter(boolean modeManagePlayers, OnPlayerListener onPlayerListener) {
        this.modeManagePlayers = modeManagePlayers;
        this.onPlayerListener = onPlayerListener;
    }

    @NonNull
    @Override
    public PlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_player_listitem, parent, false);
        return new PlayerHolder(itemView, onPlayerListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerHolder holder, int position) {
        Player currentPlayer = players.get(position);
        holder.textViewPlayerName.setText(currentPlayer.getName());
        holder.textViewPlayerNickname.setText(currentPlayer.getNickname());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
        notifyDataSetChanged();
    }

    public void notifyPlayerAdded() {
        notifyItemInserted(players.size() - 1);
    }

    class PlayerHolder extends RecyclerView.ViewHolder implements OnClickListener, View.OnLongClickListener {
        private TextView textViewPlayerName;
        private TextView textViewPlayerNickname;

        private OnPlayerListener onPlayerListener;

        public PlayerHolder(@NonNull View itemView, OnPlayerListener onPlayerListener) {
            super(itemView);
            textViewPlayerName = itemView.findViewById(R.id.textViewGameDate);
            textViewPlayerNickname = itemView.findViewById(R.id.textViewPlayerNames);
            this.onPlayerListener = onPlayerListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPlayerListener.onPlayerClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onPlayerListener.onPlayerLongClick(getAdapterPosition());
            return false;
        }
    }

    public interface OnPlayerListener {
        void onPlayerClick(int position);

        boolean onPlayerLongClick(int position);
    }
}
