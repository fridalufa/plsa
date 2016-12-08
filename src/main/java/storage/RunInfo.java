package storage;

import entities.Corpus;
import methods.Method;

/**
 * @author fridalufa
 */
public class RunInfo {

    public Method method;
    public Integer id;
    public Integer numTopics;
    public Integer iterations;
    public Corpus corpus;

    public RunInfo(Method method, Integer id, Integer numTopics, Integer iterations, Corpus corpusId) {
        this.method = method;
        this.id = id;
        this.numTopics = numTopics;
        this.iterations = iterations;
        this.corpus = corpusId;
    }

    @Override
    public String toString() {
        return method + " " + id.toString() + ". Corpus " + corpus.getId() + " (" + numTopics + " topics, " + iterations + " iterations)";
    }
}
