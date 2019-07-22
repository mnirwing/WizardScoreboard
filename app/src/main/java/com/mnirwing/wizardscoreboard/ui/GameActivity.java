package com.mnirwing.wizardscoreboard.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mnirwing.wizardscoreboard.R;
import com.mnirwing.wizardscoreboard.data.DataHolder;
import com.mnirwing.wizardscoreboard.data.Game;
import com.mnirwing.wizardscoreboard.data.Move;
import com.mnirwing.wizardscoreboard.data.Player;
import com.mnirwing.wizardscoreboard.data.Round;
import com.mnirwing.wizardscoreboard.ui.BidOrTrickDialog.BidOrTrickDialogListener;
import com.mnirwing.wizardscoreboard.ui.GameAdapter.OnRoundClickListener;
import java.util.List;


public class GameActivity extends AppCompatActivity implements BidOrTrickDialogListener,
        OnRoundClickListener {

    private static final String TAG = "GameActivity";
    private MenuItem editAction;

    private TextView[] textViewGamePlayers;

    private Button buttonGameBid;
    private Button buttonGameTricks;

    private List<Player> playersInGame;
    private Game game;

    GameAdapter adapter;

    private boolean isBiddingPhaseDone;
    private boolean showTrickDialogAfterBidDialogIsDone;
    private int editedRoundIndex;
    private int currentlyHighlightedRound = -1;

    private DataHolder data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        data = DataHolder.getInstance();

        game = data.getCurrentGame();
        playersInGame = data.getPlayersById(game.getPlayerIds());

        initialiseTextFields();
        setTextFieldPlayerNames();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_game);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new GameAdapter(playersInGame, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        if (game.getRounds().size() == 0 || game.getRounds() == null) {
            addEmptyRound();
        }

        buttonGameBid.setOnClickListener(view -> {
            BidOrTrickDialog bidOrTrickDialog = new BidOrTrickDialog(true,
                    game.getRounds().size() - 1, playersInGame);
            bidOrTrickDialog.show(getSupportFragmentManager(), "bid dialog");
        });

        buttonGameTricks.setOnClickListener(view -> {
            BidOrTrickDialog bidOrTrickDialog = new BidOrTrickDialog(false,
                    game.getRounds().size() - 1, playersInGame);
            bidOrTrickDialog.show(getSupportFragmentManager(), "trick dialog");
        });

        if (!isBiddingPhaseDone) {
            buttonGameTricks.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_game, menu);
        editAction = menu.getItem(0);
        editAction.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            if (currentlyHighlightedRound == -1) {
                return true;
            }
            showBidDialogWithValues(game.getRoundGuessValues(currentlyHighlightedRound),
                    currentlyHighlightedRound);
            showTrickDialogAfterBidDialogIsDone = true;
            editedRoundIndex = currentlyHighlightedRound;
        } else {
            Log.d(TAG, "onOptionsItemSelected: not found");
        }
        return true;
    }

    @Override
    public boolean onRoundClick(int position, boolean selected) {
        if (position != game.getRounds().size() - 1 && selected) {
            currentlyHighlightedRound = position;
        } else {
            currentlyHighlightedRound = -1;
        }
        editAction.setVisible(selected);
        return false;
    }

    /**
     * This method gets invoked by the {@link GameAdapter} after a long click on a list item. It
     * shows the bid dialog and presets values.
     *
     * @param position Position of the clicked item.
     */
    @Override
    public boolean onRoundLongClick(int position) {
        if ((game.getRounds().size() - 1) == position) {
            return true;
        }
        showBidDialogWithValues(game.getRoundGuessValues(position), position);
        showTrickDialogAfterBidDialogIsDone = true;
        editedRoundIndex = position;
        return true;
    }

    @Override
    public void applyBidsOrTricks(boolean dialogWasInBidMode, boolean dialogWasInEditMode,
            int roundIndex, List<Integer> values) {
        if (dialogWasInBidMode) {
            isBiddingPhaseDone = true;
            buttonGameTricks.setEnabled(true);

            for (int i = 0; i < game.getRounds().get(roundIndex).getMoves().size(); i++) {
                game.getRounds().get(roundIndex).getMoves().get(i).setGuess(values.get(i));
            }
            adapter.notifyRoundGuessUpdated(roundIndex);
            if (showTrickDialogAfterBidDialogIsDone) {
                showTrickDialogWithValues(game.getRoundTrickValues(editedRoundIndex),
                        editedRoundIndex);
                showTrickDialogAfterBidDialogIsDone = false;
            }
        } else {
            isBiddingPhaseDone = false;
            buttonGameTricks.setEnabled(false);
            for (int i = 0; i < game.getRounds().get(roundIndex).getMoves().size(); i++) {
                game.getRounds().get(roundIndex).getMoves().get(i)
                        .setTricksAndCalculateScore(values.get(i));
            }
            game.calculateTotalScores(roundIndex);
            if (!dialogWasInEditMode) {
                adapter.notifyRoundTricksUpdated(roundIndex);
                addEmptyRound();
            } else {
                adapter.notifyTotalScoresChangedAfterRound(roundIndex);
            }
        }
    }

    private void showBidDialogWithValues(List<Integer> roundBidValues, int roundIndex) {
        BidOrTrickDialog bidOrTrickDialog = new BidOrTrickDialog(true, roundIndex, playersInGame,
                roundBidValues);
        bidOrTrickDialog.show(getSupportFragmentManager(), "trick dialog");
    }

    private void showTrickDialogWithValues(List<Integer> roundTricksValues, int roundIndex) {
        BidOrTrickDialog bidOrTrickDialog = new BidOrTrickDialog(false, roundIndex, playersInGame,
                roundTricksValues);
        bidOrTrickDialog.show(getSupportFragmentManager(), "trick dialog");
    }

    private void addEmptyRound() {
        Log.d(TAG, "addEmptyRound: ");
        Round emptyRound = new Round();
        for (int i = 0; i < playersInGame.size(); i++) {
            emptyRound.addMoves(new Move(playersInGame.get(0).getId(), game.getId(), 0));
        }
        data.getCurrentGame().addRound(emptyRound);
        adapter.notifyRoundAdded();
        highlightCurrentTurnPlayerName();
    }

    private void highlightCurrentTurnPlayerName() {
        int indexToHighlight =
                (data.getCurrentGame().getRounds().size() - 1) % playersInGame.size();
        Log.d(TAG, "highlightCurrentTurnPlayerName index: " + indexToHighlight);
        for (int i = 0; i < playersInGame.size(); i++) {
            textViewGamePlayers[i].setTypeface(null,
                    i == indexToHighlight ? Typeface.BOLD : Typeface.NORMAL);
        }
    }

    private void setTextFieldPlayerNames() {
        Log.d(TAG, "setTextFieldPlayerNames: ");
        for (int i = 0; i < playersInGame.size(); i++) {
            textViewGamePlayers[i].setText(playersInGame.get(i).getName());
        }
    }

    private void initialiseTextFields() {
        Log.d(TAG, "initialiseTextFields: ");
        textViewGamePlayers = new TextView[playersInGame.size()];
        textViewGamePlayers[0] = findViewById(R.id.textViewGamePlayer1);
        textViewGamePlayers[1] = findViewById(R.id.textViewGamePlayer2);
        textViewGamePlayers[2] = findViewById(R.id.textViewGamePlayer3);
        textViewGamePlayers[3] = findViewById(R.id.textViewGamePlayer4);
        if (playersInGame.size() >= 5) {
            textViewGamePlayers[4] = findViewById(R.id.textViewGamePlayer5);
        }
        if (playersInGame.size() == 6) {
            textViewGamePlayers[5] = findViewById(R.id.textViewGamePlayer6);
        }

        buttonGameBid = findViewById(R.id.buttonGameBid);
        buttonGameTricks = findViewById(R.id.buttonGameTricks);
    }
}
