package entities;

import javax.persistence.*;

/**
 * @author fridalufa
 */
@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Lob
    public String interpret;

    @Lob
    public String title;

    @Lob
    public String lyrics;

    @Lob
    public String url;

    public Song() {}

    public Song(String interpret, String title, String url) {
        this.interpret = interpret;
        this.title = title;
        this.url = url;
    }
}
