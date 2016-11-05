package main;

import entities.Corpus;
import entities.Song;
import org.hibernate.Transaction;
import plsa.PLSA;
import storage.Hibernator;

import javax.persistence.TypedQuery;
import java.util.List;

public class MainHibernateTest {

    public static void main(String[] args) throws Exception {

        Corpus c = new Corpus();

        TypedQuery<Song> query = Hibernator.mainSession.createQuery("from Song", Song.class).setMaxResults(500);
        List<Song> songs = query.getResultList();

        songs.forEach((song) -> c.add(song));

        PLSA plsa = new PLSA(c, 10, 5);
        plsa.run();

        Transaction trans = Hibernator.mainSession.beginTransaction();
        Hibernator.mainSession.save(plsa);
        trans.commit();

        Hibernator.mainSession.close();
        Hibernator.sessionFactory.close();
    }
}
