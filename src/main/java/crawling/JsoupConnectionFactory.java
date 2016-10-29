package crawling;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * @author fridalufa
 */
public class JsoupConnectionFactory {

    public static Connection connect(String url) {
        return Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Safari/602.1.50");
    }
}
