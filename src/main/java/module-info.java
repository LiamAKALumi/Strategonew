module com.example.stratego {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.stratego to javafx.fxml;
    exports com.example.stratego;
}