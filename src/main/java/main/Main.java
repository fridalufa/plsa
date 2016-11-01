package main;

import entities.Song;
import entities.Word;
import org.hibernate.Transaction;
import storage.Hibernator;

import java.util.ArrayList;
import java.util.List;


/**
 * @author fridalufa
 */
public class Main {
    public static void main(String[] args) {

        List<Word> lyrics = new ArrayList<Word>();
        lyrics.add(new Word("Hallo", 1));
        lyrics.add(new Word("Welt", 1));

        Song s = new Song(1, "Fridalufa", "Title", lyrics);



        Transaction t = Hibernator.mainSession.beginTransaction();
        Hibernator.mainSession.save(s);
        t.commit();

        Hibernator.mainSession.close();
        Hibernator.sessionFactory.close();
    }
}
