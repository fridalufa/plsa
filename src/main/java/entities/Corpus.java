package entities;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Corpus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @OneToMany
    @Cascade(CascadeType.ALL)
    @OrderColumn
    private List<Song> songs;

    @ElementCollection
    private Set<String> vocabulary;

    public Corpus(){
        this.songs = new ArrayList<>();
        this.vocabulary = new HashSet<>();
    }

    public void add(Song song){
        songs.add(song);
        vocabulary.addAll(song.lyrics.stream().map(w -> w.word).collect(Collectors.toList()));
    }

    public Set<String> getVocabulary(){
        return vocabulary;
    }

    public List<Song> getSongs(){
        return songs;
    }


}
