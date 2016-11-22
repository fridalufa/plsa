package methods.plsa;

import entities.Corpus;
import methods.ProbabilisticModelResult;


public class PLSAResult extends ProbabilisticModelResult {

    public PLSAResult() {
    }

    public PLSAResult(Corpus corpus, int numTopics, int iterations) {
        super(corpus, numTopics, iterations);
    }
}
