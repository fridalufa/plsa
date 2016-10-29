package main;

import entities.Song;
import org.hibernate.Transaction;
import storage.Hibernator;


/**
 * @author fridalufa
 */
public class Main {
    public static void main(String[] args) {
            Song s = new Song("Fridalufa", "Title", "http://url");

            s.lyrics = "lyrics";


            Transaction t = Hibernator.mainSession.beginTransaction();
            Hibernator.mainSession.save(s);
            t.commit();

            Hibernator.mainSession.close();
            Hibernator.sessionFactory.close();
    }
}
