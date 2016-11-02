package plsa;


import entities.Corpus;
import entities.Song;
import entities.Word;
import util.Util;

import java.util.HashMap;
import java.util.List;

public class PLSA {

    private Corpus corpus;

    private int numTopics;
    private int iterations;

    public PLSA(Corpus corpus, int numTopics, int iterations){
        this.corpus = corpus;
        this.numTopics = numTopics;
        this.iterations = iterations;
    }

    public void run() {

        //byte[][] docTermMatrix = buildDocumentTermMatrix();
        //Util.printByteMatrix(docTermMatrix);

        // P(z|d)
        float[][] docTopicProb = Util.initNormalizedRandomMatrix(corpus.getSongs().size(), numTopics);

        Util.printFloatMatrix(docTopicProb);
    }

    private byte[][] buildDocumentTermMatrix(){

        List<Song> songs = corpus.getSongs();
        String[] vocabulary = corpus.getVocabulary().toArray(new String[corpus.getVocabulary().size()]);

        HashMap<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < vocabulary.length; i++) {
            indexMap.put(vocabulary[i], i);
        }

        byte[][] docTermMatrix = new byte[corpus.getSongs().size()][corpus.getVocabulary().size()];

        for (int d = 0; d < songs.size(); d++){
            for (Word w : songs.get(d).lyrics) {
                w.docTermIndex = indexMap.get(w.word);
                docTermMatrix[d][w.docTermIndex] = w.count.byteValue();
            }
        }

        return docTermMatrix;
    }



}
