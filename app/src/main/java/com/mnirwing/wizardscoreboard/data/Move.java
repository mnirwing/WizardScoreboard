package com.mnirwing.wizardscoreboard.data;

import java.util.UUID;

public class Move {

  private UUID id;

  private UUID playerId;

  private UUID gameId;

  private int guess;

  private int score;

  private int totalScore;

  public Move(UUID playerId, UUID gameId, int guess) {
    this.playerId = playerId;
    this.gameId = gameId;
    this.guess = guess;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setScore(int score) {
    this.score = score;
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

  public int getScore() {
    return score;
  }

  public int getTotalScore() {
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
