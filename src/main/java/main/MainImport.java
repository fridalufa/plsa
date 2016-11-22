package main;

import entities.Song;
import entities.Word;
import org.hibernate.Transaction;
import storage.Hibernator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainImport {

    private static Connection connection = null;
    private static Transaction transaction = null;

    private static String pathToSqlite;

    public static void main(String[] args) throws Exception {

        // Class.forName("org.sqlite.JDBC");

        if (args.length < 1) {
            throw new Exception("to few arguments");
        }

        int offset = 0;
        if (args.length >= 2) {
            offset = new Integer(args[1]);
        }

        int limit = 1000;
        if (args.length == 3) {
            limit = new Integer(args[2]);
        }

        pathToSqlite = args[0];

        establishSQLiteConnection();

        importTracks(offset, limit);

        closeSQLiteConnection();

        Hibernator.mainSession.close();
        Hibernator.sessionFactory.close();
    }

    private static void importTracks(int offset, int limit) {
        try {
            Statement statementTracks = connection.createStatement();
            Statement statementLyrics = connection.createStatement();

            statementTracks.setQueryTimeout(120);

            ResultSet track = statementTracks.executeQuery(
                    "select * from tracks order by mxm_id ASC limit " + offset + ", " + limit
            );

            int commitCounter = 0;

            transaction = Hibernator.mainSession.beginTransaction();

            while (track.next()) {
                ResultSet lyric = statementLyrics.executeQuery(
                        "select * from lyrics WHERE mxm_tid = " + track.getInt("mxm_id")
                );

                List<Word> lyrics = new ArrayList<>();

                while (lyric.next()) {
                    lyrics.add(
                            new Word(
                                    lyric.getString("word"),
                                    lyric.getInt("count")
                            )
                    );
                }

                if (lyrics.size() == 0) {
                    continue;
                }

                Song s = new Song(
                        track.getInt("mxm_id"),
                        track.getString("artist"),
                        track.getString("title"),
                        lyrics
                );

                Hibernator.mainSession.save(s);

                if (++commitCounter % 100 == 0) {
                    transaction.commit();

                    transaction = Hibernator.mainSession.beginTransaction();
                }
            }

            transaction.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void establishSQLiteConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + pathToSqlite);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void closeSQLiteConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
