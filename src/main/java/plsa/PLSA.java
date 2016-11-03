package plsa;


import entities.Corpus;
import entities.Song;
import entities.Word;
import util.Util;

import java.util.HashMap;
import java.util.List;

public class PLSA {

    public Corpus corpus;

    public int numTopics;
    public int iterations;

    /**
     * Document-term-matrix
     */
    public short[][] docTermMatrix;

    /**
     * P(z|d)
     */
    public float[][] docTopicProb;

    /**
     * P(w|z)
     */
    public float[][] topicWordProb;

    /**
     * P(z|d,w)
     */
    public float[][][] topicProb;

    public PLSA(Corpus corpus, int numTopics, int iterations) {
        this.corpus = corpus;
        this.numTopics = numTopics;
        this.iterations = iterations;
    }

    public void run() {

        Util.log("Constructing document-term matrix");
        docTermMatrix = buildDocumentTermMatrix();

        Util.log("Initializing matrices");
        docTopicProb = Util.initNormalizedRandomMatrix(corpus.getSongs().size(), numTopics);
        topicWordProb = Util.initNormalizedRandomMatrix(numTopics, corpus.getVocabulary().size());
        topicProb = new float[corpus.getSongs().size()][corpus.getVocabulary().size()][numTopics];

        Util.log("Starting EM-algorithm with " + iterations + " iterations");

        for (int iteration = 0; iteration < iterations; iteration++) {

            Util.log("Starting E step (iteration #" + (iteration + 1) + ")");

            // update P(z|d,w)
            for (int d = 0; d < corpus.getSongs().size(); d++) {
                for (int w = 0; w < corpus.getVocabulary().size(); w++) {

                    float[] pZD = docTopicProb[d];

                    float[] pWZ = new float[topicWordProb.length];
                    for (int i = 0; i < topicWordProb.length; i++) {
                        pWZ[i] = topicWordProb[i][w];
                    }

                    float[] p = new float[numTopics];
                    float sum = 0;
                    for (int i = 0; i < numTopics; i++) {
                        p[i] = pZD[i] * pWZ[i];
                        sum += p[i];
                    }

                    if (sum <= 0) {
                        throw new RuntimeException("Uh oh!");
                    }

                    topicProb[d][w] = Util.normalize(p);
                }
            }

            Util.log("Starting M step (iteration #" + (iteration + 1) + ")");

            // update P(w|z)
            for (int z = 0; z < numTopics; z++) {
                for (int w = 0; w < corpus.getVocabulary().size(); w++) {
                    float s = 0;
                    for (int d = 0; d < corpus.getSongs().size(); d++) {
                        s += docTermMatrix[d][w] * topicProb[d][w][z];
                    }
                    topicWordProb[z][w] = s;
                }

                topicWordProb[z] = Util.normalize(topicWordProb[z]);

            }

            // update P(z|d)
            for (int d = 0; d < corpus.getSongs().size(); d++) {
                for (int z = 0; z < numTopics; z++) {
                    float s = 0;
                    for (int w = 0; w < corpus.getVocabulary().size(); w++) {
                        s += docTermMatrix[d][w] * topicProb[d][w][z];
                    }
                    docTopicProb[d][z] = s;
                }
                docTopicProb[d] = Util.normalize(docTopicProb[d]);
            }
        }

        Util.log("Model fitting finished");
    }

    private short[][] buildDocumentTermMatrix() {

        List<Song> songs = corpus.getSongs();
        String[] vocabulary = corpus.getVocabulary().toArray(new String[corpus.getVocabulary().size()]);

        HashMap<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < vocabulary.length; i++) {
            indexMap.put(vocabulary[i], i);
        }

        short[][] docTermMatrix = new short[corpus.getSongs().size()][corpus.getVocabulary().size()];

        for (int d = 0; d < songs.size(); d++) {
            for (Word w : songs.get(d).lyrics) {
                w.docTermIndex = indexMap.get(w.word);
                docTermMatrix[d][w.docTermIndex] = w.count.shortValue();
            }
        }

        return docTermMatrix;
    }


}
