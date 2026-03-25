package org.main.Interfaces;

public interface Predicate {
    Predicate embedded = null;
    // Probably useful in the future
    int predicatesEmbedded = 0;
    public void setEmbedded(Predicate predicate);
    public Predicate getEmbedded();
    public void setSubject(String subject);
    public String getSubject();
    public void setName(String name);
    public String getName();
    public void increaseEmbedded();
}
