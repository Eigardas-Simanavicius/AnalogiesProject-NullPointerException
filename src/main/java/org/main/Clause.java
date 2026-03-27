package org.main;

import org.main.Interfaces.Predicate;

import java.util.ArrayList;

public class Clause implements Predicate {
    ArrayList<Predicate> children = new ArrayList<Predicate>();
    Predicate parent = null;
    String subject;
    String name;

    public Clause(){}

    public Clause(String name, String subject){
        this.name = name;
        this.subject = subject;
    }

    public void addEmbedded(Predicate predicate){
        predicate.setParent(this);
        this.children.add(predicate) ;
    }

    public ArrayList<Predicate> getAllChildren(){
        ArrayList<Predicate> output = new ArrayList<>();
        output.add(this);
        if(!children.isEmpty()) {
            for (Predicate child : children) {
                output.addAll(child.getAllChildren());
            }
        }
        return output;
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


    public int getPredicatesEmbedded(){
        if( embedded == null){
            return 0;
        }else{
            return embedded.getFirst().getPredicatesEmbedded() + 1;
        }
    }

    public ArrayList<Predicate> getChildren(){return children;}
    public Predicate getParent(){
        return parent;
    }
    public void setParent(Predicate head){
        this.parent = head;
    }
}