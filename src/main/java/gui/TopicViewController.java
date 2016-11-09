package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import plsa.PLSA;

import java.io.IOException;

/**
 * @author fridalufa
 */
public class TopicViewController {

    PLSA plsa;

    public static TopicViewController open() throws IOException {
        FXMLLoader loader = new FXMLLoader(TopicViewController.class.getResource("topicView.fxml"));


        Parent root = loader.load();

        Stage stage = new Stage();

        stage.setTitle("Topic Word Distribution");

        stage.setScene(new Scene(root, 800, 600));
        stage.show();

        return loader.getController();
    }

    public void setPlsa(PLSA plsa) {
        this.plsa = plsa;
    }
}
