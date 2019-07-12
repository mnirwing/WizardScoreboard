package com.mnirwing.wizardscoreboard.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Round {

    private List<Move> moves;

    public Round() {
        this.moves = new ArrayList<>();
    }

    public void addMoves(Move...moves) {
        this.moves.addAll(Arrays.asList(moves));
    }

    public List<Move> getMoves() {
        return moves;
    }

    @Override
    public String toString() {
        String allMoves = "";
        for(Move move: moves) {
            allMoves = allMoves.concat(move.toString());
        }
        return "Round{"  +
                "moves=" + allMoves +
                '}';
    }
}