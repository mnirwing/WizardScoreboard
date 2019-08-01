package com.mnirwing.wizardscoreboard.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Every game consists of 3 to 6 players, using an UUID as reference. A game contains a list of
 * {@link Round}. The game class is mainly responsible for calculating the total scores and
 * determining the winner.
 */
public class Game implements Serializable {

    private static final String TAG = "Game";

    private UUID id;

    private UUID player1Id;
    private UUID player2Id;
    private UUID player3Id;
    private UUID player4Id;
    private UUID player5Id;
    private UUID player6Id;

    private static final int ROUNDS_TO_PLAY_WITH_3_PLAYERS = 20;
    private static final int ROUNDS_TO_PLAY_WITH_4_PLAYERS = 15;
    private static final int ROUNDS_TO_PLAY_WITH_5_PLAYERS = 12;
    private static final int ROUNDS_TO_PLAY_WITH_6_PLAYERS = 10;

    private Date createdAt;

    private int numberOfPlayers;

    private List<Round> rounds = new ArrayList<>();

    public Game(List<Player> players) {
        this.id = UUID.randomUUID();
        this.player1Id = players.get(0).getId();
        this.player2Id = players.get(1).getId();
        this.player3Id = players.get(2).getId();
        this.numberOfPlayers = 3;
        if (players.size() >= 4) {
            this.player4Id = players.get(3).getId();
            this.numberOfPlayers = 4;
        }
        if (players.size() >= 5) {
            this.player5Id = players.get(4).getId();
            this.numberOfPlayers = 5;
        }
        if (players.size() == 6) {
            this.player6Id = players.get(5).getId();
            this.numberOfPlayers = 6;
        }
        this.createdAt = Calendar.getInstance().getTime();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<UUID> getPlayerIds() {
        if (player4Id == null) {
            return Arrays.asList(player1Id, player2Id, player3Id);
        }
        if (player5Id == null) {
            return Arrays.asList(player1Id, player2Id, player3Id, player4Id);
        }
        if (player6Id == null) {
            return Arrays.asList(player1Id, player2Id, player3Id, player4Id, player5Id);
        }
        return Arrays.asList(player1Id, player2Id, player3Id, player4Id, player5Id, player6Id);
    }

    public Round getCurrentRound() {
        return this.rounds.get(rounds.size() - 1);
    }

    /**
     * Calculates the total score of every round with equal or higher index than the specified start
     * index.
     */
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

    public List<Integer> getRoundBidValues(int position) {
        List<Integer> roundGuessValues = new ArrayList<>();
        Round clickedRound = rounds.get(position);
        for (Move move : clickedRound.getMoves()) {
            roundGuessValues.add(move.getGuess());
        }
        return roundGuessValues;
    }

    public List<Integer> getRoundTrickValues(int position) {
        List<Integer> roundTrickValues = new ArrayList<>();
        Round clickedRound = rounds.get(position);
        for (Move move : clickedRound.getMoves()) {
            roundTrickValues.add(move.getTricks());
        }
        return roundTrickValues;
    }

    public List<Integer> getRoundScoreValues(int position) {
        List<Integer> roundScoreValues = new ArrayList<>();
        Round clickedRound = rounds.get(position);
        for (Move move : clickedRound.getMoves()) {
            roundScoreValues.add(move.getScore());
        }
        return roundScoreValues;
    }

    /**
     * Adds a round to the game. Returns false if the round can't be added due to the game being
     * already over.
     */
    public boolean addRound(Round round) {
        if (isFinished()) {
            return false;
        }
        this.rounds.add(round);
        return true;
    }

    public boolean isFinished() {
        switch (numberOfPlayers) {
            case 3:
                if (rounds.size() == ROUNDS_TO_PLAY_WITH_3_PLAYERS) {
                    return rounds.get(rounds.size() - 1).getMoves().get(0).isMoveCompleted();
                }
                break;
            case 4:
                if (rounds.size() == ROUNDS_TO_PLAY_WITH_4_PLAYERS) {
                    return rounds.get(rounds.size() - 1).getMoves().get(0).isMoveCompleted();
                }
                break;
            case 5:
                if (rounds.size() == ROUNDS_TO_PLAY_WITH_5_PLAYERS) {
                    return rounds.get(rounds.size() - 1).getMoves().get(0).isMoveCompleted();
                }
                break;
            case 6:
                if (rounds.size() == ROUNDS_TO_PLAY_WITH_6_PLAYERS) {
                    return rounds.get(rounds.size() - 1).getMoves().get(0).isMoveCompleted();
                }
                break;
        }
        return false;
    }

    public List<UUID> getWinner() {
        if (!isFinished()) {
            return null;
        }
        List<UUID> winnerIds = new ArrayList<>();
        int currentHighestScore = Integer.MIN_VALUE;

        for (int i = 0; i < numberOfPlayers; i++) {
            int currentPlayerTotalScore = rounds.get(rounds.size() - 1).getMoves().get(i)
                    .getTotalScore();
            UUID currentPlayerId = rounds.get(rounds.size() - 1).getMoves().get(i).getPlayerId();

            if (currentPlayerTotalScore > currentHighestScore) {
                currentHighestScore = currentPlayerTotalScore;
                winnerIds.clear();
                winnerIds.add(currentPlayerId);
            } else if (currentPlayerTotalScore == currentHighestScore) {
                winnerIds.add(currentPlayerId);
            }
        }
        return winnerIds;
    }
}
