package org.main;

import org.main.Interfaces.Predicate;

public class Clause implements Predicate {
    Predicate embedded = null;
    int predicatesEmbedded = 0;
    String subject;
    String name;

    public Clause(){};

    public Clause(String name, String subject){
        this.name = name;
        this.subject = subject;
    }

    public void setEmbedded(Predicate predicate){
        this.embedded = predicate;
    }

    public Predicate getEmbedded(){
        return this.embedded;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject(){
        return this.subject;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void increaseEmbedded(){
        return;
    }
}