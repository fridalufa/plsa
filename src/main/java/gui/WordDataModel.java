package gui;

import entities.Word;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author fridalufa
 */
public class WordDataModel {
    private final SimpleStringProperty word;
    private final SimpleIntegerProperty count;

    public WordDataModel(Word word) {
        this.word = new SimpleStringProperty(word.word);
        this.count = new SimpleIntegerProperty(word.count);
    }

    public String getWord() {
        return word.get();
    }

    public SimpleStringProperty wordProperty() {
        return word;
    }

    public int getCount() {
        return count.get();
    }

    public SimpleIntegerProperty countProperty() {
        return count;
    }
}
