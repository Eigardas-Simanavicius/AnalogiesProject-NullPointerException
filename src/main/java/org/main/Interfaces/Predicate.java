package org.main.Interfaces;

import java.util.ArrayList;

public interface Predicate {
    ArrayList<Predicate> embedded = null;
    // Probably useful in the future
    int predicatesEmbedded = 0;
    public void addEmbedded(Predicate predicate);
    public ArrayList<Predicate> getChildren();
    public ArrayList<Predicate> getAllChildren();
    public void setSubject(String subject);
    public String getSubject();
    public void setName(String name);
    public String getName();
    public void increaseEmbedded();
    public int getPredicatesEmbedded();
    public int getPredicatesCount();
    public Predicate get(int i );
    public int length();
    public Predicate getParent();
    public void setParent(Predicate head);
    public void addChild(Predicate child);
}

