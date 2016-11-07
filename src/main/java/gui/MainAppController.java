package gui;

import entities.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.hibernate.Criteria;
import storage.Hibernator;

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
    public void initialize() {
        lvArtists.setItems(fetchArtists());
    }

    protected ObservableList<String> fetchArtists() {

        Hibernator.mainSession.beginTransaction();
        TypedQuery<Song> q = Hibernator.mainSession.createQuery("select s from Song s group by s.interpret order by s.interpret", Song.class);
        List<String> artists = q.getResultList().stream().map((Song s) -> s.interpret).collect(Collectors.toList());
        Hibernator.mainSession.getTransaction().commit();
        return FXCollections.observableArrayList(artists);
    }
}
