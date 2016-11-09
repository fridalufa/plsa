package plsa.metrics;

import plsa.Result;

import java.util.stream.IntStream;

/**
 * @author fridalufa
 */
public class CosineSimilarity implements Metric {
    @Override
    public float calculate(float[] v1, float[] v2) {
        float dotProduct = (float) IntStream.range(0, v1.length).mapToDouble(i -> v1[i] * v2[i]).parallel().sum();

        float v1Len = (float) Math.sqrt(IntStream.range(0, v1.length).mapToDouble(i -> Math.sqrt(v1[i])).parallel().sum());
        float v2Len = (float) Math.sqrt(IntStream.range(0, v2.length).mapToDouble(i -> Math.sqrt(v2[i])).parallel().sum());

        return Math.abs(dotProduct / v1Len * v2Len);
    }

    @Override
    public int compareResults(Result a, Result b) {
        if (Math.abs(a.score - b.score) < 1e-6) {
            return 0;
        }

        return (a.score < b.score) ? 1 : -1;
    }

    @Override
    public String toString() {
        return "Cosine Similarity";
    }
}
