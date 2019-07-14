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
    for (int i = games.size() - 1; i <= 0; i--) {
      if (games.get(i).isCurrentGame()) {
        return games.get(i);
      }
    }
    return null;
  }

  public void addGame(Game game) {
    if (games == null) {
      games = new ArrayList<>();
    }
    for (Game oldGame : games) {
      oldGame.setCurrentGame(false);
    }
    game.setCurrentGame(true);
    this.games.add(game);
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
    for(Player player : players) {
      if (playerIds.contains(player.getId())) {
        playersById.add(player);
      }
    }
    return playersById;
  }

  public void addPlayer(Player player) {
    this.players.add(player);
  }

//    public static ArrayList<Question> getActiveQuestions() {
//        ArrayList<Question> activeQuestions = new ArrayList<>();
//        for (int i = 0; i < topicList.size(); i++) {
//            if (topicList.get(i).isSelected()) {
//                for (int y = 0; y < topicList.get(i).getQuestions().size(); y++) {
//                    if (topicList.get(i).getQuestions().get(y).isActive()) {
//                        activeQuestions.add(topicList.get(i).getQuestions().get(y));
//                    }
//                }
//            }
//        }
//        return activeQuestions;
//    }

//    public static boolean readyForReset() {
//        for (int i = 0; i < topicList.size(); i++) {
//            if (topicList.get(i).isSelected() && (topicList.get(i).getQuestions().size() > 0)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean readyForShuffle() {
//        for (int i = 0; i < topicList.size(); i++) {
//            if (topicList.get(i).isSelected()) {
//                for (int y = 0; y < topicList.get(i).getQuestions().size(); y++) {
//                    if (topicList.get(i).getQuestions().get(y).isActive()) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

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
