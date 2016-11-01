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

        pathToSqlite = args[0];

        establishSQLiteConnection();

        importTracks();

        closeSQLiteConnection();

        Hibernator.mainSession.close();
        Hibernator.sessionFactory.close();
    }

    private static void importTracks() {
        try {
            Statement statementTracks = connection.createStatement();
            Statement statementLyrics = connection.createStatement();

            statementTracks.setQueryTimeout(120);

            ResultSet track = statementTracks.executeQuery(
                    "select * from tracks"
            );

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

                transaction = Hibernator.mainSession.beginTransaction();

                Hibernator.mainSession.save(s);

                transaction.commit();
            }
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
