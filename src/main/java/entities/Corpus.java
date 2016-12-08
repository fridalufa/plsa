package entities;


import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.OrderBy;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Corpus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @ManyToMany
    @Cascade(CascadeType.ALL)
    @OrderColumn
    private List<Song> songs;

    @ElementCollection
    @OrderBy()
    private SortedSet<String> vocabulary;

    public Corpus(){
        this.songs = new ArrayList<>();
        this.vocabulary = new TreeSet<>();
    }

    public void add(Song song){
        songs.add(song);
        vocabulary.addAll(song.lyrics.stream().map(w -> w.word).collect(Collectors.toList()));
    }

    public SortedSet<String> getVocabulary(){
        return vocabulary;
    }

    public List<Song> getSongs(){
        return songs;
    }

    public Integer getId() {
        return id;
    }
}
