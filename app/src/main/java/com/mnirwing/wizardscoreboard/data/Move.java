package com.mnirwing.wizardscoreboard.data;

public class Move {

  private int id;

  private int playerId;

  private int gameId;

  private int guess;

  private int score;

  private int round;

  public Move(int playerId, int gameId, int guess, int round) {
    this.playerId = playerId;
    this.gameId = gameId;
    this.round = round;
    this.guess = guess;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getId() {
    return id;
  }

  public int getPlayerId() {
    return playerId;
  }

  public int getGameId() {
    return gameId;
  }

  public int getGuess() {
    return guess;
  }

  public int getScore() {
    return score;
  }

  public int getRound() {
    return round;
  }
}
