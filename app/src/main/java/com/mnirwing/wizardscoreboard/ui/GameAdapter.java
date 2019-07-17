package com.mnirwing.wizardscoreboard.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Player;
import com.mnirwing.wizardscoreboard.data.Round;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameHolder> {

    private static final String TAG = "GameAdapter";
    private Context context;

    private List<Player> playersInGame;
    private List<Round> rounds;

    private boolean displayGuessesInCurrentRound;

    private DataHolder data = DataHolder.getInstance();

    public GameAdapter(List<Player> players, Context context) {
        this.context = context;
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
        Log.d(TAG, "onBindViewHolder: Pos: " + position);

        Round currentRound = rounds.get(position);
        holder.textViewRound.setText(Integer.toString(position + 1));

        for (int i = 0; i < playersInGame.size(); i++) {
            String guessToDisplay =
                    (isPositionCurrentRound(position) && displayGuessesInCurrentRound)
                            || !isPositionCurrentRound(position) ?
                            Integer.toString(currentRound.getMoves().get(i).getGuess()) : "-";
            String scoreToDisplay =
                    !isPositionCurrentRound(position) ?
                            Integer.toString(currentRound.getMoves().get(i).getTotalScore()) : "-";
            holder.textViewPlayerGuesses[i].setText(guessToDisplay);
            holder.textViewPlayerScores[i].setText(scoreToDisplay);
        }

        holder.itemView.setBackgroundColor(context.getResources().getColor(
                position % 2 == 0 ? R.color.colorAlternatingRowNormal
                        : R.color.colorAlternatingRowHighlight, null));

    }

    private boolean isPositionCurrentRound(int position) {
        return (position == rounds.size() - 1);
    }

    @Override
    public int getItemCount() {
        return rounds.size();
    }

    public void notifyCurrentRoundGuessUpdated() {
        displayGuessesInCurrentRound = true;
        notifyItemChanged(rounds.size() - 1);
    }

    public void notifyCurrentRoundUpdated() {
        displayGuessesInCurrentRound = false;
        Log.d(TAG, "notifyCurrentRoundUpdated: Pos: " + (rounds.size() - 1));
        notifyItemChanged(rounds.size() - 1);
    }

    public void notifyRoundAdded() {
        displayGuessesInCurrentRound = false;
        Log.d(TAG, "notifyRoundAdded: Size:" + (rounds.size() - 1));
        notifyItemInserted(rounds.size() - 1);
    }

    class GameHolder extends RecyclerView.ViewHolder {

        private TextView textViewRound;

        private TextView[] textViewPlayerGuesses;
        private TextView[] textViewPlayerScores;

        public GameHolder(@NonNull View itemView) {
            super(itemView);
            textViewRound = itemView.findViewById(R.id.textViewRound);
            textViewPlayerGuesses = new TextView[playersInGame.size()];
            textViewPlayerScores = new TextView[playersInGame.size()];

            textViewPlayerGuesses[0] = itemView.findViewById(R.id.textViewPlayer1Guess);
            textViewPlayerScores[0] = itemView.findViewById(R.id.textViewPlayer1Score);

            textViewPlayerGuesses[1] = itemView.findViewById(R.id.textViewPlayer2Guess);
            textViewPlayerScores[1] = itemView.findViewById(R.id.textViewPlayer2Score);

            textViewPlayerGuesses[2] = itemView.findViewById(R.id.textViewPlayer3Guess);
            textViewPlayerScores[2] = itemView.findViewById(R.id.textViewPlayer3Score);

            textViewPlayerGuesses[3] = itemView.findViewById(R.id.textViewPlayer4Guess);
            textViewPlayerScores[3] = itemView.findViewById(R.id.textViewPlayer4Score);

            textViewPlayerGuesses[4] = itemView.findViewById(R.id.textViewPlayer5Guess);
            textViewPlayerScores[4] = itemView.findViewById(R.id.textViewPlayer5Score);

            textViewPlayerGuesses[5] = itemView.findViewById(R.id.textViewPlayer6Guess);
            textViewPlayerScores[5] = itemView.findViewById(R.id.textViewPlayer6Score);
        }
    }
}
