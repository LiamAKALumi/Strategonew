package com.example.stratego;

public class Piece {
    private String type;
    private int rank;
    private String color;

    public Piece(String type, int rank, String color) {
        this.type = type;
        this.rank = rank;
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return  type + " : "+
                 rank;
    }
}