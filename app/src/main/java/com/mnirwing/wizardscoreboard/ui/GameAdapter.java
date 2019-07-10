package com.mnirwing.wizardscoreboard.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.Move;
import com.mnirwing.wizardscoreboard.data.Player;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameHolder> {


    private Player[] playersInGame;
    private List<Move> moves;


    public GameAdapter(Player[] players) {
        this.playersInGame = players;
    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        switch (playersInGame.length) {
            case 4:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_game_4player_listitem, parent, false);
                return new GameHolder(itemView);
            case 6:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_game_listitem, parent, false);
                return new GameHolder6Player(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_game_4player_listitem, parent, false);
                return new GameHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GameHolder holder, int position) {
        //if(holder instanceof GameHolder6Player)

        Move currentMove = moves.get(position);
        // holder.textViewRound.setText(currentRound.getRound());
    }

    @Override
    public int getItemCount() {
        return 0;
//        return moves.size();
    }

    public void addMove(Move move) {
        this.moves.add(move);
        notifyDataSetChanged();
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
        }
    }

    class GameHolder6Player extends GameHolder {
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

        public GameHolder6Player(@NonNull View itemView) {
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
