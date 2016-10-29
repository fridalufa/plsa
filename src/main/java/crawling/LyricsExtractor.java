package crawling;

import entities.Song;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author fridalufa
 */
public class LyricsExtractor {

    public static void fetchLyricsForSong(Song song) throws IOException {
        Document doc = JsoupConnectionFactory.connect(song.url).get();

        Elements lyrics = doc.select("div:not([class]):not([style]):not(:empty):not(:has(>iframe))");

        song.lyrics = lyrics.text();
    }
}
