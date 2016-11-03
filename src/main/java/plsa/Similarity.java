package plsa;


import entities.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.IntStream;

public class Similarity {

    public PLSA plsa;

    public Similarity(PLSA plsa) {
        this.plsa = plsa;
    }

    public List<Result> getSimilarSongs(Song target, int topN) {

        PriorityQueue<Result> results = new PriorityQueue<>();

        int targetIndex = plsa.corpus.getSongs().indexOf(target);
        float[] targetTopicDist = plsa.docTopicProb[targetIndex];

        for (int i = 0; i < plsa.docTopicProb.length; i++) {
            if (i != targetIndex) {
                float score = cosineSimilarity(targetTopicDist, plsa.docTopicProb[i]);
                results.add(new Result(plsa.corpus.getSongs().get(i), score));
            }
        }


        List<Result> topSongs = new ArrayList<>();
        for (int i=0; i < topN; i++) {
            topSongs.add(results.poll());
        }

        return topSongs;
    }

    private static float cosineSimilarity(float[] v1, float[] v2) {

        float dotProduct = (float) IntStream.range(0, v1.length).mapToDouble(i -> v1[i] * v2[i]).parallel().sum();

        float v1Len = (float) Math.sqrt(IntStream.range(0, v1.length).mapToDouble(i -> Math.sqrt(v1[i])).parallel().sum());
        float v2Len = (float) Math.sqrt(IntStream.range(0, v2.length).mapToDouble(i -> Math.sqrt(v2[i])).parallel().sum());

        return dotProduct / v1Len * v2Len;
    }

}
