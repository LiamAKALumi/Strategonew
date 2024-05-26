package com.example.stratego;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class GameView extends Application {

    // liam first comment


    private static final int BOARD_SIZE = 600;
    private static final int NUM_CELLS = 10;

    //////
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

// example
    public void updateView(Piece[][] board,boolean isOver,int player) {
        for (int x = 0; x < NUM_CELLS; x++) {
            for (int y = 0; y < NUM_CELLS; y++) {
                Piece piece = board[x][y];
                if (piece != null) {
                    if (piece.getType().equals("W")){
                        buttons[x][y].setStyle("-fx-background-color: #3ea4f0;"); // Red
                        buttons[x][y].setText("");
                    }
                    else if (piece.getColor().equals("Red")) {
                        buttons[x][y].setStyle("-fx-background-color: #ff0000;"); // Red
                        buttons[x][y].setText("");
                    } else if (piece.getColor().equals("Blue")) {
                        buttons[x][y].setText(piece.getType());
                        buttons[x][y].setStyle("-fx-background-color: #0000ff;-fx-text-fill: white;"); // Blue
                    }
                } else {
                    buttons[x][y].setText("");
                    buttons[x][y].setStyle(""); // Reset to default style
                }
            }
        }

        if(isOver) {
            // show popup
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            String title = "game over player " + player + " WON ";
            a.setTitle("GAME OVER ");
            a.setContentText(title);



            // show the dialog


            Optional<ButtonType> result = a.showAndWait();
            ButtonType button = result.orElse(ButtonType.OK);

            if (button == ButtonType.OK) {
                System.exit(1);
                Platform.exit();
            }



            System.out.println("GAME OVER");



        }

    }
}