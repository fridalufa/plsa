package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import storage.Hibernator;

import java.io.IOException;

public class MainGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainApp.fxml"));

        primaryStage.setTitle("PLSA");
        Scene s = new Scene(root);
        primaryStage.setScene(s);

        primaryStage.setOnCloseRequest(event -> {
            Hibernator.mainSession.close();
            Hibernator.sessionFactory.close();
        });

        primaryStage.show();
    }
}
