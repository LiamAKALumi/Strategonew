package com.example.stratego;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class GameView extends Application {

    private static final int BOARD_SIZE = 600;
    private static final int NUM_CELLS = 10;
    private Button[][] buttons = new Button[NUM_CELLS][NUM_CELLS];

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameView.class.getResource("hello-view.fxml"));

        // Create a new instance of GameController
        GameController controller = new GameController(this);
   //     fxmlLoader.setController(controller);
        fxmlLoader.setControllerFactory(param -> controller);

        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Stratego Game");
        stage.setScene(scene);
        stage.show();


        initButtonArray(scene);

        // Set up the button click event handlers
        setupButtonHandlers(controller);


        controller.init();



    }

    private void initButtonArray(Scene scene) {
        // Initialize the buttons array with the buttons from the FXML file
        for (int i = 0; i < NUM_CELLS; i++) {
            for (int j = 0; j < NUM_CELLS; j++) {
                String buttonId = "cell" + i + j;
                buttons[i][j] = (Button) scene.lookup("#" + buttonId);
            }
        }
    }

    public void setupButtonHandlers(GameController controller) {
        for (Button[] buttonRow : buttons) {
            for (Button button : buttonRow) {
                button.setOnAction(event -> controller.handleButtonAction(button.getId()));
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }


    public void updateView(Piece[][] board) {
        for (int x = 0; x < NUM_CELLS; x++) {
            for (int y = 0; y < NUM_CELLS; y++) {
                Piece piece = board[x][y];
                if (piece != null) {
                    buttons[x][y].setText(piece.toString());
                    if (piece.getColor().equals("Red")) {
                        buttons[x][y].setStyle("-fx-background-color: #ff0000;"); // Red
                    } else if (piece.getColor().equals("Blue")) {
                        buttons[x][y].setStyle("-fx-background-color: #0000ff;"); // Blue
                    }
                } else {
                    buttons[x][y].setText("");
                    buttons[x][y].setStyle(""); // Reset to default style
                }
            }
        }
    }

}