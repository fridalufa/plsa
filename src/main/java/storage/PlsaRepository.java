package storage;

import entities.Corpus;
import plsa.PLSA;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fridalufa
 */
public class PlsaRepository {

    public List<PlsaRunInfo> listPlsaRuns() {
        String hqlQuery = "select p.id, p.numTopics, p.iterations, p.corpus from PLSA as p";

        Hibernator.mainSession.beginTransaction();

        List<Object[]> runList = Hibernator.mainSession.createQuery(hqlQuery).list();

        Hibernator.mainSession.getTransaction().commit();

        return runList.stream()
                .map((el) -> new PlsaRunInfo((Integer)el[0], (Integer)el[1], (Integer)el[2], (Corpus) el[3]))
                .collect(Collectors.toList());
    }

    public PLSA fetchPLSA(Integer id) {
        Hibernator.mainSession.beginTransaction();

        PLSA plsa = Hibernator.mainSession.createQuery("from PLSA as p where p.id=:id", PLSA.class).setParameter("id", id).uniqueResult();

        Hibernator.mainSession.getTransaction().commit();

        return plsa;
    }
}
