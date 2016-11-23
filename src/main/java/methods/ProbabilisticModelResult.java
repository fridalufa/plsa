package methods;


import entities.Corpus;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;


@MappedSuperclass
public abstract class ProbabilisticModelResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

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

    public ProbabilisticModelResult(){}

    public ProbabilisticModelResult(Corpus corpus, int numTopics, int iterations) {
        this.corpus = corpus;
        this.numTopics = numTopics;
        this.iterations = iterations;
    }
}
