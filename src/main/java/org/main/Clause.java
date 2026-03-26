package org.main;

import org.main.Interfaces.Predicate;

public class Clause implements Predicate {
    Predicate embedded = null;
    Predicate head = null;
    int predicatesEmbedded = 0;
    String subject;
    String name;

    public Clause(){}

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
        predicatesEmbedded++;
    }
    // TODO
    // both of these cant exist, I dont want to touch the second one rn since it breaks your funcs
    // -Eigardas

    public int getPredicatesCount(){
        return predicatesEmbedded;
    }

    public int getPredicatesEmbedded(){
        if( embedded == null){
            return 0;
        }else{
            return embedded.getPredicatesEmbedded() + 1;
        }
    }

    public Predicate get(int i) {
        try {
            if(i > predicatesEmbedded){
                throw new IndexOutOfBoundsException("Failed To get");
            }else {
                Clause curr = this;
                for (int j = 0; j < i; j++) {
                    if (curr.getEmbedded() != null) {
                        curr = (Clause) curr.getEmbedded();
                    }
                }

                return curr;
            }
        } catch (Exception e) {
            System.out.println("Clause get failed");
            throw new RuntimeException(e);
        }
    }

    public int length() {
        if(predicatesEmbedded > 0){
            return predicatesEmbedded;
        }else{
           return head.getPredicatesCount();
        }
    }

    public Predicate getHead(){
        return head;
    }
    public void setHead(Predicate head){
        this.head = head;
    }
}