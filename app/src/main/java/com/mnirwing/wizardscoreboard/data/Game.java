package com.mnirwing.wizardscoreboard.data;

import android.util.Log;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Game implements Serializable {

    private static final String TAG = "Game";

    private UUID id;

    private UUID player1Id;
    private UUID player2Id;
    private UUID player3Id;
    private UUID player4Id;
    private UUID player5Id;
    private UUID player6Id;

    private Date createdAt;

    private boolean isFinished;
    private boolean isCurrentGame;

    private List<Round> rounds = new ArrayList<>();

    public Game(UUID player1Id, UUID player2Id, UUID player3Id, UUID player4Id, UUID player5Id,
            UUID player6Id) {
        this.id = UUID.randomUUID();
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.player3Id = player3Id;
        this.player4Id = player4Id;
        this.player5Id = player5Id;
        this.player6Id = player6Id;
        this.isFinished = false;
        this.isCurrentGame = false;
    }

    public Game(List<Player> players) {
        this.id = UUID.randomUUID();
        this.player1Id = players.get(0).getId();
        this.player2Id = players.get(1).getId();
        this.player3Id = players.get(2).getId();
        this.player4Id = players.get(3).getId();
        this.player5Id = players.get(4).getId();
        this.player6Id = players.get(5).getId();
        this.isFinished = false;
        this.isCurrentGame = false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<UUID> getPlayerIds() {
        return Arrays.asList(player1Id, player2Id, player3Id, player4Id, player5Id, player6Id);
    }

    public UUID getPlayer1Id() {
        return player1Id;
    }

    public UUID getPlayer2Id() {
        return player2Id;
    }

    public UUID getPlayer3Id() {
        return player3Id;
    }

    public UUID getPlayer4Id() {
        return player4Id;
    }

    public UUID getPlayer5Id() {
        return player5Id;
    }

    public UUID getPlayer6Id() {
        return player6Id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void addRound(Move... moves) {
        Round round = new Round();
        round.addMoves(moves);
        this.rounds.add(round);
    }

    public Round getCurrentRound() {
        return this.rounds.get(rounds.size() - 1);
    }

    public void calculateTotalScores(int calculateRoundStartIndex) {
        if (this.rounds.size() == 0) {
            return;
        }

        if (calculateRoundStartIndex == 0) {
            for (Move move : rounds.get(0).getMoves()) {
                move.setTotalScore(move.getScore());
            }
            calculateRoundStartIndex = 1;
        }

        for (int i = calculateRoundStartIndex; i < rounds.size(); i++) {
            for (int j = 0; j < rounds.get(i).getMoves().size(); j++) {
                int previousRoundTotalScore = rounds.get(i - 1).getMoves().get(j).getTotalScore();
                int currentRoundScore = rounds.get(i).getMoves().get(j).getScore();
                rounds.get(i).getMoves().get(j)
                        .setTotalScore(previousRoundTotalScore + currentRoundScore);
            }
        }
    }

    /**
     * Updates the total score of every round.
     */
    public void calculateAllTotalScores() {
        calculateTotalScores(0);
    }

    public void calculateCurrentRoundTotalScore() {
        calculateTotalScores(rounds.size() - 1);
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public List<Integer> getRoundGuessValues(int position) {
        List<Integer> roundGuessValues = new ArrayList<>();
        Round clickedRound = rounds.get(position);
        for (Move move : clickedRound.getMoves()) {
            roundGuessValues.add(move.getGuess());
        }
        return roundGuessValues;
    }

    public List<Integer> getRoundScoreValues(int position) {
        List<Integer> roundScoreValues = new ArrayList<>();
        Round clickedRound = rounds.get(position);
        for (Move move : clickedRound.getMoves()) {
            roundScoreValues.add(move.getScore());
        }
        return roundScoreValues;
    }

    public void addRound(Round round) {
        this.rounds.add(round);
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isCurrentGame() {
        return isCurrentGame;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void setCurrentGame(boolean isCurrentGame) {
        this.isCurrentGame = isCurrentGame;
    }
}
