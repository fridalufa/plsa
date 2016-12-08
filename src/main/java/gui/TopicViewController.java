package gui;

import entities.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import methods.plsa.PLSA;

import java.io.IOException;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author fridalufa
 */
public class TopicViewController {

    @FXML
    private ListView<Topic> lstTopics;

    @FXML
    private TableView<TopicWordModel> tblWords;

    PLSA plsa;

    public void initialize() {
        lstTopics.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tblWords.setItems(getWordsInTopic(newValue));
            }
        });
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
    }

    private ObservableList<TopicWordModel> getWordsInTopic(Topic topic) {
        float[] probs = plsa.topicWordProb[topic.id];
        SortedSet<String> vocabulary = plsa.corpus.getVocabulary();
        ObservableList<TopicWordModel> topicWordModels = FXCollections.observableArrayList();
        int i = 0;
        for (String word:vocabulary) {
            topicWordModels.add(new TopicWordModel(word, probs[i++]));
        }

        return topicWordModels;
    }
}
