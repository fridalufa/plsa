package entities;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Word {

    public String word;

    public Integer count;

    @Transient
    public int docTermIndex;

    public Word(){}

    public Word(String word, Integer count) {
        this.word = word;
        this.count = count;
    }

    @Override
    public String toString(){
        return "("+word+", "+count+")";
    }

}
