package storage;

import methods.Method;
import methods.ProbabilisticModelResult;
import methods.lda.LDAResult;
import methods.plsa.PLSAResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fridalufa
 */
public class ProbabilisticAnalysisResultRepository {

    public List<RunInfo> listPlsaRuns() {

        List<PLSAResult> runList = Hibernator.mainSession.createQuery("from PLSAResult", PLSAResult.class).getResultList();

        return runList.stream().map(res -> new RunInfo(Method.PLSA, res.id, res.numTopics, res.iterations, res.corpus)).collect(Collectors.toList());
    }

    public ProbabilisticModelResult fetchResult(Method method, Integer id) {
        Hibernator.mainSession.beginTransaction();

        ProbabilisticModelResult result;
        if (method == Method.PLSA) {
            result = Hibernator.mainSession.createQuery("from PLSAResult as p where p.id=:id", PLSAResult.class).setParameter("id", id).uniqueResult();
        } else {
            result = Hibernator.mainSession.createQuery("from LDAResult as p where p.id=:id", LDAResult.class).setParameter("id", id).uniqueResult();
        }

        Hibernator.mainSession.getTransaction().commit();

        return result;
    }

    public List<RunInfo> listLdaRuns() {
        List<LDAResult> runList = Hibernator.mainSession.createQuery("from LDAResult", LDAResult.class).getResultList();

        return runList.stream().map(res -> new RunInfo(Method.LDA, res.id, res.numTopics, res.iterations, res.corpus)).collect(Collectors.toList());
    }
}
