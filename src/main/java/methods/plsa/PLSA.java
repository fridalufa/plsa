package methods.plsa;


import entities.Song;
import entities.Word;
import methods.ProbabilisticModelAnalysis;
import methods.ProbabilisticModelResult;
import util.Util;

import java.util.HashMap;
import java.util.List;


public class PLSA implements ProbabilisticModelAnalysis {

    private ProbabilisticModelResult result;

    public PLSA() {
    }

    public PLSA(ProbabilisticModelResult result) {
        this.result = result;
    }

    @Override
    public void run() {

        Util.log("Constructing document-term matrix");

        //Document-term-matrix
        short[][] docTermMatrix = buildDocumentTermMatrix();

        Util.log("Initializing matrices");
        result.docTopicProb = Util.initNormalizedRandomMatrix(result.corpus.getSongs().size(), result.numTopics);
        result.topicWordProb = Util.initNormalizedRandomMatrix(result.numTopics, result.corpus.getVocabulary().size());

        // P(z|d,w)
        float[][][] topicProb = new float[result.corpus.getSongs().size()][result.corpus.getVocabulary().size()][result.numTopics];

        Util.log("Starting EM-algorithm with " + result.iterations + " iterations");

        for (result.iteration = 0; result.iteration < result.iterations; result.iteration++) {

            Util.log("Starting E step (iteration #" + (result.iteration + 1) + ")");

            // update P(z|d,w)
            for (int d = 0; d < result.corpus.getSongs().size(); d++) {
                for (int w = 0; w < result.corpus.getVocabulary().size(); w++) {

                    float[] pZD = result.docTopicProb[d];

                    float[] pWZ = new float[result.topicWordProb.length];
                    for (int i = 0; i < result.topicWordProb.length; i++) {
                        pWZ[i] = result.topicWordProb[i][w];
                    }

                    float[] p = new float[result.numTopics];
                    float sum = 0;
                    for (int i = 0; i < result.numTopics; i++) {
                        p[i] = pZD[i] * pWZ[i];
                        sum += p[i];
                    }

                    if (sum <= 0) {
                        throw new RuntimeException("Uh oh!");
                    }

                    topicProb[d][w] = Util.normalize(p);
                }
            }

            Util.log("Starting M step (iteration #" + (result.iteration + 1) + ")");

            // update P(w|z)
            for (int z = 0; z < result.numTopics; z++) {
                for (int w = 0; w < result.corpus.getVocabulary().size(); w++) {
                    float s = 0;
                    for (int d = 0; d < result.corpus.getSongs().size(); d++) {
                        s += docTermMatrix[d][w] * topicProb[d][w][z];
                    }
                    result.topicWordProb[z][w] = s;
                }

                result.topicWordProb[z] = Util.normalize(result.topicWordProb[z]);

            }

            // update P(z|d)
            for (int d = 0; d < result.corpus.getSongs().size(); d++) {
                for (int z = 0; z < result.numTopics; z++) {
                    float s = 0;
                    for (int w = 0; w < result.corpus.getVocabulary().size(); w++) {
                        s += docTermMatrix[d][w] * topicProb[d][w][z];
                    }
                    result.docTopicProb[d][z] = s;
                }
                result.docTopicProb[d] = Util.normalize(result.docTopicProb[d]);
            }
        }

        Util.log("Model fitting finished");
    }

    @Override
    public ProbabilisticModelResult getResult() {
        return result;
    }

    private short[][] buildDocumentTermMatrix() {

        List<Song> songs = result.corpus.getSongs();
        String[] vocabulary = result.corpus.getVocabulary().toArray(new String[result.corpus.getVocabulary().size()]);

        HashMap<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < vocabulary.length; i++) {
            indexMap.put(vocabulary[i], i);
        }

        short[][] docTermMatrix = new short[result.corpus.getSongs().size()][result.corpus.getVocabulary().size()];

        for (int d = 0; d < songs.size(); d++) {
            for (Word w : songs.get(d).lyrics) {
                w.docTermIndex = indexMap.get(w.word);
                docTermMatrix[d][w.docTermIndex] = w.count.shortValue();
            }
        }

        return docTermMatrix;
    }
}
