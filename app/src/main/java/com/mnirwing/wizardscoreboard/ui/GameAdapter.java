package com.mnirwing.wizardscoreboard.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

    private int selectedPosition = RecyclerView.NO_POSITION;
    private boolean clickedOnce = false;

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
        int layoutId;
        switch (playersInGame.size()) {
            case 4:
                layoutId = R.layout.layout_game_4player_listitem;
                break;
            case 5:
                layoutId = R.layout.layout_game_5player_listitem;
                break;
            default:
                layoutId = R.layout.layout_game_6player_listitem;
        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
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

        if (selectedPosition != position) {
            holder.itemView
                    .setBackgroundColor(context.getColor(
                            position % 2 == 0 ? R.color.colorAlternatingRowNormal
                                    : R.color.colorAlternatingRowHighlight));
        } else {
            holder.itemView
                    .setBackgroundColor(context.getColor(R.color.colorHighlightedRow));
        }
    }

    private boolean isPositionCurrentRound(int position) {
        return position == (rounds.size() - 1);
    }

    @Override
    public int getItemCount() {
        return rounds.size();
    }

    public void notifyRoundGuessUpdated(int roundIndex) {
        Log.d(TAG, "notifyRoundGuessUpdated: " + roundIndex);
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
        notifyItemRangeChanged(roundIndex, rounds.size() - 1 - roundIndex);
    }

    class GameHolder extends RecyclerView.ViewHolder implements OnClickListener,
            OnLongClickListener {

        private TextView textViewRound;

        private TextView[] textViewPlayerGuesses;
        private TextView[] textViewPlayerScores;

        private OnRoundClickListener onRoundClickListener;

        public GameHolder(@NonNull View itemView, OnRoundClickListener onRoundClickListener) {
            super(itemView);
            this.onRoundClickListener = onRoundClickListener;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
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

            if (playersInGame.size() > 4) {
                textViewPlayerGuesses[4] = itemView.findViewById(R.id.textViewPlayer5Guess);
                textViewPlayerScores[4] = itemView.findViewById(R.id.textViewPlayer5Score);
            }

            if (playersInGame.size() > 5) {
                textViewPlayerGuesses[5] = itemView.findViewById(R.id.textViewPlayer6Guess);
                textViewPlayerScores[5] = itemView.findViewById(R.id.textViewPlayer6Score);
            }
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
                onRoundClickListener.onRoundClick(selectedPosition, false);
            } else {
                notifyItemChanged(selectedPosition);
                onRoundClickListener.onRoundClick(selectedPosition, true);
            }
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

        boolean onRoundClick(int position, boolean selected);
    }
}
