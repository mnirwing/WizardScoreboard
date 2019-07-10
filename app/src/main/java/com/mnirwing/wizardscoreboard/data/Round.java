package com.mnirwing.wizardscoreboard.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Round {

    private List<Move> moves;

    public Round() {
        this.moves = new ArrayList<>();
    }

    public void addMove(Move...moves) {
        this.moves.addAll(Arrays.asList(moves));
    }

    public List<Move> getMoves() {
        return moves;
    }
}