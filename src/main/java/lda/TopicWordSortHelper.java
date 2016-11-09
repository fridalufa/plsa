package lda;

import cc.mallet.types.Alphabet;

import java.util.SortedSet;
import java.util.TreeSet;

public class TopicWordSortHelper {

    private double[][] topicWordProb;
    private Alphabet dataAlphabet;

    public TopicWordSortHelper(double[][] topicWordProb, Alphabet dataAlphabet) {
        this.topicWordProb = topicWordProb;
        this.dataAlphabet = dataAlphabet;
    }

    public float[][] getTopicWordProb() {

        //for (int i = 0; i < )
        //dataAlphabet.lookupObject(max)

        return null;
    }

    class TopicWordAssignment implements Comparable<TopicWordAssignment>{
        public double[] probs;
        public int index;
        public String word;

        @Override
        public int compareTo(TopicWordAssignment o) {
            return word.compareTo(o.word);
        }
    }

    public static void main(String[] args) {

        SortedSet<String> test = new TreeSet<>();
        test.add("affe");
        test.add("cola");
        test.add("banane");

        test.forEach((s) -> System.out.println(s));
    }
}
