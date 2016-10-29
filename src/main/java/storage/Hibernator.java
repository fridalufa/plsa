package storage;

import entities.Song;
import entities.Word;
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
        sessionFactory = new Configuration()
                .addAnnotatedClass(Song.class)
                .addAnnotatedClass(Word.class)
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                .setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/plsa?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
                .setProperty("hibernate.connection.username", "plsa")
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .buildSessionFactory();

        mainSession = sessionFactory.openSession();
    }
}
