package lda;

import cc.mallet.pipe.*;
import cc.mallet.types.*;
import cc.mallet.topics.*;
import entities.Corpus;
import entities.Song;
import org.hibernate.Transaction;
import plsa.PLSA;
import storage.Hibernator;

import javax.persistence.TypedQuery;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;

public class MainTest {

    public static final int numTopics = 100;
    public static final int numIterations = 20;

    public static void main(String[] args) throws Exception {

        Corpus corpus = new Corpus();

        TypedQuery<Song> query = Hibernator.mainSession.createQuery("from Song", Song.class).setMaxResults(50000);
        List<Song> songs = query.getResultList();

        songs.forEach(corpus::add);

        Iterator<Instance> instancesIterator = corpus.getSongs().stream().map((song) -> Song2InstanceTransformer.transform(song)).iterator();
        InstanceList instances = new InstanceList(buildPipe());
        instances.addThruPipe(instancesIterator);


        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is the parameter for a single dimension of the Dirichlet prior.
        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

        model.addInstances(instances);

        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(2);

        // Run the model for 50 iterations and stop (this is for testing only,
        //  for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(numIterations);
        model.estimate();


        // fake a PLSA instance
        PLSA plsa = new PLSA(corpus, numTopics, numIterations);

        plsa.docTopicProb = new float[corpus.getSongs().size()][];
        for (int d = 0; d < corpus.getSongs().size(); d++) {

            // double array to float array
            double[] dTopics = model.getTopicProbabilities(d);
            float[] topics = new float[numTopics];
            for (int t = 0; t < numTopics; t++) {
                topics[t] = (float) dTopics[t];
            }

            plsa.docTopicProb[d] = topics;
        }

        Transaction trans = Hibernator.mainSession.beginTransaction();
        Hibernator.mainSession.save(plsa);
        trans.commit();
        Hibernator.mainSession.close();
        Hibernator.sessionFactory.close();


        // The data alphabet maps word IDs to strings
        Alphabet dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;

        Formatter out = new Formatter(new StringBuilder(), Locale.US);
        for (int position = 0; position < tokens.getLength(); position++) {
            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }
        System.out.println(out);

        // Estimate the topic distribution of the first instance,
        //  given the current Gibbs state.
        //double[] topicDistribution = model.getTopicProbabilities(0);

        // Get an array of sorted sets of word ID/count pairs
        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
        double[][] topicWords = model.getTopicWords(true, false);
        System.out.println(Arrays.toString(topicWords[0]));

        int max = 0;
        double maxVal = 0;
        for (int i = 0; i < topicWords[0].length; i++) {
            if (topicWords[0][i] > maxVal) {
                maxVal = topicWords[0][i];
                max = i;
            }
        }

        System.out.println("Max index: "+max+" l: "+topicWords[0].length);
        System.out.println(dataAlphabet.lookupObject(max));

        System.out.println(Arrays.toString(model.getTopWords(5)[0]));

        Iterator it = dataAlphabet.iterator();
        int i = 0;
        System.out.println(dataAlphabet.size());
        /*while(it.hasNext()) {
            //System.out.println(it.next()+" i: "+(i++));
        }*/

        for (IDSorter idCountPair : topicSortedWords.get(0)) {
            //System.out.println(idCountPair.getID() + " w: "+idCountPair.getWeight());
        }

        // Show top 5 words in topics with proportions for the first document
        /*for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

            out = new Formatter(new StringBuilder(), Locale.US);
            //out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
            int rank = 0;
            while (iterator.hasNext() && rank < 5) {
                IDSorter idCountPair = iterator.next();
                out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
                rank++;
            }
            System.out.println(out);
        }*Z
        /*
        // Create a new instance with high probability of topic 0
        StringBuilder topicZeroText = new StringBuilder();
        Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

        int rank = 0;
        while (iterator.hasNext() && rank < 5) {
            IDSorter idCountPair = iterator.next();
            topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
            rank++;
        }

        // Create a new instance named "test instance" with empty target and source fields.
        /*InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

        TopicInferencer inferencer = model.getInferencer();
        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
        System.out.println("0\t" + testProbabilities[0]);*/
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

        // Now convert the sequence of features to a sparse vector,
        //  mapping feature IDs to counts.
        //pipeList.add(new FeatureSequence2FeatureVector());

        // Print out the features and the label
        //pipeList.add(new PrintInputAndTarget());

        return new SerialPipes(pipeList);
    }

}