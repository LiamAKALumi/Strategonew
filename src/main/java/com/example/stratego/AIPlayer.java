package com.example.stratego;

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
}