package gui;

import entities.Song;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author fridalufa
 */
public class SongDataModel {
    private final SimpleStringProperty artist;
    private final SimpleStringProperty title;
    private final Song song;

    public SongDataModel(Song song) {
        this.artist = new SimpleStringProperty(song.interpret);
        this.title = new SimpleStringProperty(song.title);
        this.song = song;
    }

    public String getArtist() {
        return artist.get();
    }

    public SimpleStringProperty artistProperty() {
        return artist;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public Song getSong() {
        return song;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SongDataModel) {
            return song.equals(((SongDataModel) obj).getSong());
        }
        return super.equals(obj);
    }
}
