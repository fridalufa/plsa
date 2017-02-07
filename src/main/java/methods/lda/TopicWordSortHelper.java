package methods.lda;

import cc.mallet.types.Alphabet;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class TopicWordSortHelper {

    private double[][] topicWords;
    private Alphabet dataAlphabet;
    private int corpusVocabularySize;

    public TopicWordSortHelper(double[][] topicWords, Alphabet dataAlphabet, int corpusVocabularySize) {
        this.topicWords = topicWords;
        this.dataAlphabet = dataAlphabet;

        // occasionally the corpus vocabulary size and the mallet alphabet size differ
        // (i.e. that mallet removes some words), so we use the presumably bigger corpusVocubularySize
        // to ensure compability with the PLSA behaviour
        this.corpusVocabularySize = corpusVocabularySize;
    }

    public float[][] getTopicWordProb() {
        // sort the alphabet lexicographically
        SortedSet<WordPosition> sortedWords = new TreeSet<WordPosition>();
        Iterator it = dataAlphabet.iterator();
        int i = 0;
        while (it.hasNext()) {
            sortedWords.add(new WordPosition((String) it.next(), i++));
        }

        // re-sort the topicWords probability matrix with respect to the new order
        int newPos = 0;
        float[][] topicSortedWords = new float[topicWords.length][corpusVocabularySize];
        for (WordPosition wp : sortedWords) {

            for (int t = 0; t < topicWords.length; t++) {
                topicSortedWords[t][newPos] = (float) topicWords[t][wp.position];
            }

            newPos++;
        }

        return topicSortedWords;
    }

    class WordPosition implements Comparable<WordPosition> {
        public String word;
        public int position;

        public WordPosition(String word, int position) {
            this.word = word;
            this.position = position;
        }

        @Override
        public int compareTo(WordPosition o) {
            return word.compareTo(o.word);
        }
    }

}
