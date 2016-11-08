package gui;

import entities.Song;
import entities.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import storage.SongRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fridalufa
 */
public class MainAppController {
    @FXML
    private ListView<String> lvArtists;

    @FXML
    private TableView<SongDataModel> tblSongs;

    @FXML
    private TableView<WordDataModel> tblWords;

    @FXML
    private TextField txtFilterArtists;

    private SongRepository songRepository;

    public MainAppController() {
        this.songRepository = new SongRepository();
        this.songRepository.selectCorpus(2);
    }

    @FXML
    public void initialize() {

        FilteredList<String> filteredList = new FilteredList<>(songRepository.fetchArtists(), s -> true);
        txtFilterArtists.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.length() == 0) {
                filteredList.setPredicate(s -> true);
            } else {
                filteredList.setPredicate(s -> s.contains(newValue));
            }
        });

        lvArtists.setItems(filteredList);

        lvArtists.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    tblWords.setItems(FXCollections.observableArrayList());
                    tblSongs.setItems(fetchSongsOfArtist(newValue));
                });

        tblSongs.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Song selectedSong = newValue.getSong();
                tblWords.setItems(prepareLyrics(selectedSong.lyrics));
            } else {
                tblWords.setItems(FXCollections.observableArrayList());
            }
        });
    }

    private ObservableList<SongDataModel> fetchSongsOfArtist(String artist) {
        List<SongDataModel> models = songRepository.fetchSongsOfArtist(artist).stream()
                .map(SongDataModel::new).collect(Collectors.toList());

        return FXCollections.observableArrayList(models);
    }

    private ObservableList<WordDataModel> prepareLyrics(List<Word> lyrics) {
        List<WordDataModel> wordDataModels = lyrics.stream()
                .map(WordDataModel::new).collect(Collectors.toList());

        return FXCollections.observableArrayList(wordDataModels);
    }
}
