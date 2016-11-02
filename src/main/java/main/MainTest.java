package main;

import entities.Corpus;
import entities.Song;
import entities.Word;
import plsa.PLSA;

import java.util.Arrays;

public class MainTest {

    public static void main(String[] args) {

        Corpus c = new Corpus();

        Song briarRose = new Song();
        briarRose.lyrics = Arrays.asList(
                new Word("king", 1),
                new Word("queen", 1),
                new Word("time", 1),
                new Word("reigned", 1),
                new Word("country", 1),
                new Word("great", 1)
        );

        c.add(briarRose);

        Song dogAndSparrow = new Song();
        dogAndSparrow.lyrics = Arrays.asList(
                new Word("shepherd", 1),
                new Word("dog", 1),
                new Word("master", 1),
                new Word("care", 1),
                new Word("suffer", 1),
                new Word("greatest", 1),
                new Word("hunger", 1)
        );

        c.add(dogAndSparrow);

        Song fishermanAndWife = new Song();
        fishermanAndWife.lyrics = Arrays.asList(
                new Word("live", 1),
                new Word("day", 1)
        );

        c.add(fishermanAndWife);

        PLSA plsa = new PLSA(c, 3, 10);
        plsa.run();

    }
}
