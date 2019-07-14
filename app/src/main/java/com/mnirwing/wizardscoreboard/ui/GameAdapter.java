package com.mnirwing.wizardscoreboard.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Move;
import com.mnirwing.wizardscoreboard.data.Player;
import com.mnirwing.wizardscoreboard.data.Round;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameHolder> {

    private static final String TAG = "GameAdapter";


    private List<Player> playersInGame;
    private List<Round> rounds;

    private DataHolder data = DataHolder.getInstance();

    public GameAdapter(List<Player> players) {
        this.playersInGame = players;
        this.rounds = data.getCurrentGame().getRounds();
    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Players:  " + playersInGame.size());
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_game_6player_listitem, parent, false);
        return new GameHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameHolder holder, int position) {
        //if(holder instanceof GameHolder6Player)

        Round currentRound = rounds.get(position);
        holder.textViewRound.setText("" + position + 1);
        holder.textViewPlayer1Guess.setText("" + currentRound.getMoves().get(0).getGuess());
        holder.textViewPlayer2Guess.setText("" + currentRound.getMoves().get(1).getGuess());
        holder.textViewPlayer3Guess.setText("" + currentRound.getMoves().get(2).getGuess());
        holder.textViewPlayer4Guess.setText("" + currentRound.getMoves().get(3).getGuess());
        holder.textViewPlayer5Guess.setText("" + currentRound.getMoves().get(4).getGuess());
        holder.textViewPlayer6Guess.setText("" + currentRound.getMoves().get(5).getGuess());
        holder.textViewPlayer1Score.setText("" + currentRound.getMoves().get(0).getScore());
        holder.textViewPlayer2Score.setText("" + currentRound.getMoves().get(1).getScore());
        holder.textViewPlayer3Score.setText("" + currentRound.getMoves().get(2).getScore());
        holder.textViewPlayer4Score.setText("" + currentRound.getMoves().get(3).getScore());
        holder.textViewPlayer5Score.setText("" + currentRound.getMoves().get(4).getScore());
        holder.textViewPlayer6Score.setText("" + currentRound.getMoves().get(5).getScore());
    }

    @Override
    public int getItemCount() {
        return rounds.size();
    }

    public void notifyRoundAdded() {
        Log.d(TAG, "notifyRoundAdded: ");
        rounds.add(data.getCurrentGame().getCurrentRound());
        notifyItemInserted(rounds.size() - 1);
    }

    public void updateCurrentRound(Round updatedRound) {
        Log.d(TAG, "updateCurrentRound: " + updatedRound);
        this.rounds.remove(rounds.size() - 1);
        this.rounds.add(updatedRound);
        notifyItemChanged(rounds.size() - 1);
    }

    class GameHolder extends RecyclerView.ViewHolder {
        private TextView textViewRound;

        private TextView textViewPlayer1Guess;
        private TextView textViewPlayer1Score;

        private TextView textViewPlayer2Guess;
        private TextView textViewPlayer2Score;

        private TextView textViewPlayer3Guess;
        private TextView textViewPlayer3Score;

        private TextView textViewPlayer4Guess;
        private TextView textViewPlayer4Score;

        private TextView textViewPlayer5Guess;
        private TextView textViewPlayer5Score;

        private TextView textViewPlayer6Guess;
        private TextView textViewPlayer6Score;

        public GameHolder(@NonNull View itemView) {
            super(itemView);
            textViewRound = itemView.findViewById(R.id.textViewRound);

            textViewPlayer1Guess = itemView.findViewById(R.id.textViewPlayer1Guess);
            textViewPlayer1Score = itemView.findViewById(R.id.textViewPlayer1Score);

            textViewPlayer2Guess = itemView.findViewById(R.id.textViewPlayer2Guess);
            textViewPlayer2Score = itemView.findViewById(R.id.textViewPlayer2Score);

            textViewPlayer3Guess = itemView.findViewById(R.id.textViewPlayer3Guess);
            textViewPlayer3Score = itemView.findViewById(R.id.textViewPlayer3Score);

            textViewPlayer4Guess = itemView.findViewById(R.id.textViewPlayer4Guess);
            textViewPlayer4Score = itemView.findViewById(R.id.textViewPlayer4Score);

            textViewPlayer5Guess = itemView.findViewById(R.id.textViewPlayer5Guess);
            textViewPlayer5Score = itemView.findViewById(R.id.textViewPlayer5Score);

            textViewPlayer6Guess = itemView.findViewById(R.id.textViewPlayer6Guess);
            textViewPlayer6Score = itemView.findViewById(R.id.textViewPlayer6Score);
        }
    }
}
