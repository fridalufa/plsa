package entities;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.List;

/**
 * @author fridalufa
 */
@Entity
public class Song {

    @Id
    private Integer id;

    public String interpret;

    public String title;

    @ElementCollection
    public List<Word> lyrics;

    @Transient
    private int wordCount;

    public Song() {}

    public Song(Integer id, String interpret, String title, List<Word> lyrics) {
        this.id = id;
        this.interpret = interpret;
        this.title = title;
        this.lyrics = lyrics;
    }

    public int getWordCount(){

        // cache word count for performance reasons
        if (wordCount == 0){
            wordCount = lyrics.stream().map(w -> w.count).reduce(0, (c1, c2) -> c1 + c2);
        }

        return wordCount;
    }
}
