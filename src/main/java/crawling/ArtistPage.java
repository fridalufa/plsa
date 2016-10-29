package crawling;

import entities.Song;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fridalufa
 */
public class ArtistPage {

    public static List<Song> searchArtistPage(String canoncialName) throws URISyntaxException, IOException, ArtistNotFoundException {

        Document doc = JsoupConnectionFactory.connect("http://search.azlyrics.com/search.php?q=" + canoncialName).get();

        Elements fields = doc.select(".text-lowercase");

        if (fields.first().text().equals("Artists")) {
            Elements link = doc.select(".panel").first().select("a[target=_blank]");

            return fetchArtistSongs(link.text(), link.attr("href"));
        }

        throw new ArtistNotFoundException();

    }

    public static List<Song> fetchArtistSongs(String artist, String artistPage) throws IOException {

        Document doc = JsoupConnectionFactory.connect(artistPage).get();

        Elements songLinks = doc.select("a:not(.btn)[target=_blank]");

        return songLinks.stream().map((song) -> {
            String url = "http://azlyrics.com" + song.attr("href").substring(2);

            return new Song(artist, song.text(), url);
        }).collect(Collectors.toList());
    }
}
