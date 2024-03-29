package com.mnirwing.wizardscoreboard.data;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class DataHolderTest {

    private DataHolder data = DataHolder.getInstance();

    @Before
    public void setUp() {
        data.addPlayer(new Player("Test1", "t1"));
        data.addPlayer(new Player("Test2", "t2"));
        data.addPlayer(new Player("Test3", "t3"));
        data.addPlayer(new Player("Test4", "t4"));
        data.addPlayer(new Player("Test5", "t5"));
        data.addPlayer(new Player("Test6", "t6"));

        Game game = new Game(data.getPlayers());
        for (int i = 0; i < 9; i++) {
            Round round = new Round();
            Move move1 = new Move(data.getPlayers().get(0).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move2 = new Move(data.getPlayers().get(1).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move3 = new Move(data.getPlayers().get(2).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move4 = new Move(data.getPlayers().get(3).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move5 = new Move(data.getPlayers().get(4).getId(), game.getId(),
                    (int) (Math.random() * 4));
            Move move6 = new Move(data.getPlayers().get(5).getId(), game.getId(),
                    (int) (Math.random() * 4));
            move1.setTricksAndCalculateScore((int) (Math.random() * 4));
            move2.setTricksAndCalculateScore((int) (Math.random() * 4));
            move3.setTricksAndCalculateScore((int) (Math.random() * 4));
            move4.setTricksAndCalculateScore((int) (Math.random() * 4));
            move5.setTricksAndCalculateScore((int) (Math.random() * 4));
            move6.setTricksAndCalculateScore((int) (Math.random() * 4));
            round.addMoves(move1, move2, move3, move4, move5, move6);
            game.addRound(round);
        }
        game.calculateAllTotalScores();
        data.setGame(game);
    }

    @Test
    public void getAllPlayersById() {
        List<Player> players = data.getPlayersById(
                Arrays.asList(data.getPlayers().get(0).getId(), data.getPlayers().get(1).getId(),
                        data.getPlayers().get(2).getId(), data.getPlayers().get(3).getId(),
                        data.getPlayers().get(4).getId(), data.getPlayers().get(5).getId()));
        assertEquals(players.get(0).getId().toString(),
                data.getPlayers().get(0).getId().toString());
        assertEquals(players.get(1).getId().toString(),
                data.getPlayers().get(1).getId().toString());
        assertEquals(players.get(2).getId().toString(),
                data.getPlayers().get(2).getId().toString());
        assertEquals(players.get(3).getId().toString(),
                data.getPlayers().get(3).getId().toString());
        assertEquals(players.get(4).getId().toString(),
                data.getPlayers().get(4).getId().toString());
        assertEquals(players.get(5).getId().toString(),
                data.getPlayers().get(5).getId().toString());
    }

    @Test
    public void getOnePlayerById() {
        List<Player> player = data.getPlayersById(Arrays.asList(data.getPlayers().get(0).getId()));
        assertEquals(player.get(0).getId().toString(),
                data.getPlayers().get(0).getId().toString());
    }

    @Test
    public void getTwoPlayersById() {
        List<Player> players = data.getPlayersById(
                Arrays.asList(data.getPlayers().get(2).getId(), data.getPlayers().get(4).getId()));
        assertEquals(players.get(0).getId().toString(),
                data.getPlayers().get(2).getId().toString());
        assertEquals(players.get(1).getId().toString(),
                data.getPlayers().get(4).getId().toString());
    }

    @Test
    public void getPlayerNames() {
        List<String> playerNames = data.getPlayerNames(
                Arrays.asList(data.getPlayers().get(3), data.getPlayers().get(1),
                        data.getPlayers().get(4)));
        assertEquals(playerNames.get(0), "Test4");
        assertEquals(playerNames.get(1), "Test2");
        assertEquals(playerNames.get(2), "Test5");
    }

    @Test
    public void getPlayerName() {
        List<String> playerNames = data.getPlayerNames(Arrays.asList(data.getPlayers().get(3)));
        assertEquals(playerNames.get(0), "Test4");
    }

    @Test
    public void getPlayerNamesByPlayersById() {
        List<String> playerNames = data.getPlayerNames(data.getPlayersById(
                Arrays.asList(data.getPlayers().get(1).getId(), data.getPlayers().get(5).getId(),
                        data.getPlayers().get(2).getId())));
        assertEquals(playerNames.get(0), "Test2");
        assertEquals(playerNames.get(1), "Test6");
        assertEquals(playerNames.get(2), "Test3");
    }
}