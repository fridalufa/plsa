package methods.similarity.metrics;

import methods.similarity.Result;

/**
 * @author fridalufa
 */
public interface Metric {

    float calculate(float[] v1, float[] v2);

    int compareResults(Result a, Result b);
}
