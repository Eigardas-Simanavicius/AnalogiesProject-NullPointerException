package org.main.Interfaces;


import org.main.AnalogyManager;

public interface AnalogicalObject {
    String toString();
    Predicate getParent();
    void setParent(Predicate parent);
    String getName();
    void setName(String name);
    int getDepth();
    boolean hasParent();
    AnalogicalObject getDeepCopy();
}
