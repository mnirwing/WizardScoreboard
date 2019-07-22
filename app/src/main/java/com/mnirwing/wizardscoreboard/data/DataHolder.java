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

    private List<Game> games = new ArrayList<>();
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

    public List<Game> getGames() {
        if (games == null) {
            games = new ArrayList<>();
        }
        return games;
    }

    public Game getCurrentGame() {
        if (games == null) {
            games = new ArrayList<>();
        }
        for (Game game : games) {
            if (game.isCurrentGame()) {
                return game;
            }
        }
        return null;
    }

    public void setAllGamesOldExcept(Game game) {
        if (games == null) {
            games = new ArrayList<>();
        }
        for (Game oldGame : games) {
            oldGame.setCurrentGame(false);
        }
        game.setCurrentGame(true);
    }

    public void addGameAndSetCurrent(Game game) {
        if (games == null) {
            games = new ArrayList<>();
        }
        this.games.add(game);
        setAllGamesOldExcept(game);
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

    public void addPlayer(Player player) {
        this.getPlayers().add(player);
    }

    public void save(Context mContext) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String games = gson.toJson(this.games);
        String players = gson.toJson(this.players);
        prefsEditor.putString("games", games);
        prefsEditor.putString("players", players);
        prefsEditor.apply();
    }

    public void deleteAll() {
        this.games = null;
        this.players = null;
    }

    public String showJson() {
        Gson gson = new Gson();
        return gson.toJson(games);
    }

    public void load(Context mContext) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String gamesJson = appSharedPrefs.getString("games", "");
        Type type = new TypeToken<ArrayList<Game>>() {
        }.getType();
        String playersJson = appSharedPrefs.getString("players", "");
        Type type2 = new TypeToken<ArrayList<Player>>() {
        }.getType();
        this.games = gson.fromJson(gamesJson, type);
        this.players = gson.fromJson(playersJson, type2);
    }

    public boolean isLoaded() {
        return initialLoad;
    }

    public void setInitialLoad(boolean initialLoad) {
        DataHolder.initialLoad = initialLoad;
    }
}
