package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

public class Clause implements Predicate{
    private ArrayList<AnalogicalObject> children = new ArrayList<AnalogicalObject>();
    private Predicate parent = null;
    private String name;

    private Clause(){}

    public Clause(String name){
        this.name = name;
    }

    @Override
    public void addEmbedded(AnalogicalObject predicate){
        if(!predicate.equals(this)) {
            predicate.setParent(this);
            this.children.add(predicate);
        }
        else{
            System.out.println("You cannot embed a predicate in itself.");
        }
    }

    @Override
    public ArrayList<AnalogicalObject> getAllChildren(){
        ArrayList<AnalogicalObject> output = new ArrayList<>();
        output.add(this);
        if(!children.isEmpty()) {
            for (AnalogicalObject child : children) {
                if(child instanceof Predicate){
                    output.addAll(((Predicate)child).getAllChildren());
                }
            }
        }
        return output;
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
    public ArrayList<AnalogicalObject> getChildren(){return children;}

    @Override
    public AnalogicalObject fromString(String stringRepresentation) {
        return null;
    }

    @Override
    public Predicate getParent(){
        return parent;
    }

    @Override
    public void setParent(Predicate head){
        this.parent = head;
    }

    @Override
    public int getDepth(){
        if(parent == null){
            return 0;
        }else{
            return parent.getDepth() + 1;
        }
    }

    private class Subject implements AnalogicalObject{
        private Predicate parent;
        private String name;

        private Subject(String name){
            this.name = name;
        }

        @Override
        public AnalogicalObject fromString(String subject){
            if(subject.contains("(") || subject.contains(")")){
                throw new InvalidParameterException("Subject value cannot contain formating characters such as ')' or '('.");
            }else{
                return new Subject(subject);
            }
        }

        @Override
        public String toString(){
            return name;
        }

        @Override
        public void setParent(Predicate parent) {
            this.parent = parent;
        }

        @Override
        public Predicate getParent() {
            return parent;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getDepth() {
            if(parent == null){
                return 0;
            }else{
                return parent.getDepth() + 1;
            }
        }
    }
}