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

    public Song() {}

    public Song(Integer id, String interpret, String title, List<Word> lyrics) {
        this.id = id;
        this.interpret = interpret;
        this.title = title;
        this.lyrics = lyrics;
    }

    @Override
    public String toString(){
        return this.interpret + " - " + this.title;
    }
}
