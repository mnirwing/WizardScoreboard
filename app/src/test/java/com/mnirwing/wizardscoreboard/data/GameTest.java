package com.mnirwing.wizardscoreboard.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private Player player1;
    private Player player2;
    private Player player3;

    @Before
    public void setUp() throws Exception {
        player1 = new Player("Test1", "t1");
        player2 = new Player("Test2", "t2");
        player3 = new Player("Test3", "t3");
    }

    @Test
    public void calculateTotalScores() {
        Game gameThreePlayers = new Game(Arrays.asList(player1, player2, player3));

        Round round1 = new Round();
        Move move1R1 = new Move(player1.getId(), gameThreePlayers.getId(), 3);
        Move move2R1 = new Move(player2.getId(), gameThreePlayers.getId(), 2);
        Move move3R1 = new Move(player3.getId(), gameThreePlayers.getId(), 1);

        move1R1.setTricksAndCalculateScore(3);
        move2R1.setTricksAndCalculateScore(1);
        move3R1.setTricksAndCalculateScore(2);

        round1.addMoves(move1R1, move2R1, move3R1);
        gameThreePlayers.addRound(round1);

        Round round2 = new Round();
        Move move1R2 = new Move(player1.getId(), gameThreePlayers.getId(), 0);
        Move move2R2 = new Move(player2.getId(), gameThreePlayers.getId(), 1);
        Move move3R2 = new Move(player3.getId(), gameThreePlayers.getId(), 1);

        move1R2.setTricksAndCalculateScore(0);
        move2R2.setTricksAndCalculateScore(1);
        move3R2.setTricksAndCalculateScore(2);

        round2.addMoves(move1R2, move2R2, move3R2);
        gameThreePlayers.addRound(round2);

        Round round3 = new Round();
        Move move1R3 = new Move(player1.getId(), gameThreePlayers.getId(), 2);
        Move move2R3 = new Move(player2.getId(), gameThreePlayers.getId(), 2);
        Move move3R3 = new Move(player3.getId(), gameThreePlayers.getId(), 3);

        move1R3.setTricksAndCalculateScore(1);
        move2R3.setTricksAndCalculateScore(1);
        move3R3.setTricksAndCalculateScore(2);

        round3.addMoves(move1R3, move2R3, move3R3);
        gameThreePlayers.addRound(round3);

        // values should be null because total score has not been calculated
        assertNull(round1.getMoves().get(0).getTotalScore());
        assertNull(round1.getMoves().get(1).getTotalScore());
        assertNull(round1.getMoves().get(2).getTotalScore());

        gameThreePlayers.calculateTotalScores(0);

        assertEquals(round1.getMoves().get(0).getTotalScore().intValue(), 50);
        assertEquals(round1.getMoves().get(1).getTotalScore().intValue(), -10);
        assertEquals(round1.getMoves().get(2).getTotalScore().intValue(), -10);

        assertEquals(round2.getMoves().get(0).getTotalScore().intValue(), 70);
        assertEquals(round2.getMoves().get(1).getTotalScore().intValue(), 20);
        assertEquals(round2.getMoves().get(2).getTotalScore().intValue(), -20);

        assertEquals(round3.getMoves().get(0).getTotalScore().intValue(), 60);
        assertEquals(round3.getMoves().get(1).getTotalScore().intValue(), 10);
        assertEquals(round3.getMoves().get(2).getTotalScore().intValue(), -30);

        for (int i = 0; i < 4; i++) {
            Round round = new Round();
            Move move1 = new Move(player1.getId(), gameThreePlayers.getId(), 1);
            Move move2 = new Move(player2.getId(), gameThreePlayers.getId(), 2);
            Move move3 = new Move(player3.getId(), gameThreePlayers.getId(), 3);

            move1.setTricksAndCalculateScore(1);
            move2.setTricksAndCalculateScore(2);
            move3.setTricksAndCalculateScore(2);

            round.addMoves(move1, move2, move3);
            gameThreePlayers.addRound(round);
        }

        gameThreePlayers.calculateTotalScores(0);

        assertEquals(
                gameThreePlayers.getCurrentRound().getMoves().get(0).getTotalScore().intValue(),
                180);
        assertEquals(
                gameThreePlayers.getCurrentRound().getMoves().get(1).getTotalScore().intValue(),
                170);
        assertEquals(
                gameThreePlayers.getCurrentRound().getMoves().get(2).getTotalScore().intValue(),
                -70);

        // edit round 0 1 1   0 1 2
        gameThreePlayers.getRounds().get(1).getMoves().get(0).setGuess(0);
        gameThreePlayers.getRounds().get(1).getMoves().get(0).setTricksAndCalculateScore(2);

        gameThreePlayers.getRounds().get(1).getMoves().get(1).setGuess(2);
        gameThreePlayers.getRounds().get(1).getMoves().get(1).setTricksAndCalculateScore(2);

        gameThreePlayers.getRounds().get(1).getMoves().get(2).setGuess(2);
        gameThreePlayers.getRounds().get(1).getMoves().get(2).setTricksAndCalculateScore(2);

        gameThreePlayers.calculateAllTotalScores();

        assertEquals(
                gameThreePlayers.getCurrentRound().getMoves().get(0).getTotalScore().intValue(),
                140);
        assertEquals(
                gameThreePlayers.getCurrentRound().getMoves().get(1).getTotalScore().intValue(),
                180);
        assertEquals(
                gameThreePlayers.getCurrentRound().getMoves().get(2).getTotalScore().intValue(),
                -20);
    }


    @Test
    public void isFinished() {
        Game game = new Game(Arrays.asList(player1, player2, player3));
        for (int i = 0; i < 19; i++) {
            Round round = new Round();
            Move move1 = new Move(player1.getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move2 = new Move(player2.getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move3 = new Move(player3.getId(), game.getId(),
                    (int) (Math.random() * 4));
            move1.setTricksAndCalculateScore((int) (Math.random() * 4));
            move2.setTricksAndCalculateScore((int) (Math.random() * 4));
            move3.setTricksAndCalculateScore((int) (Math.random() * 4));
            round.addMoves(move1, move2, move3);
            game.addRound(round);
        }
        game.calculateAllTotalScores();
        assertFalse(game.isFinished());

        Round round = new Round();
        Move move1 = new Move(player1.getId(), game.getId(),
                (int) (Math.random() * 4));
        Move move2 = new Move(player2.getId(), game.getId(),
                (int) (Math.random() * 4));
        Move move3 = new Move(player3.getId(), game.getId(),
                (int) (Math.random() * 4));
        move1.setTricksAndCalculateScore((int) (Math.random() * 4));
        move2.setTricksAndCalculateScore((int) (Math.random() * 4));
        move3.setTricksAndCalculateScore((int) (Math.random() * 4));
        round.addMoves(move1, move2, move3);
        game.addRound(round);

        game.calculateAllTotalScores();
        assertTrue(game.isFinished());
    }

    @Test
    public void getWinner() {
        Game game = new Game(Arrays.asList(player1, player2, player3));
        for (int i = 0; i < 20; i++) {
            Round round = new Round();
            Move move1 = new Move(player1.getId(), game.getId(), 1);
            Move move2 = new Move(player2.getId(), game.getId(), 1);
            Move move3 = new Move(player3.getId(), game.getId(), 2);
            move1.setTricksAndCalculateScore(1);
            move2.setTricksAndCalculateScore(1);
            move3.setTricksAndCalculateScore(0);
            round.addMoves(move1, move2, move3);
            game.addRound(round);
        }
        game.calculateAllTotalScores();

        assertEquals(game.getWinner().size(), 2);
        assertEquals(game.getWinner().get(0), player1.getId());
        assertEquals(game.getWinner().get(1), player2.getId());

        game.getCurrentRound().getMoves().get(0).setTricksAndCalculateScore(0);
        game.calculateCurrentRoundTotalScore();

        assertEquals(game.getWinner().size(), 1);
        assertEquals(game.getWinner().get(0), player2.getId());
    }
}