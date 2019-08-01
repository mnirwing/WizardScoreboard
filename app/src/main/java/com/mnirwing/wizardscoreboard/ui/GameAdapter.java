package com.mnirwing.wizardscoreboard.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.Player;
import com.mnirwing.wizardscoreboard.data.Round;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameHolder> {

    private static final String TAG = "GameAdapter";
    private Context context;

    private List<Player> playersInGame;
    private List<Round> rounds;

    private boolean displayGuessesInCurrentRound;
    private boolean finalRound;

    private OnRoundClickListener onRoundClickListener;

    private int selectedPosition = RecyclerView.NO_POSITION;

    public GameAdapter(List<Player> players, Context context,
            OnRoundClickListener onRoundClickListener, List<Round> rounds) {
        this.context = context;
        this.playersInGame = players;
        this.onRoundClickListener = onRoundClickListener;
        this.rounds = rounds;
    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Players:  " + playersInGame.size());
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_game_listitem, parent, false);

        initializeGuidelines(itemView);

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
            String totalScoreToDisplay = !isPositionCurrentRound(position) || finalRound ?
                    Integer.toString(roundAtPosition.getMoves().get(i).getTotalScore()) : "-";
            holder.textViewPlayerGuesses[i].setText(guessToDisplay);
            holder.textViewPlayerScores[i].setText(totalScoreToDisplay);
            holder.textViewPlayerScores[i]
                    .setTextColor(context.getColor(roundAtPosition.getMoves().get(i).getScore() >= 0
                            ? R.color.colorPositiveScore : R.color.colorNegativeScore));
            if(selectedPosition == position){
                holder.textViewRound.setTextColor(Color.BLACK);
                holder.textViewPlayerScores[i].setTextColor(Color.BLACK);
                holder.textViewPlayerGuesses[i].setTextColor(Color.BLACK);
            }

        }

        if (selectedPosition != position) {
            holder.itemView
                    .setBackgroundColor(context.getColor(
                            position % 2 == 0 ? R.color.colorAlternatingRowNormal
                                    : R.color.colorAlternatingRowHighlight));
        } else {
            holder.itemView
                    .setBackgroundColor(context.getColor(R.color.colorRedPrimaryLight));
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

    public void notifyFinalRound() {
        finalRound = true;
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
            textViewPlayerGuesses = new TextView[6];
            textViewPlayerScores = new TextView[6];

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

            if (playersInGame.size() == 4) {
                textViewPlayerGuesses[4].setVisibility(View.GONE);
                textViewPlayerScores[4].setVisibility(View.GONE);
                textViewPlayerGuesses[5].setVisibility(View.GONE);
                textViewPlayerScores[5].setVisibility(View.GONE);
            }
            if (playersInGame.size() == 5) {
                textViewPlayerGuesses[5].setVisibility(View.GONE);
                textViewPlayerScores[5].setVisibility(View.GONE);
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

        boolean onRoundClick(int position, boolean rowAlreadySelected);

    }

    private void initializeGuidelines(View itemView) {
        Guideline[] guidelines = new Guideline[12];
        View[] dividerLines = new View[12];
        guidelines[0] = itemView.findViewById(R.id.guideline_adapter_01);
        guidelines[1] = itemView.findViewById(R.id.guideline_adapter_02);
        guidelines[2] = itemView.findViewById(R.id.guideline_adapter_03);
        guidelines[3] = itemView.findViewById(R.id.guideline_adapter_04);
        guidelines[4] = itemView.findViewById(R.id.guideline_adapter_05);
        guidelines[5] = itemView.findViewById(R.id.guideline_adapter_06);
        guidelines[6] = itemView.findViewById(R.id.guideline_adapter_07);
        guidelines[7] = itemView.findViewById(R.id.guideline_adapter_08);
        guidelines[8] = itemView.findViewById(R.id.guideline_adapter_09);
        guidelines[9] = itemView.findViewById(R.id.guideline_adapter_10);
        guidelines[10] = itemView.findViewById(R.id.guideline_adapter_11);
        guidelines[11] = itemView.findViewById(R.id.guideline_adapter_12);

        dividerLines[0] = itemView.findViewById(R.id.view01);
        dividerLines[1] = itemView.findViewById(R.id.view02);
        dividerLines[2] = itemView.findViewById(R.id.view03);
        dividerLines[3] = itemView.findViewById(R.id.view04);
        dividerLines[4] = itemView.findViewById(R.id.view05);
        dividerLines[5] = itemView.findViewById(R.id.view06);
        dividerLines[6] = itemView.findViewById(R.id.view07);
        dividerLines[7] = itemView.findViewById(R.id.view08);
        dividerLines[8] = itemView.findViewById(R.id.view09);
        dividerLines[9] = itemView.findViewById(R.id.view10);
        dividerLines[10] = itemView.findViewById(R.id.view11);
        dividerLines[11] = itemView.findViewById(R.id.view12);

        TypedValue outValue = new TypedValue();
        switch (playersInGame.size()) {
            case 3:
                context.getResources().getValue(R.fraction.guideline_3_player_01, outValue, true);
                guidelines[0].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_3_player_02, outValue, true);
                guidelines[1].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_3_player_03, outValue, true);
                guidelines[2].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_3_player_04, outValue, true);
                guidelines[3].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_3_player_05, outValue, true);
                guidelines[4].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_3_player_06, outValue, true);
                guidelines[5].setGuidelinePercent(outValue.getFloat());

                guidelines[6].setGuidelinePercent(1);
                guidelines[7].setVisibility(View.GONE);
                guidelines[8].setVisibility(View.GONE);
                guidelines[9].setVisibility(View.GONE);
                guidelines[9].setVisibility(View.GONE);
                guidelines[10].setVisibility(View.GONE);
                guidelines[11].setVisibility(View.GONE);

                dividerLines[6].setVisibility(View.GONE);
                dividerLines[7].setVisibility(View.GONE);
                dividerLines[8].setVisibility(View.GONE);
                dividerLines[9].setVisibility(View.GONE);
                dividerLines[10].setVisibility(View.GONE);
                dividerLines[11].setVisibility(View.GONE);
                break;
            case 4:
                context.getResources().getValue(R.fraction.guideline_4_player_01, outValue, true);
                guidelines[0].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_4_player_02, outValue, true);
                guidelines[1].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_4_player_03, outValue, true);
                guidelines[2].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_4_player_04, outValue, true);
                guidelines[3].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_4_player_05, outValue, true);
                guidelines[4].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_4_player_06, outValue, true);
                guidelines[5].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_4_player_07, outValue, true);
                guidelines[6].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_4_player_08, outValue, true);
                guidelines[7].setGuidelinePercent(outValue.getFloat());
                guidelines[8].setGuidelinePercent(1);
                guidelines[9].setVisibility(View.GONE);
                guidelines[10].setVisibility(View.GONE);
                guidelines[11].setVisibility(View.GONE);

                dividerLines[8].setVisibility(View.GONE);
                dividerLines[9].setVisibility(View.GONE);
                dividerLines[10].setVisibility(View.GONE);
                dividerLines[11].setVisibility(View.GONE);
                break;
            case 5:
                context.getResources().getValue(R.fraction.guideline_5_player_01, outValue, true);
                guidelines[0].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_5_player_02, outValue, true);
                guidelines[1].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_5_player_03, outValue, true);
                guidelines[2].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_5_player_04, outValue, true);
                guidelines[3].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_5_player_05, outValue, true);
                guidelines[4].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_5_player_06, outValue, true);
                guidelines[5].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_5_player_07, outValue, true);
                guidelines[6].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_5_player_08, outValue, true);
                guidelines[7].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_5_player_09, outValue, true);
                guidelines[8].setGuidelinePercent(outValue.getFloat());
                context.getResources().getValue(R.fraction.guideline_5_player_10, outValue, true);
                guidelines[9].setGuidelinePercent(outValue.getFloat());
                guidelines[10].setGuidelinePercent(1);
                guidelines[11].setVisibility(View.GONE);

                dividerLines[10].setVisibility(View.GONE);
                dividerLines[11].setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
