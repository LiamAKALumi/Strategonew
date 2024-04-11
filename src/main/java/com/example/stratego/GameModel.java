package com.example.stratego;
// GameModel.java
// GameModel.java
import static java.lang.Math.abs;

public class GameModel {
    private Board board;

    public GameModel() {
        this.board = new Board();
    }

    public void applyMove(Move move) {
        // Check if the move is valid
        if (!isMoveValid(move)) {
            return;
        }

        // Get the piece from the source position
        Piece piece = board.getPiece(move.getSourceX(), move.getSourceY());

        // Set the piece to the destination position
        board.setPiece(move.getDestX(), move.getDestY(), piece);

        // Remove the piece from the source position
        board.setPiece(move.getSourceX(), move.getSourceY(), null);
    }
    public boolean isMoveablePiece(int x,int y)
    {
        // check if the piece that is being moved is an unoccupied square
        if(board.getPiece(x, y)==null) {
            return false;
        }
        if(board.getPiece(x, y).getColor().equals("Red")){
            return false;
        }
        // check if a move is done with a piece that can't move
        if(board.getPiece(x, y).getRank()==-1){
            return false;
        }
        return true;
    }
    public boolean checkLast(Move move){
        if(board.getPiece(move.getDestX(),move.getDestY())==null||!(board.getPiece(move.getSourceX(),move.getSourceY()).getColor().equals(board.getPiece(move.getDestX(),move.getDestY()).getColor()))){
            return true;
        }
        return false;
    }
    public boolean scoutSpecialCase(Move move){
        // check if the move affects both the X and Y axis
        if(move.getSourceX()- move.getDestX()!=0&&move.getSourceY()- move.getDestY()!=0){
            return false;
        }
        // if the index of the source row is larger than the destination row, check if there are any pieces in between the source and the destination
        if(move.getSourceX()- move.getDestX()>0){
            for(int i=move.getSourceX()-1; i>move.getDestX(); i--){
                if(board.getPiece(i,move.getSourceY())!=null){
                    return false;
                }
            }
            //special check for the last piece in the row
            if(checkLast(move)){
                return true;
            }

        }// if the index of the source row is larger than the destination row, check if there are any pieces in between the destination and the source
        if(move.getSourceX()- move.getDestX()<0){
            for(int i=move.getDestX()-1; i>move.getSourceX(); i--){
                if(board.getPiece(i,move.getSourceY())!=null){
                    return false;
                }
            }
            //special check for the last piece in the row
            if(checkLast(move)){
                return true;
            }
        }
        // if the index of the source column is larger than the destination column, check if there are any pieces in between the source and the destination
        if(move.getSourceY()- move.getDestY()>0) {
            for (int i = move.getSourceY() - 1; i > move.getDestY(); i--) {
                if (board.getPiece(move.getSourceX(), i) != null) {
                    return false;
                }
            }
            //special check for the last piece in the row
            if (checkLast(move)) {
                return true;
            }
        }

        if(move.getSourceY()- move.getDestY()<0) {
            for (int i = move.getDestY() - 1; i > move.getSourceY(); i--) {
                if (board.getPiece(move.getDestX(), i) != null) {
                    return false;
                }
            }
            //special check for the last piece in the row
            if (checkLast(move)) {
                return true;
            }
        }
        return false;

    }
    public boolean isMoveValid(Move move) {

         // check if the piece is going to an unoccupied square
        if(board.getPiece(move.getDestX(), move.getDestY())!=null){
            // if it's not, check if it's going to a square that is occupied by a piece with the same color
            if(board.getPiece(move.getSourceX(), move.getSourceY()).getColor().equals(board.getPiece(move.getDestX(), move.getDestY()).getColor())){
                return false;
            }
        }
        // check if it only moves 1 step in the X or Y axis
        if((abs(move.getSourceX()- move.getDestX())==1&&abs(move.getSourceY()- move.getDestY())==0)||(abs(move.getSourceX()- move.getDestX())==0&&abs(move.getSourceY()- move.getDestY())==1)){
            return true;
        }
        // check if the piece is not a scout
        if(board.getPiece(move.getSourceX(), move.getSourceY()).getRank()==2){
            return scoutSpecialCase(move);
        }
        return false;
    }

    public Piece[][] getBoard() {
        return board.getBoard();
    }
}