package com.example.stratego;
// GameModel.java
// GameModel.java
import java.util.Random;

import static java.lang.Math.abs;

public class GameModel {
    private Board board;

    private boolean gameOver = false;


    public GameModel() {
        this.board = new Board();
    }

    public void updatePos(Move move){
        board.getPiece(move.getSourceX(), move.getSourceY()).setPosX(move.getDestX());
        board.getPiece(move.getSourceX(), move.getSourceY()).setPosY(move.getDestY());
    }
    public void applyMove(Move move) {
        // Check if the move is valid
        if (!isMoveValid(move)) {
            return;
        }



        // When moving a piece - update the piece setPosX setPosTY
        // when Removing a piece - remove also from piecesArrayList -> computer & player

        // Get the piece from the source position
        if(board.getPiece(move.getSourceX(), move.getSourceY())!=null){
            Piece piece = board.getPiece(move.getSourceX(), move.getSourceY());
            // Update that the piece has been revealed
            piece.setRevealed(true);

            // Move the piece from its source to its destination
            if (board.getPiece(move.getDestX(), move.getDestY()) != null) {
                // this means that there was an attack
                System.out.println("the "+board.getPiece(move.getSourceX(), move.getSourceY()).getColor()+" "+board.getPiece(move.getSourceX(), move.getSourceY()).getType()+" has attacked the " + board.getPiece(move.getDestX(), move.getDestY()).getColor()+" "+board.getPiece(move.getDestX(), move.getDestY()).getType()+"!");
                if(!getInteractionResult(move)){
                    // this means that there was either a loss for the attacker or a tie
                    // in either scenario, there's no need to update the position of the piece itself, because it'll no longer be on the board

                    if(board.getPiece(move.getDestX(), move.getDestY()).getRank()==board.getPiece(move.getSourceX(), move.getSourceY()).getRank()){
                        // if true, this means the outcome was a tie
                        System.out.println("the attack resulted a tie!");

                        // update the pieces Arraylists
                        if(getFullBoard().getComputerPieces().contains(board.getPiece(move.getDestX(), move.getDestY()))){
                            getFullBoard().getComputerPieces().remove(board.getPiece(move.getDestX(), move.getDestY()));
                            getFullBoard().getPlayerPieces().remove(board.getPiece(move.getSourceX(), move.getSourceY()));
                            // update the knownPieces Arraylist
                            getFullBoard().getKnownPieces().remove(board.getPiece(move.getSourceX(), move.getSourceY()));
                        }
                        else {
                            getFullBoard().getPlayerPieces().remove(board.getPiece(move.getDestX(), move.getDestY()));
                            getFullBoard().getComputerPieces().remove(board.getPiece(move.getSourceX(), move.getSourceY()));
                            // update the knownPieces Arraylist
                            getFullBoard().getKnownPieces().remove(board.getPiece(move.getDestX(), move.getDestY()));
                        }

                        board.setPiece(move.getDestX(), move.getDestY(), null);
                        board.setPiece(move.getSourceX(), move.getSourceY(), null);
                    }
                    else{
                        // this means the outcome was a loss for the attacker
                        System.out.println("the attack resulted a loss! for the attacker");
                        board.getPiece(move.getDestX(), move.getDestY()).setRevealed(true);


                        // update the pieces Arraylists
                        if(getFullBoard().getComputerPieces().contains(board.getPiece(move.getSourceX(), move.getSourceY()))){
                            getFullBoard().getComputerPieces().remove(board.getPiece(move.getSourceX(), move.getSourceY()));
                            // update the knownPieces Arraylist
                            if(!getFullBoard().getKnownPieces().contains(board.getPiece(move.getDestX(), move.getDestY()))){
                                getFullBoard().getKnownPieces().add(board.getPiece(move.getDestX(), move.getDestY()));
                            }
                        }
                        else{
                            getFullBoard().getPlayerPieces().remove(board.getPiece(move.getSourceX(), move.getSourceY()));
                            getFullBoard().getKnownPieces().remove(board.getPiece(move.getSourceX(), move.getSourceY()));
                        }
                        board.setPiece(move.getSourceX(), move.getSourceY(), null);
                    }
                }
                else{
                    // this means that the attack was successful
                    System.out.println("the attack resulted a win for the attacker!");

                    // check -whether a move is a winning move
                    //  check if dest is FLAG

                    if(board.getPiece(move.getDestX(), move.getDestY()).getType().equals("F")){
                        gameOver=true;
                    }


                    // there is a need to change the position of the piece itself
                    updatePos(move);
                    // update the pieces Arraylists
                    if(getFullBoard().getComputerPieces().contains(board.getPiece(move.getDestX(), move.getDestY()))){
                        getFullBoard().getComputerPieces().remove(board.getPiece(move.getDestX(), move.getDestY()));
                        // update the knownPieces Arraylist
                        if(!getFullBoard().getKnownPieces().contains(board.getPiece(move.getSourceX(), move.getSourceY()))){
                            getFullBoard().getKnownPieces().add(board.getPiece(move.getSourceX(), move.getSourceY()));
                        }
                    }
                    else{
                        getFullBoard().getPlayerPieces().remove(board.getPiece(move.getDestX(), move.getDestY()));
                        // update the knownPieces Arraylist
                        getFullBoard().getKnownPieces().remove(board.getPiece(move.getDestX(), move.getDestY()));
                    }
                    // Remove the piece from the source position
                    board.setPiece(move.getSourceX(), move.getSourceY(), null);
                    // Set the piece to the destination position, overriding the defending piece
                    board.setPiece(move.getDestX(), move.getDestY(), piece);
                }
            }
            else{
                // this means that there wasn't an attack

                // there is a need to change the position of the piece itself
                updatePos(move);
                // Remove the piece from the source position
                board.setPiece(move.getSourceX(), move.getSourceY(), null);
                // Set the piece to the destination position
                board.setPiece(move.getDestX(), move.getDestY(), piece);
            }
        }
        }



    public boolean isMovablePiece(int x, int y)
    {
        // check if the piece that is being moved is an unoccupied square
        if(board.getPiece(x, y)==null) {
            return false;
        }
        // check if not moving opposing pieces
        if(board.getPiece(x, y).getColor().equals("Red")){
            return false;
        }
        // check if a piece is a water tile
        if(board.getPiece(x,y).getType().equals("W")){
            return false;
        }
        // check if a move is done with a piece that can't move
        if(board.getPiece(x, y).getRank()==-1){
            return false;
        }
        return true;
    }
    public boolean isComputerMovablePiece(int x, int y)
    {
        // check if the piece that is being moved is an unoccupied square
        if(board.getPiece(x, y)==null) {
            return false;
        }
        // check if not moving opposing pieces
        if(board.getPiece(x, y).getColor().equals("Blue")){
            return false;
        }
        // check if a piece is a water tile
        if(board.getPiece(x,y).getType().equals("W")){
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
        // check if the move doesn't cause an out of bounds exception
        if(move.getDestX()>=10||move.getDestX()<0||move.getDestY()>=10||move.getDestY()<0){
            return false;
        }
        // check if the piece is really a piece
        if(board.getPiece(move.getSourceX(), move.getSourceY())==null){
            return false;
        }
        // check if the moving piece can move
        if(board.getPiece(move.getSourceX(), move.getSourceY()).getRank()==-1){
            return false;
        }
        // check if the piece is not moving to a water tile
        if(board.getPiece(move.getDestX(), move.getDestY())!=null&&board.getPiece(move.getDestX(), move.getDestY()).getType().equals("W")){
            return false;
        }
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
    public boolean getInteractionResult(Move move){return board.getPiece(move.getSourceX(), move.getSourceY()).checkInteraction(board.getPiece(move.getDestX(),move.getDestY()));}

    public Piece[][] getBoard() {
        return board.getBoard();
    }


    public Board getFullBoard(){return this.board;}

    public boolean isGameOver() {
        return gameOver;
    }
    public Move getCloserToOtherPiece(Piece p, Piece other) {
        // if only possible to get closer with the X axis, go in the X axis
        if(p.getPosX()!= other.getPosX()&&p.getPosY()== other.getPosY()) {
            Move move;
            if(p.getPosX()> other.getPosX()){
                move = new Move(p.getPosX(), p.getPosY(), p.getPosX() - 1, p.getPosY());
            }
            else{
                move = new Move(p.getPosX(), p.getPosY(), p.getPosX() + 1, p.getPosY());
            }
            if (isMoveValid(move)){
                return move;
            }
        }
        if(p.getPosY()!= other.getPosY()&&p.getPosX()== other.getPosX()) {
            Move move;
            if(p.getPosY()> other.getPosY()){
                move = new Move(p.getPosX(), p.getPosY(), p.getPosX(), p.getPosY() - 1);
            }
            else{
                move = new Move(p.getPosX(), p.getPosY(), p.getPosX(), p.getPosY() + 1);
            }
            if (isMoveValid(move)){
                return move;
            }
        }
        // if both are possible, choose one of the options
        if(p.getPosX()!= other.getPosX()&&p.getPosY()!= other.getPosY()) {
            if(p.getPosX()> other.getPosX()){
                Move move = new Move (p.getPosX(),p.getPosY(), p.getPosX()-1, p.getPosY());
                if(isMoveValid(move)){
                    return move;
                }
            }
            Move move=new Move (p.getPosX(),p.getPosY(), p.getPosX()+1, p.getPosY());
            if(isMoveValid(move)){
                return move;
            }
        }
        if(p.getPosY()> other.getPosY()){
            Move move = new Move (p.getPosX(),p.getPosY(), p.getPosX(),p.getPosY()-1);
            if(isMoveValid(move)){
                return move;
            }
        }
        Move move= new Move (p.getPosX(),p.getPosY(), p.getPosX(), p.getPosY()+1);
        if(isMoveValid(move)){
            return move;
        }
        return null;
    }
}