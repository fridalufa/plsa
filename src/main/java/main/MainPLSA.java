package main;

import entities.Corpus;
import entities.Song;
import methods.plsa.PLSAResult;
import org.hibernate.Transaction;
import methods.plsa.PLSA;
import methods.ProbabilisticModelResult;
import storage.Hibernator;

import javax.persistence.TypedQuery;
import java.util.*;


/**
 * @author fridalufa
 */
public class MainPLSA {

    public static void main(String[] args) {

        Corpus c = new Corpus();

        TypedQuery<Song> query = Hibernator.mainSession.createQuery("from Song", Song.class).setMaxResults(500);
        List<Song> songs = query.getResultList();

        songs.forEach(c::add);

        ProbabilisticModelResult result = new PLSAResult(c, 10, 5);

        PLSA plsa = new PLSA(result);
        try {
            result = plsa.run();
        } catch (RuntimeException e) {
            System.err.println("An error occured while executing the PLSA algorithm (possibly overfitting!)");
        } finally {
            Transaction trans = Hibernator.mainSession.beginTransaction();
            Hibernator.mainSession.save(result);
            trans.commit();
        }

        Hibernator.mainSession.close();
        Hibernator.sessionFactory.close();
    }

}
