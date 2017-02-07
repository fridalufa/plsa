package methods;


import entities.Corpus;
import entities.Song;
import org.hibernate.annotations.*;

import javax.persistence.*;


@MappedSuperclass
public abstract class ProbabilisticModelResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    public Corpus corpus;

    public int numTopics;

    public int iterations;

    public int iteration = 0;

    /**
     * P(z|d)
     */
    @Lob
    public float[][] docTopicProb;

    /**
     * P(w|z)
     */
    @Lob
    public float[][] topicWordProb;

    public ProbabilisticModelResult() {
    }

    public ProbabilisticModelResult(Corpus corpus, int numTopics, int iterations) {
        this.corpus = corpus;
        this.numTopics = numTopics;
        this.iterations = iterations;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ", " + id.toString() + ". Corpus " + corpus.getId() + " (" + numTopics + " topics, " + iterations + " iterations)";
    }

    public float[] topicProbForSong(Song song) {

        int i = corpus.getSongs().indexOf(song);

        return docTopicProb[i];
    }
}
