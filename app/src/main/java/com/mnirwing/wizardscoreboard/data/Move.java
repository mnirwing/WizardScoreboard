package com.mnirwing.wizardscoreboard.data;

import java.util.UUID;

public class Move {

    private UUID id;

    private UUID playerId;

    private UUID gameId;

    private int guess;

    private Integer tricks;

    private int score;

    private Integer totalScore;

    public Move(UUID playerId, UUID gameId, int guess) {
        this.playerId = playerId;
        this.gameId = gameId;
        this.guess = guess;
        this.tricks = null;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTricksAndCalculateScore(int tricks) {
        this.tricks = tricks;
        if (guess == tricks) {
            score = tricks * 10 + 20;
        } else {
            score = Math.abs(guess - tricks) * -10;
        }
    }

    public Integer getTricks() {
        return tricks;
    }

    public boolean isMoveCompleted() {
        return tricks != null;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public int getScore() {
        return score;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        return "Move{" +
                ", pId=" + playerId +
                ", gId=" + gameId +
                ", " + guess +
                "/ " + score +
                '}';
    }
}
