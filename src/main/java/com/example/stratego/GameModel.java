package com.example.stratego;
// GameModel.java
// GameModel.java
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

    public boolean isMoveValid(Move move) {
        // Check the game rules to see if the move is valid
        // For example, check if the source and destination cells are within the board,
        // if the source cell contains a piece, if the destination cell is empty, etc.
        // Return true if the move is valid, false otherwise
        return true;

    }

    public Piece[][] getBoard() {
        return board.getBoard();
    }
}