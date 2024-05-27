package com.example.stratego;

public class Move {
    private int sourceX;
    private int sourceY;
    private int destX;
    private int destY;

    public Move(int sourceX, int sourceY, int destX, int destY) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.destX = destX;
        this.destY = destY;
    }
    //

    public int getSourceX() {
        return sourceX;
    }

    public int getSourceY() {
        return sourceY;
    }

    public int getDestX() {
        return destX;
    }

    public int getDestY() {
        return destY;
    }

    @Override
    public String toString() {
        return "Move{" +
                "sourceX=" + sourceX +
                ", sourceY=" + sourceY +
                ", destX=" + destX +
                ", destY=" + destY +
                '}';
    }
}