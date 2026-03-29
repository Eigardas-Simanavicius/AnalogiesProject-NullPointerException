package org.main;

import org.main.Interfaces.Predicate;

import java.util.ArrayList;
import java.util.Iterator;

public class Clause implements Predicate{
    ArrayList<Predicate> children = new ArrayList<Predicate>();
    Predicate parent = null;
    String subject = null;
    String name;

    public Clause(){}

    public Clause(String name, String subject){
        this.name = name;
        this.subject = subject;
    }

    public Clause(String name){
        this.name = name;
    }

    @Override
    public void addEmbedded(Predicate predicate){
        predicate.setParent(this);
        this.children.add(predicate) ;
    }

    @Override
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

    @Override
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String getSubject(){
        return this.subject;
    }

    @Override
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArrayList<Predicate> getChildren(){return children;}

    @Override
    public Predicate getParent(){
        return parent;
    }
    public void setParent(Predicate head){
        this.parent = head;
    }

    @Override
    public int depth(){
        if(parent == null){
            return 0;
        }else{
            return parent.depth() + 1;
        }
    }
}