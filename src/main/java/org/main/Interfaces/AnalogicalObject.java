package org.main.Interfaces;


public interface AnalogicalObject {
    String toString();
    AnalogicalObject fromString(String stringRepresentation);
    Predicate getParent();
    void setParent(Predicate parent);
    int getDepth();
    void setName(String name);
    String getName();
}
