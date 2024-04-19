package com.example.stratego;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {
    private GameModel model;
    private Random random;

    public AIPlayer(GameModel model) {
        this.model = model;
        this.random = new Random();
    }

    public void makeMove() {
        Move move = generateMove();
        System.out.println("AI move: " + move.toString());
        model.applyMove(move);
    }

    /*

    private Move generateMove() {
        int sourceX, sourceY, destX, destY;
        Piece sourcePiece, destPiece;
        do {
            sourceX = random.nextInt(10);
            sourceY = random.nextInt(10);
            destX = random.nextInt(10);
            destY = random.nextInt(10);
            sourcePiece = model.getBoard()[sourceX][sourceY];
            destPiece = model.getBoard()[destX][destY];
        } while (sourcePiece == null || !sourcePiece.getColor().equals("Red") ||
                (destPiece != null && !destPiece.getColor().equals("Blue")) ||
                !model.isMoveValid(new Move(sourceX, sourceY, destX, destY)));

        return new Move(sourceX, sourceY, destX, destY);
    }

     */
    private Move generateMove() {
        List<Move> possibleMoves = new ArrayList<>();
        Piece[][] board = model.getBoard();

        // Generate all possible moves
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Piece piece = board[x][y];
                //Generate all non scout moves
                if (piece != null && piece.getColor().equals("Red") && piece.getRank()!=2 && piece.getRank()!=-1) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx == 0 && dy == 0) continue; // Skip the current cell
                            int newX = x + dx;
                            int newY = y + dy;
                            if (newX >= 0 && newX < 10 && newY >= 0 && newY < 10) {
                                Move move = new Move(x, y, newX, newY);
                                if (model.isMoveValid(move)) {
                                    possibleMoves.add(move);
                                }
                            }
                        }
                    }
                }
                // Generate all scout moves
                else{
                    if(piece != null && piece.getColor().equals("Red") && piece.getRank()==2){
                        for (int dx = -9; dx <= 9; dx++) {
                            for (int dy = -9; dy <= 9; dy++) {
                                if (dx == 0 && dy == 0) continue; // Skip the current cell
                                int newX = x + dx;
                                int newY = y + dy;
                                if (newX >= 0 && newX < 10 && newY >= 0 && newY < 10) {
                                    Move move = new Move(x, y, newX, newY);
                                    if (model.isMoveValid(move)) {
                                        possibleMoves.add(move);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Prioritize moves that capture an opponent's piece
        for (Move move : possibleMoves) {
            Piece destPiece = board[move.getDestX()][move.getDestY()];
            if (destPiece != null && destPiece.getColor().equals("Blue") && model.getInteractionResult(move)) {
                return move;
            }
        }

        // If no capturing move is found, fall back to a random move
        if (!possibleMoves.isEmpty()) {
            return possibleMoves.get(random.nextInt(possibleMoves.size()));
        }

        // If no valid move is found, return null
        return null;
    }
}