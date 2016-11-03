package main;

import entities.Corpus;
import entities.Song;
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

        songs.forEach((song) -> c.add(song));

        Hibernator.mainSession.close();
        Hibernator.sessionFactory.close();

        PLSA plsa = new PLSA(c, 10, 10);
        plsa.run();

        // Similarity
        // fake a chosen song
        Song target = plsa.corpus.getSongs().get(10);
        System.out.println("Target: "+target);
        System.out.println("================");

        Similarity sim = new Similarity(plsa);

        for (Result res : sim.getSimilarSongs(target, 5)) {
            System.out.println(res.song + " (Score: "+res.score+")");
        }
    }

}
