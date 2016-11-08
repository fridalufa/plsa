package main;

import entities.Corpus;
import entities.Song;
import org.hibernate.Transaction;
import plsa.PLSA;
import plsa.Result;
import plsa.Similarity;
import storage.Hibernator;

import javax.persistence.TypedQuery;
import java.util.*;


/**
 * @author fridalufa
 */
public class Main {

    public static void main(String[] args) {

        Corpus c = new Corpus();

        TypedQuery<Song> query = Hibernator.mainSession.createQuery("from Song", Song.class).setMaxResults(500);
        List<Song> songs = query.getResultList();

        songs.forEach(c::add);

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

        /*
        // Similarity
        // fake a chosen song
        Song target = plsa.corpus.getSongs().get(10);
        System.out.println("Target: "+target);
        System.out.println("================");

        Similarity sim = new Similarity(plsa);

        for (Result res : sim.getSimilarSongs(target, 5)) {
            System.out.println(res.song + " (Score: "+res.score+")");
        } */
    }

}
