package com.example.stratego;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
// GameController.java
public class GameController {
    private GameModel model;
    private GameView view;
    private AIPlayer aiPlayer;

    private int selectedPieceX, selectedPieceY;
    private int turn = 0;


    public GameController(GameView v) {
        this.model = new GameModel();
        this.view = v;
        this.aiPlayer = new AIPlayer(model);


    }

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        this.aiPlayer = new AIPlayer(model);
    }

    public void init()
    {
        view.updateView(model.getBoard());

    }

    public boolean handleUserMove(Move move) {
        // Check if the move is valid
        if (!model.isMoveValid(move)) {
            return false;
        }



        // Apply the move
        model.applyMove(move);



        return true;
    }

    public void handleButtonAction(String buttonId) {
        // Get the button that was clicked

        // Get the button's ID

        // Parse the button's ID to get the grid coordinates
        int x = Integer.parseInt(buttonId.substring(4, 5));
        int y = Integer.parseInt(buttonId.substring(5, 6));

        turn++;
        if(turn % 2 != 0){
            // Update the view
            selectedPieceX = x;
            selectedPieceY = y;
            return;
        }
        // this means this is the second click
        // Create a Move object
        Move move = new Move(selectedPieceX, selectedPieceY, x, y);

        // Call the handleUserMove method
        if(handleUserMove(move)){
                // Update the view
                view.updateView(model.getBoard());

            // Let the AI player make a move
            aiPlayer.makeMove();
            view.updateView(model.getBoard());



        }
        else
            turn--; // not a legal move
            // Update the view
        }
    }
