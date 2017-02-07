package gui;

/**
 * @author fridalufa
 */
public class Topic {

    public int id;

    public Topic(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Topic %d", id);
    }
}
