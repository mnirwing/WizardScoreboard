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
import com.mnirwing.wizardscoreboard.data.Player;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerHolder> {

    private static final String TAG = "PlayerAdapter";

    private Context context;

    private List<Player> players;

    private final boolean modeManagePlayers;

    private final OnPlayerListener onPlayerListener;

    private int selectedPosition = RecyclerView.NO_POSITION;

    public PlayerAdapter(Context context, boolean modeManagePlayers,
            OnPlayerListener onPlayerListener, List<Player> players) {
        this.context = context;
        this.modeManagePlayers = modeManagePlayers;
        this.onPlayerListener = onPlayerListener;
        this.players = players;

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

        if (selectedPosition != position) {
            holder.itemView
                    .setBackgroundColor(context.getColor(R.color.colorAlternatingRowNormal));
        } else {
            holder.itemView
                    .setBackgroundColor(context.getColor(R.color.colorRedPrimaryLight));
        }
    }

    @Override
    public int getItemCount() {
        return players.size();
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
            if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                return;
            }

            int oldPosition = selectedPosition;
            // Updating old as well as new positions
            notifyItemChanged(oldPosition);

            selectedPosition = getAdapterPosition();
            if (oldPosition == selectedPosition) {
                selectedPosition = RecyclerView.NO_POSITION;
                notifyItemChanged(selectedPosition);
                onPlayerListener.onPlayerClick(selectedPosition, false);
            } else {
                notifyItemChanged(selectedPosition);
                onPlayerListener.onPlayerClick(selectedPosition, true);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            onPlayerListener.onPlayerLongClick(getAdapterPosition());
            return false;
        }
    }

    public interface OnPlayerListener {

        void onPlayerClick(int position, boolean selected);

        boolean onPlayerLongClick(int position);
    }
}
