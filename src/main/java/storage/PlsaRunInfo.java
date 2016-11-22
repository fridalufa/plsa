package storage;

import entities.Corpus;

/**
 * @author fridalufa
 */
public class PlsaRunInfo {

    public Integer id;
    public Integer numTopics;
    public Integer iterations;
    public Corpus corpus;

    public PlsaRunInfo(Integer id, Integer numTopics, Integer iterations, Corpus corpusId) {
        this.id = id;
        this.numTopics = numTopics;
        this.iterations = iterations;
        this.corpus = corpusId;
    }

    @Override
    public String toString() {
        return id.toString() + ". Corpus " + corpus.getId() + " (" + numTopics + " topics, " + iterations + " iterations)";
    }
}
