package com.mnirwing.wizardscoreboard.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Game implements Serializable {

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

  public void addRound(Move...moves) {
    Round round = new Round();
    round.addMoves(moves);
    this.rounds.add(round);
  }

  public Round getCurrentRound() {
    return this.rounds.get(rounds.size() - 1);
  }

  public List<Round> getRounds() {
    return rounds;
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
