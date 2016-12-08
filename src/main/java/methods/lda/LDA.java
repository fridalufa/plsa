package methods.lda;


import cc.mallet.pipe.*;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import entities.Word;
import methods.ProbabilisticModelAnalysis;
import methods.ProbabilisticModelResult;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class LDA implements ProbabilisticModelAnalysis {

    private LDAResult result;

    private static Pipe pipe = buildPipe();

    public LDA(LDAResult result) {
        this.result = result;
    }

    @Override
    public void run() {

        // create a list of mallet instances from the songs
        Iterator<Instance> instancesIterator = result.corpus.getSongs().stream().map(
                (song) -> Song2InstanceTransformer.transform(song)).iterator();
        InstanceList instances = new InstanceList(pipe);
        instances.addThruPipe(instancesIterator);

        // create the model with the given parameters and add our instances
        ParallelTopicModel model = new ParallelTopicModel(result.numTopics, result.beta_w, result.alpha_t);
        model.addInstances(instances);

        // Use two parallel samplers, which each look at one half the corpus and combine
        // statistics after every iteration.
        model.setNumThreads(2);

        // Run the model for the given number of iterations
        model.setNumIterations(result.iterations);
        try {
            model.estimate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // populate the results matrices
        Alphabet dataAlphabet = instances.getDataAlphabet();
        double[][] topicWords = model.getTopicWords(true, false);
        result.topicWordProb = (new TopicWordSortHelper(topicWords, dataAlphabet, result.corpus.getVocabulary().size()))
                .getTopicWordProb();

        result.docTopicProb = new float[result.corpus.getSongs().size()][];
        for (int d = 0; d < result.corpus.getSongs().size(); d++) {

            // double array to float array
            double[] dTopics = model.getTopicProbabilities(d);
            float[] topics = new float[result.numTopics];
            for (int t = 0; t < result.numTopics; t++) {
                topics[t] = (float) dTopics[t];
            }

            result.docTopicProb[d] = topics;
        }

    }

    @Override
    public ProbabilisticModelResult getResult() {
        return result;
    }

    private static Pipe buildPipe() {

        ArrayList pipeList = new ArrayList();

        // Read data from File objects
        pipeList.add(new Input2CharSequence("UTF-8"));

        // Regular expression for what constitutes a token.
        //  This pattern includes Unicode letters, Unicode numbers,
        //   and the underscore character. Alternatives:
        //    "\\S+"   (anything not whitespace)
        //    "\\w+"    ( A-Z, a-z, 0-9, _ )
        //    "[\\p{L}\\p{N}_]+|[\\p{P}]+"   (a group of only letters and numbers OR
        //                                    a group of only punctuation marks)
        Pattern tokenPattern =
                Pattern.compile("[\\p{L}\\p{N}_]+");

        // Tokenize raw strings
        pipeList.add(new CharSequence2TokenSequence(tokenPattern));

        // Normalize all tokens to all lowercase
        pipeList.add(new TokenSequenceLowercase());

        // Remove stopwords from a standard English stoplist.
        //  options: [case sensitive] [mark deletions]
        pipeList.add(new TokenSequenceRemoveStopwords(false, false));

        // Rather than storing tokens as strings, convert
        //  them to integers by looking them up in an alphabet.
        pipeList.add(new TokenSequence2FeatureSequence());

        // Do the same thing for the "target" field:
        //  convert a class label string to a Label object,
        //  which has an index in a Label alphabet.
        pipeList.add(new Target2Label());

        return new SerialPipes(pipeList);
    }

}
