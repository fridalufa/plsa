package gui;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author fridalufa
 */
public class TopicWordModel {
    private final SimpleStringProperty word;
    private final SimpleFloatProperty propability;

    public TopicWordModel(String word, float propability) {
        this.word = new SimpleStringProperty(word);
        this.propability = new SimpleFloatProperty(propability);
    }

    public String getWord() {
        return word.get();
    }

    public SimpleStringProperty wordProperty() {
        return word;
    }

    public float getPropability() {
        return propability.get();
    }

    public SimpleFloatProperty propabilityProperty() {
        return propability;
    }
}
