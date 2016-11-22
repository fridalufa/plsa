package plsa;


import entities.Song;
import plsa.metrics.CosineSimilarity;
import plsa.metrics.KullbackLeiblerDivergence;
import plsa.metrics.Metric;
import plsa.metrics.SSDSimilarity;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Similarity {

    public PLSA plsa;

    public static Metric[] availableMetrics = {
            new CosineSimilarity(),
            new SSDSimilarity(),
            new KullbackLeiblerDivergence()
    };

    public Similarity(PLSA plsa) {
        this.plsa = plsa;
    }

    public List<Result> getSimilarSongs(Song target, int topN, Metric metric) {

        PriorityQueue<Result> results = new PriorityQueue<>();

        int targetIndex = plsa.corpus.getSongs().indexOf(target);
        float[] targetTopicDist = plsa.docTopicProb[targetIndex];

        for (int i = 0; i < plsa.docTopicProb.length; i++) {
            if (i != targetIndex) {
                float score = metric.calculate(targetTopicDist, plsa.docTopicProb[i]);
                results.add(new Result(plsa.corpus.getSongs().get(i), score, metric));
            }
        }


        List<Result> topSongs = new ArrayList<>();
        for (int i=0; i < topN; i++) {
            topSongs.add(results.poll());
        }

        return topSongs;
    }

}
