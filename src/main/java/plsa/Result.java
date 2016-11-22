package plsa;

import entities.Song;
import plsa.metrics.Metric;

public class Result implements Comparable<Result>{

    public Song song;
    public float score;
    private Metric metric;

    public Result(Song song, float score, Metric metric) {
        this.song = song;
        this.score = score;
        this.metric = metric;
    }


    @Override
    public int compareTo(Result o) {
        return metric.compareResults(this, o);
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f)", song.toString(), score);
    }
}
