package main;

import entities.Corpus;
import entities.Song;
import entities.Word;
import org.hibernate.Transaction;
import org.hibernate.mapping.Array;
import plsa.PLSA;
import storage.Hibernator;

import javax.persistence.TypedQuery;
import java.util.*;


/**
 * @author fridalufa
 */
public class Main {
    public static void main(String[] args) {

        Corpus c = new Corpus();


        //Transaction t = Hibernator.mainSession.beginTransaction();
        TypedQuery<Song> query = Hibernator.mainSession.createQuery("from Song", Song.class).setMaxResults(100);
        List<Song> songs = query.getResultList();
        //t.commit();

        songs.forEach((song) -> c.add(song));

        Hibernator.mainSession.close();
        Hibernator.sessionFactory.close();

        PLSA plsa = new PLSA(c, 3, 10);
        plsa.run();
    }

}
