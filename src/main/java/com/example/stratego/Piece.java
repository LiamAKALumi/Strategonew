package com.example.stratego;

public class Piece {
    private String type;
    private int rank;
    private String color;
    private int posX;
    private int posY;
    private boolean moved;

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
    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
    public boolean canAttack(Piece other) {return calcDistance(other)==1&&!color.equals(other.getColor());}

    public int calcDistance(Piece other){ return Math.abs(posX-other.getPosX())+Math.abs(posY-other.getPosY());}
    @Override
    public String toString() {
        return  type + " : "+
                 rank;
    }
}