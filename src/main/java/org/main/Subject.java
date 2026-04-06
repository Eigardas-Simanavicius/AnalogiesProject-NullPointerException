package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;

import java.security.InvalidParameterException;

public class Subject implements AnalogicalObject {
    private Predicate parent;
    private String name;
    private boolean hasAsterisk;

    public Subject(String name){
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

    public boolean isHasAsterisk(){
        return hasAsterisk;
    }
    public void setHasAsterisk(){
        hasAsterisk = true;
    }

    public boolean hasParent(){
        return !(this.getParent()==null);
    }
}

