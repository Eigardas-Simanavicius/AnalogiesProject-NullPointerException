package org.main;

import org.main.Interfaces.Predicate;

import java.util.ArrayList;

public class Clause implements Predicate {
    ArrayList<Predicate> children = new ArrayList<Predicate>();
    Predicate parent = null;
    int predicatesEmbedded = 0;
    String subject;
    String name;

    public Clause(){}

    public Clause(String name, String subject){
        this.name = name;
        this.subject = subject;
    }

    public void addEmbedded(Predicate predicate){
        this.children.add(predicate) ;
    }

    public ArrayList<Predicate> getAllChildren(){
        ArrayList<Predicate> output = new ArrayList<>();
        for(Predicate child : children){
            output.add(child);
            if(!child.getChildren().isEmpty()){
                ArrayList<Predicate> temp = child.getAllChildren();
                output.addAll(temp);
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
            return embedded.getFirst().getPredicatesEmbedded() + 1;
        }
    }

    public Predicate get(int i) {
        try {
            if(i > predicatesEmbedded){
                throw new IndexOutOfBoundsException("Failed To get");
            }else {
                Clause curr = this;
                for (int j = 0; j < i; j++) {
                    if (curr.getChildren() != null) {
                        curr = (Clause) curr.getChildren().getFirst();
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
           //return head.getPredicatesCount();
        }
        return 0;
    }

    public void addChild(Predicate child){
        children.add(child);
    }
    public ArrayList<Predicate> getChildren(){return children;}
    public Predicate getParent(){
        return parent;
    }
    public void setParent(Predicate head){
        this.parent = head;
    }
}