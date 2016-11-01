package entities;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Word {

    @Lob
    public String word;

    public Integer count;

    public Word(){}

    public Word(String word, Integer count) {
        this.word = word;
        this.count = count;
    }

}
