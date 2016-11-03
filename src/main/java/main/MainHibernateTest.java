package main;

import entities.Corpus;
import entities.Song;
import entities.Word;
import org.hibernate.Transaction;
import plsa.PLSA;
import storage.Hibernator;

import java.util.ArrayList;
import java.util.List;

public class MainHibernateTest {

    public static void main(String[] args) throws Exception {
        Corpus corpus = new Corpus();
        Word word = new Word("hi",2);
        List<Word> wordList =  new ArrayList<>();
        wordList.add(word);
        corpus.add(new Song(9,"i","you and i",wordList));

        PLSA plsa = new PLSA(corpus,1,1);

        Transaction trans = Hibernator.mainSession.beginTransaction();
        Hibernator.mainSession.save(plsa);
        trans.commit();

        Hibernator.mainSession.close();
        Hibernator.sessionFactory.close();
    }
}
