package com.mnirwing.wizardscoreboard.data;

import android.util.Log;
import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {

    private static final String TAG = "Player";

    private UUID id;

    private String name;

    private String nickname;

    public Player(String name, String nickname) {
        Log.d(TAG, "created Player: " + name);
        this.id = UUID.randomUUID();
        this.name = name;
        this.nickname = nickname;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }
}
