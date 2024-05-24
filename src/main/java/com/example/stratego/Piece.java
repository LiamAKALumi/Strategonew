package com.example.stratego;

import java.util.Objects;
import java.util.Random;

public class Piece {
    private String type;
    private int rank;
    private String color;
    private int posX;
    private int posY;
    private int value;
    private boolean revealed;

    public Piece(String type, int rank, String color) {
        this.type = type;
        this.rank = rank;
        this.color = color;
        this.revealed = false;
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
    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }
    public boolean canAttack(Piece other) {return calcDistance(other)==1&&!color.equals(other.getColor());}

    public int calcDistance(Piece other){ return Math.abs(posX-other.getPosX())+Math.abs(posY-other.getPosY());}
    @Override
    public String toString() {
        return  type + " : "+
                 rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece piece)) return false;
        return rank == piece.rank && posX == piece.posX && posY == piece.posY && revealed == piece.revealed && Objects.equals(type, piece.type) && Objects.equals(color, piece.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, rank, color, posX, posY, revealed);
    }
    public Move getCloserToOtherPiece(Piece other) {
        // if only possible to get closer with the X axis, go in the X axis
        if(posX!= other.getPosX()&&posY== other.getPosY()) {
            if(posX> other.getPosX()){
                 Move move =new Move (posX,posY, posX-1, posY);
            }
            return new Move (posX,posY, posX+1, posY);
        }
        // if only possible to get closer with the Y axis, go in the Y axis
        if(posY!= other.getPosY()&&posX== other.getPosX()) {
            if(posY> other.getPosY()){
                return new Move (posX,posY, posX,posY-1);
            }
            return new Move (posX,posY, posX, posY+1);
        }
        // if both are possible, choose one randomly
        Random rnd = new Random();
        int xOrY=rnd.nextInt(2);
        if(xOrY==0){
            if(posX!= other.getPosX()&&posY== other.getPosY()) {
                if(posX> other.getPosX()){
                    return new Move (posX,posY, posX-1, posY);
                }
                return new Move (posX,posY, posX+1, posY);
            }
        }
        if(posY> other.getPosY()){
            return new Move (posX,posY, posX,posY-1);
        }
        return new Move (posX,posY, posX, posY+1);

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public boolean checkInteraction(Piece other){
        // if defending piece is a bomb
        if(other.getType().equals("B")){
            // return true if attacking troop is a miner. return false in all other cases
            return type.equals("8");
        }
        // if defending piece is a marshal
        if(other.getType().equals("1")){
            // return true if attacking troop is a spy. return false in all other cases
            return type.equals("S");
        }
        // in every other case, just check if the attacking piece's rank is higher than the defenders. If so return true, else false
        return other.getRank()<rank;
    }
}