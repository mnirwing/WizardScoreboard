package com.mnirwing.wizardscoreboard.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;
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

        highlightCurrentTurnPlayerName();
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
        if (item.getItemId() != R.id.action_edit || currentlyHighlightedRound == -1) {
            return true;
        }
        showBidDialogWithValues(game.getRoundGuessValues(currentlyHighlightedRound),
                currentlyHighlightedRound);
        showTrickDialogAfterBidDialogIsDone = true;
        editedRoundIndex = currentlyHighlightedRound;
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
        for (int i = 0; i < playersInGame.size(); i++) {
            textViewGamePlayers[i].setTypeface(null,
                    i == indexToHighlight ? Typeface.BOLD : Typeface.NORMAL);
        }
    }

    private void setTextFieldPlayerNames() {
        for (int i = 0; i < playersInGame.size(); i++) {
            textViewGamePlayers[i].setText(playersInGame.get(i).getName());
        }
    }

    private void initialiseTextFields() {
        Guideline[] guidelines = new Guideline[6];
        guidelines[0] = findViewById(R.id.guideline_game_01);
        guidelines[1] = findViewById(R.id.guideline_game_02);
        guidelines[2] = findViewById(R.id.guideline_game_03);
        guidelines[3] = findViewById(R.id.guideline_game_04);
        guidelines[4] = findViewById(R.id.guideline_game_05);
        guidelines[5] = findViewById(R.id.guideline_game_06);

        View[] dividerLines = new View[6];
        dividerLines[0] = findViewById(R.id.divider_line_game_01);
        dividerLines[1] = findViewById(R.id.divider_line_game_02);
        dividerLines[2] = findViewById(R.id.divider_line_game_03);
        dividerLines[3] = findViewById(R.id.divider_line_game_04);
        dividerLines[4] = findViewById(R.id.divider_line_game_05);
        dividerLines[5] = findViewById(R.id.divider_line_game_06);

        textViewGamePlayers = new TextView[6];
        textViewGamePlayers[0] = findViewById(R.id.textViewGamePlayer1);
        textViewGamePlayers[1] = findViewById(R.id.textViewGamePlayer2);
        textViewGamePlayers[2] = findViewById(R.id.textViewGamePlayer3);
        textViewGamePlayers[3] = findViewById(R.id.textViewGamePlayer4);
        textViewGamePlayers[4] = findViewById(R.id.textViewGamePlayer5);
        textViewGamePlayers[5] = findViewById(R.id.textViewGamePlayer6);

        if (playersInGame.size() == 5) {
            textViewGamePlayers[5].setVisibility(View.GONE);
            TypedValue outValue = new TypedValue();
            getResources().getValue(R.fraction.guideline_5_player_01, outValue, true);
            guidelines[0].setGuidelinePercent(outValue.getFloat());
            getResources().getValue(R.fraction.guideline_5_player_03, outValue, true);
            guidelines[1].setGuidelinePercent(outValue.getFloat());
            getResources().getValue(R.fraction.guideline_5_player_05, outValue, true);
            guidelines[2].setGuidelinePercent(outValue.getFloat());
            getResources().getValue(R.fraction.guideline_5_player_07, outValue, true);
            guidelines[3].setGuidelinePercent(outValue.getFloat());
            getResources().getValue(R.fraction.guideline_5_player_09, outValue, true);
            guidelines[4].setGuidelinePercent(outValue.getFloat());
            guidelines[5].setGuidelinePercent(1);
            dividerLines[5].setVisibility(View.GONE);
        }
        if (playersInGame.size() == 4) {
            textViewGamePlayers[4].setVisibility(View.GONE);
            textViewGamePlayers[5].setVisibility(View.GONE);
            TypedValue outValue = new TypedValue();
            getResources().getValue(R.fraction.guideline_4_player_01, outValue, true);
            guidelines[0].setGuidelinePercent(outValue.getFloat());
            getResources().getValue(R.fraction.guideline_4_player_03, outValue, true);
            guidelines[1].setGuidelinePercent(outValue.getFloat());
            getResources().getValue(R.fraction.guideline_4_player_05, outValue, true);
            guidelines[2].setGuidelinePercent(outValue.getFloat());
            getResources().getValue(R.fraction.guideline_4_player_07, outValue, true);
            guidelines[3].setGuidelinePercent(outValue.getFloat());

            guidelines[4].setGuidelinePercent(1);
            guidelines[5].setVisibility(View.GONE);

            dividerLines[4].setVisibility(View.GONE);
            dividerLines[5].setVisibility(View.GONE);
        }

        buttonGameBid = findViewById(R.id.buttonGameBid);
        buttonGameTricks = findViewById(R.id.buttonGameTricks);
    }
}
