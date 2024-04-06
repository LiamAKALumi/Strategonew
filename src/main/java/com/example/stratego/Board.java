package com.example.stratego;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Board.java
public class Board {
    private Piece[][] board;

    public Board() {
        this.board = new Piece[10][10];
        initializeBoard();// assuming a 10x10 board
    }

    private void initializeBoard() {
        String[] colors = {"Red", "Blue"};
        for (String color : colors) {
            int row = color.equals("Red") ? 0 : 6;
            List<Piece> piecesList = new ArrayList<>();
            piecesList.addAll(Arrays.asList(createPieces(6, "B", -1, color))); // Bombs are not ranked
            piecesList.addAll(Arrays.asList(createPieces(1, "1", 10, color))); // Marshall has rank 10
            piecesList.addAll(Arrays.asList(createPieces(1, "2", 9, color))); // General has rank 9
            piecesList.addAll(Arrays.asList(createPieces(2, "3", 8, color))); // Colonel has rank 8
            piecesList.addAll(Arrays.asList(createPieces(3, "4", 7, color))); // Major has rank 7
            piecesList.addAll(Arrays.asList(createPieces(4, "5", 6, color))); // Captain has rank 6
            piecesList.addAll(Arrays.asList(createPieces(4, "6", 5, color))); // Lieutenant has rank 5
            piecesList.addAll(Arrays.asList(createPieces(4, "7", 4, color))); // Sergeant has rank 4
            piecesList.addAll(Arrays.asList(createPieces(5, "8", 3, color))); // Miner has rank 3
            piecesList.addAll(Arrays.asList(createPieces(8, "9", 2, color))); // Scout has rank 2
            piecesList.addAll(Arrays.asList(createPieces(1, "S", 1, color))); // Spy has rank 1
            piecesList.addAll(Arrays.asList(createPieces(1, "F", -1, color))); // Flag is not ranked

            Piece[] pieces = piecesList.toArray(new Piece[0]);

            int pieceIndex = 0;
            for (int i = 0; i < 4; i++) { // Loop over the top/bottom 4 rows
                for (int j = 0; j < 10; j++) { // Loop over each column
                    if (color.equals("Red")) {
                        board[i][j] = pieces[pieceIndex++]; // Place red pieces at the top
                    } else {
                        board[i + 6][j] = pieces[pieceIndex++]; // Place blue pieces at the bottom
                    }
                }
            }
        }
    }


    private Piece[] createPieces(int count, String type, int rank, String color) {
        Piece[] pieces = new Piece[count];
        for (int i = 0; i < count; i++) {
            pieces[i] = new Piece(type, rank, color);
        }
        return pieces;
    }


    public Piece[][] getBoard() {
        return board;
    }

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public void setPiece(int x, int y, Piece piece) {
        board[x][y] = piece;
    }
}