package methods.lda;


import entities.Corpus;
import methods.ProbabilisticModelResult;

public class LDAResult extends ProbabilisticModelResult {

    // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
    //  Note that the first parameter is passed as the sum over topics, while
    //  the second is the parameter for a single dimension of the Dirichlet prior.
    public double alpha_t = 0.01;
    public double beta_w = 1.0;

    public LDAResult() {
    }

    public LDAResult(Corpus corpus, int numTopics, int iterations) {
        super(corpus, numTopics, iterations);
    }
}
