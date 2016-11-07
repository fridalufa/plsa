package gui;

import entities.Song;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Criteria;
import storage.Hibernator;
import storage.SongRepository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

    private SongRepository songRepository;

    public MainAppController() {
        this.songRepository = new SongRepository();
    }

    @FXML
    public void initialize() {
        lvArtists.setItems(songRepository.fetchArtists());

        lvArtists.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> tblSongs.setItems(fetchSongsOfArtist(newValue)));
    }

    protected ObservableList<SongDataModel> fetchSongsOfArtist(String artist) {
        List<SongDataModel> models = songRepository.fetchSongsOfArtist(artist).stream()
                .map(SongDataModel::new).collect(Collectors.toList());

        return FXCollections.observableArrayList(models);
    }
}
