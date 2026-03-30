package org.main.Interfaces;
import java.util.ArrayList;

public interface Predicate extends AnalogicalObject {
    void addEmbedded(AnalogicalObject analogicalObject);
    ArrayList<AnalogicalObject> getChildren();
    ArrayList<AnalogicalObject> getAllChildren();
}

