package com.mnirwing.wizardscoreboard.data;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {

    private UUID id;

    private String name;

    private String nickname;

    public Player(String name, String nickname) {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
