package com.mnirwing.wizardscoreboard.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataHolder {

    private static DataHolder instance;

    private Game game;
    private List<Player> players = new ArrayList<>();
    private static boolean initialLoad;

    private DataHolder() {
    }

    public static DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Player> getPlayers() {
        if (players == null) {
            players = new ArrayList<>();
        }
        return players;
    }

    public List<Player> getPlayersById(List<UUID> playerIds) {
        if (players == null) {
            players = new ArrayList<>();
        }
        List<Player> playersById = new ArrayList<>();
        for (int i = 0; i < playerIds.size(); i++) {
            for (int j = 0; j < players.size(); j++) {
                if (playerIds.get(i).equals(players.get(j).getId())) {
                    playersById.add(players.get(j));
                }
            }
        }
        return playersById;
    }

    public List<String> getPlayerNames(List<Player> players) {
        List<String> playerNames = new ArrayList<>();
        if (players == null) {
            return playerNames;
        }
        for (Player player : players) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }

    public void addPlayer(Player player) {
        this.getPlayers().add(player);
    }

    public void deleteGameIfPlayerIsInvolved(Player involvedPlayer) {
        if (game.getPlayerIds().contains(involvedPlayer.getId())) {
            this.game = null;
        }
        players.remove(involvedPlayer);
    }

    public void save(Context mContext) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        prefsEditor.putString("game", gson.toJson(this.game));
        prefsEditor.putString("players", gson.toJson(this.players));
        prefsEditor.apply();
    }

    public void deleteAll() {
        this.game = null;
        this.players = null;
    }

    public String showJson() {
        Gson gson = new Gson();
        return gson.toJson(game);
    }

    public void load(Context mContext) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String gameJson = appSharedPrefs.getString("game", "");
        Type type = new TypeToken<Game>() {
        }.getType();
        String playersJson = appSharedPrefs.getString("players", "");
        Type type2 = new TypeToken<ArrayList<Player>>() {
        }.getType();
        this.game = gson.fromJson(gameJson, type);
        this.players = gson.fromJson(playersJson, type2);
    }

    public boolean isLoaded() {
        return initialLoad;
    }

    public void setInitialLoad(boolean initialLoad) {
        DataHolder.initialLoad = initialLoad;
    }
}
