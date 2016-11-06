package main;

import entities.Corpus;
import entities.Song;
import org.hibernate.Transaction;
import plsa.PLSA;
import storage.Hibernator;

import javax.persistence.TypedQuery;
import java.util.List;

public class MainHibernateTest {

    public static void main(String[] args) {

        Corpus c = new Corpus();

        TypedQuery<Song> query = Hibernator.mainSession.createQuery("from Song", Song.class).setMaxResults(500);
        List<Song> songs = query.getResultList();

        songs.forEach((song) -> c.add(song));

        PLSA plsa = new PLSA(c, 10, 5);
        try {
            plsa.run();
        } catch (RuntimeException e) {
            System.err.println("An error occured while executing the PLSA algorithm (possibly overfitting!)");
        } finally {
            Transaction trans = Hibernator.mainSession.beginTransaction();
            Hibernator.mainSession.save(plsa);
            trans.commit();
        }

        Hibernator.mainSession.close();
        Hibernator.sessionFactory.close();
    }
}
