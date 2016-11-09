package plsa.metrics;

import plsa.Result;

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
    public int compareResults(Result a, Result b) {
        if (Math.abs(a.score - b.score) < 1e-6) {
            return 0;
        }

        return (a.score > b.score) ? 1 : -1;
    }

    @Override
    public String toString() {
        return "Kullback-Leibler-Divergenz";
    }


}
