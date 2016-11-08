package plsa.metrics;

import antlr.collections.impl.IntRange;

import java.util.stream.IntStream;

/**
 * @author fridalufa
 */
public class SSDSimilarity implements Metric {
    @Override
    public float calculate(float[] v1, float[] v2) {
        return (float)IntStream.range(0,v1.length).mapToDouble(i -> Math.pow(v1[i] - v2[i], 2)).sum();
    }

    @Override
    public String toString() {
        return "Sum of Squared Differences";
    }
}
