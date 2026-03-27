package org.main.Interfaces;

import java.util.ArrayList;

public interface Predicate {
    ArrayList<Predicate> embedded = null;
    public void addEmbedded(Predicate predicate);
    public ArrayList<Predicate> getChildren();
    public ArrayList<Predicate> getAllChildren();
    public void setSubject(String subject);
    public String getSubject();
    public void setName(String name);
    public String getName();
    public int getPredicatesEmbedded();
    public Predicate getParent();
    public void setParent(Predicate head);

}

