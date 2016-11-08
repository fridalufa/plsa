package plsa.metrics;

import java.util.stream.IntStream;

/**
 * @author fridalufa
 */
public class KullbackLeiblerDivergence implements Metric {
    @Override
    public float calculate(float[] v1, float[] v2) {
        return (float) IntStream.range(0,v1.length).mapToDouble(i -> v1[i]*Math.log(v1[i]/v2[i])).sum();
    }

    @Override
    public String toString() {
        return "Kullback-Leibler-Divergenz";
    }
}
