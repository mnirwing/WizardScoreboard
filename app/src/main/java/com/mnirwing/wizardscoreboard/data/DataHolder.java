package com.mnirwing.wizardscoreboard.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataHolder {

    private static DataHolder instance;

    private List<Game> games;
    private List<Player> players;
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

    public void addGame(Game game) {
        this.getGames().add(game);
    }

    public List<Player> getPlayers() {
        if (players == null) {
            players = new ArrayList<>();
        }
        return players;
    }

    public void addPlayer(Player player) {
        this.getPlayers().add(player);
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
        Type type = new TypeToken<ArrayList<Game>>() {}.getType();
        String playersJson = appSharedPrefs.getString("players", "");
        Type type2 = new TypeToken<ArrayList<Player>>() {}.getType();
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
