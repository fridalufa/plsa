package storage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Level;

/**
 * @author fridalufa
 */
public class Hibernator {

    public static SessionFactory sessionFactory;
    public static Session mainSession;

    static {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        sessionFactory = new Configuration().configure("/storage/hibernate.cfg.xml").buildSessionFactory();
        mainSession = sessionFactory.openSession();
    }
}
