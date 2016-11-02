package plsa;


import entities.Corpus;
import entities.Song;
import entities.Word;

import java.util.HashMap;
import java.util.List;

public class PLSA {

    private Corpus c;
    private int topics;
    private int iterations;


    public PLSA(Corpus c, int topics, int iterations){
        this.c = c;
        this.topics = topics;
        this.iterations = iterations;
    }

    public void run() {

        byte[][] docTermMatrix = buildDocumentTermMatrix();

        c.getSongs().get(0).lyrics.forEach(w -> System.out.print (w.docTermIndex+" "));
        System.out.println();
        printMatrix(docTermMatrix);
    }

    private byte[][] buildDocumentTermMatrix(){

        List<Song> songs = c.getSongs();
        String[] vocabulary = c.getVocabulary().toArray(new String[c.getVocabulary().size()]);

        HashMap<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < vocabulary.length; i++) {
            indexMap.put(vocabulary[i], i);
        }

        byte[][] docTermMatrix = new byte[c.getSongs().size()][c.getVocabulary().size()];

        for (int d = 0; d < songs.size(); d++){
            for (Word w : songs.get(d).lyrics) {
                w.docTermIndex = indexMap.get(w.word);
                docTermMatrix[d][w.docTermIndex] = w.count.byteValue();
            }
        }

        return docTermMatrix;
    }


    private void printMatrix(byte[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
    }

}
