package main;

import crawling.ArtistNotFoundException;
import crawling.ArtistPage;
import crawling.LyricsExtractor;
import entities.Song;
import org.hibernate.Hibernate;
import org.hibernate.Transaction;
import storage.Hibernator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fridalufa
 */
public class Main {
    public static void main(String[] args) {
        //try {
            //List<Song> applesongs = ArtistPage.searchArtistPage("fiona_apple");

            Song s = new Song("Fridalufa", "Title", "http://url");

            s.lyrics = "lyrics";


            Transaction t = Hibernator.mainSession.beginTransaction();
            Hibernator.mainSession.save(s);
            t.commit();

            /*applesongs.forEach((song) -> {
                try {
                    LyricsExtractor.fetchLyricsForSong(song);
                    System.out.println("Fetched song: " + song.title);
                    Hibernator.mainSession.save(song);
                    Thread.sleep(1000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //System.out.println(song.title);
            });*/

            //System.out.println(applesongs.get(0).lyrics);
            Hibernator.mainSession.close();
            Hibernator.sessionFactory.close();
        /*} catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        } catch (ArtistNotFoundException e) {
            e.printStackTrace();
        }*/

        /*try {

            List<Song> annahssongs = ArtistPage.fetchArtistSongs("Annah Mac", "http://www.azlyrics.com/a/annahmac.html");

            annahssongs.forEach((song) -> {
                System.out.println(song.title);
            });

            LyricsExtractor.fetchLyricsForSong(annahssongs.get(0));

            System.out.println(annahssongs.get(0).lyrics);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static List<String> readCanoncialArtistList() throws URISyntaxException, IOException {
        URL artistsFile = ArtistPage.class.getResource("aset400.txt");

        return Files.lines(Paths.get(artistsFile.toURI())).collect(Collectors.toList());

        /*try (Stream<String> stream = Files.lines(Paths.get(artistsFile.toURI()))) {
            stream.forEach(System.out::println);
        }*/
    }
}
