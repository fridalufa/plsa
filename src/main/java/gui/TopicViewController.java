package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import plsa.PLSA;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author fridalufa
 */
public class TopicViewController {

    @FXML
    private ListView<Topic> lstTopics;

    PLSA plsa;

    public void initialize() {

    }

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
        if (plsa != null) {
            updateTopicList();
        }
    }

    private void updateTopicList() {
        lstTopics.setItems(FXCollections.observableArrayList(
                IntStream.range(0, plsa.numTopics).mapToObj(Topic::new).collect(Collectors.toList())
        ));
        System.out.println(plsa.corpus.getVocabulary());
    }
}
