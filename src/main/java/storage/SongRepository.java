package storage;

import entities.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fridalufa
 */
public class SongRepository {

    protected Integer corpusId = 0;

    public void selectCorpus(Integer corpusId) {
        this.corpusId = corpusId;
    }

    public List<Song> fetchSongsOfArtist(String artist) {

        Hibernator.mainSession.beginTransaction();
        String hqlQuery = "select s from Corpus as c left join c.songs as s where c.id=:corpus and s.interpret=:artist";
        TypedQuery<Song> query = Hibernator.mainSession
                .createQuery(hqlQuery, Song.class)
                .setParameter("artist", artist)
                .setParameter("corpus", corpusId);
        List<Song> results = query.getResultList();
        Hibernator.mainSession.getTransaction().commit();

        return results;
    }

    public ObservableList<String> fetchArtists() {

        //String hqlQuery = "select s from Song s group by s.interpret order by s.interpret";
        String hqlQuery = "select s from Corpus as c left join c.songs as s where c.id=:corpus group by s.interpret order by s.interpret";

        Hibernator.mainSession.beginTransaction();

        TypedQuery<Song> q = Hibernator.mainSession
                .createQuery(hqlQuery, Song.class)
                .setParameter("corpus", corpusId);
        List<String> artists = q.getResultList().stream()
                .map((Song s) -> s.interpret).collect(Collectors.toList());

        Hibernator.mainSession.getTransaction().commit();

        return FXCollections.observableArrayList(artists);
    }
}
