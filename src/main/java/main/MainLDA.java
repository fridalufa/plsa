package main;

import entities.Corpus;
import entities.Song;
import methods.lda.LDA;
import methods.lda.LDAResult;
import org.hibernate.Transaction;
import storage.Hibernator;

import javax.persistence.TypedQuery;
import java.util.*;

public class MainLDA {


    public static void main(String[] args) throws Exception {

        Corpus corpus = new Corpus();

        TypedQuery<Song> query = Hibernator.mainSession.createQuery("from Song", Song.class).setMaxResults(5000);
        List<Song> songs = query.getResultList();

        songs.forEach(corpus::add);

        LDAResult result = new LDAResult(corpus, 20, 50);

        LDA lda = new LDA(result);
        try {
            lda.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Transaction trans = Hibernator.mainSession.beginTransaction();
            Hibernator.mainSession.save(lda.getResult());
            trans.commit();
        }

        Hibernator.mainSession.close();
        Hibernator.sessionFactory.close();

    }



}