package com.mnirwing.wizardscoreboard.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
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

    private OnRoundClickListener onRoundClickListener;

    private DataHolder data = DataHolder.getInstance();

    public GameAdapter(List<Player> players, Context context,
            OnRoundClickListener onRoundClickListener) {
        this.context = context;
        this.playersInGame = players;
        this.onRoundClickListener = onRoundClickListener;
        this.rounds = data.getCurrentGame().getRounds();
    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Players:  " + playersInGame.size());
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_game_6player_listitem, parent, false);
        return new GameHolder(itemView, onRoundClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GameHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Pos: " + position);

        Round roundAtPosition = rounds.get(position);
        holder.textViewRound.setText(Integer.toString(position + 1));

        for (int i = 0; i < playersInGame.size(); i++) {
            String guessToDisplay =
                    (isPositionCurrentRound(position) && displayGuessesInCurrentRound)
                            || !isPositionCurrentRound(position) ?
                            Integer.toString(roundAtPosition.getMoves().get(i).getGuess()) : "-";
            String totalScoreToDisplay = !isPositionCurrentRound(position) ? Integer
                    .toString(roundAtPosition.getMoves().get(i).getTotalScore()) : "-";
            //String scoreToDisplay = Integer.toString(currentRound.getMoves().get(i).getTotalScore());
            holder.textViewPlayerGuesses[i].setText(guessToDisplay);
            holder.textViewPlayerScores[i].setText(totalScoreToDisplay);
            holder.textViewPlayerScores[i]
                    .setTextColor(context.getColor(roundAtPosition.getMoves().get(i).getScore() >= 0
                            ? R.color.colorPositiveScore : R.color.colorNegativeScore));
        }

        holder.itemView.setBackgroundColor(context.getColor(
                position % 2 == 0 ? R.color.colorAlternatingRowNormal
                        : R.color.colorAlternatingRowHighlight));

    }

    private boolean isPositionCurrentRound(int position) {
        return position == (rounds.size() - 1);
    }

    @Override
    public int getItemCount() {
        return rounds.size();
    }

    public void notifyRoundGuessUpdated(int roundIndex) {
        displayGuessesInCurrentRound = true;
        notifyItemChanged(roundIndex);
    }

    public void notifyRoundTricksUpdated(int roundIndex) {
        Log.d(TAG, "notifyCurrentRoundUpdated: Pos: " + (rounds.size() - 1));
        notifyItemChanged(roundIndex);
    }

    public void notifyRoundAdded() {
        displayGuessesInCurrentRound = false;
        Log.d(TAG, "notifyRoundAdded: Size:" + (rounds.size() - 1));
        notifyItemInserted(rounds.size() - 1);
    }

    public void notifyTotalScoresChangedAfterRound(int roundIndex) {
        notifyItemRangeChanged(roundIndex, rounds.size() - roundIndex);
    }

    class GameHolder extends RecyclerView.ViewHolder implements OnLongClickListener {

        private TextView textViewRound;

        private TextView[] textViewPlayerGuesses;
        private TextView[] textViewPlayerScores;

        private OnRoundClickListener onRoundClickListener;

        public GameHolder(@NonNull View itemView, OnRoundClickListener onRoundClickListener) {
            super(itemView);
            this.onRoundClickListener = onRoundClickListener;
            itemView.setOnLongClickListener(this);
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

        @Override
        public boolean onLongClick(View v) {
            Log.d(TAG, "onLongClick: GameAdapter" + getAdapterPosition());
            onRoundClickListener.onRoundLongClick(getAdapterPosition());
            return false;
        }
    }

    public interface OnRoundClickListener {

        boolean onRoundLongClick(int position);
    }
}
