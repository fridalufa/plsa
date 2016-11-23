package methods.similarity;


import entities.Song;
import methods.ProbabilisticModelResult;
import methods.plsa.PLSA;
import methods.similarity.metrics.CosineSimilarity;
import methods.similarity.metrics.KullbackLeiblerDivergence;
import methods.similarity.metrics.Metric;
import methods.similarity.metrics.SSDSimilarity;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Similarity {

    public ProbabilisticModelResult result;

    public static Metric[] availableMetrics = {
            new CosineSimilarity(),
            new SSDSimilarity(),
            new KullbackLeiblerDivergence()
    };

    public Similarity(ProbabilisticModelResult result) {
        this.result = result;
    }

    public List<Result> getSimilarSongs(Song target, int topN, Metric metric) {

        PriorityQueue<Result> results = new PriorityQueue<>();

        int targetIndex = result.corpus.getSongs().indexOf(target);
        float[] targetTopicDist = result.docTopicProb[targetIndex];

        for (int i = 0; i < result.docTopicProb.length; i++) {
            if (i != targetIndex) {
                float score = metric.calculate(targetTopicDist, result.docTopicProb[i]);
                results.add(new Result(result.corpus.getSongs().get(i), score, metric));
            }
        }


        List<Result> topSongs = new ArrayList<>();
        for (int i=0; i < topN; i++) {
            topSongs.add(results.poll());
        }

        return topSongs;
    }

}
