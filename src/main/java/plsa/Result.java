package plsa;

import entities.Song;

public class Result implements Comparable<Result>{

    public Song song;
    public float score;

    public Result(Song song, float score) {
        this.song = song;
        this.score = score;
    }


    @Override
    public int compareTo(Result o) {

        if (Math.abs(this.score - o.score) < 1e-6) {
            return 0;
        }

        return (this.score < o.score) ? 1 : -1;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f)", song.toString(), score);
    }
}
