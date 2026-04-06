package org.main.Interfaces;


public interface AnalogicalObject {
    AnalogicalObject fromString(String stringRepresentation);
    String toString();
    Predicate getParent();
    void setParent(Predicate parent);
    String getName();
    void setName(String name);
    int getDepth();
    boolean hasParent();
}
