package org.main.Objects;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class Clause implements Predicate{
    private ArrayList<AnalogicalObject> children = new ArrayList<AnalogicalObject>();
    private Predicate parent = null;
    private String name;

    private Clause(){}

    public Clause(String name){
        this.name = name;
    }


    @Override
    public String toString(){
        return toStringHelper(false);
    }

    public String toIndentedString(){
        return toStringHelper(true);
    }

    private String toStringHelper(boolean prettify){
        if(parent != null){
            throw new UnsupportedOperationException("You cannot call toString() on a child Predicate. toString must be called on the root Predicate");
        }
        StringBuilder output = new StringBuilder();
        ArrayList<Predicate> clauseList = getAllChildren();
        int endParenthesesCounter = 0;
        int tabulationFixer = 0;
        for(int i = 0; i < clauseList.size(); i++){
            Predicate current = clauseList.get(i);
            ArrayList<AnalogicalObject> currentChildren = current.getChildren();
            output.append("(");
            if(current.getName() != null){
                output.append(current.getName());
            }

            boolean hasSubjects = false;
            if(!currentChildren.isEmpty()){
                for(AnalogicalObject child : currentChildren){
                    if(child instanceof Subject){
                        output.append(" ").append(child.getName());
                        hasSubjects = true;
                    }
                }
            }
            if(hasSubjects){
                //Assuming that parantheses only need to be closed if the predicate has a subject
                if(i != clauseList.size() -1){
                    Predicate next = clauseList.get(i+1);
                    if(next.getParent() != current){
                        int counter = 1;
                        Predicate possibleParent = current.getParent();
                        while(next.getParent() != possibleParent){
                            possibleParent = possibleParent.getParent();
                            counter++;
                        }
                        output.repeat(")",counter);
                        endParenthesesCounter -= counter;
                        tabulationFixer = counter-1;
                    }
                    if(prettify) {
                        output.append("\n");
                        output.repeat("\t", i+1-tabulationFixer);
                        tabulationFixer = 0;
                    }
                }
            }
            endParenthesesCounter++;
        }

        output.repeat(")", endParenthesesCounter);
        return output.toString();
    }

    @Override
    public void addEmbedded(AnalogicalObject embed){
        if(!embed.equals(this)) {
            embed.setParent(this);
            children.add(embed);
        }
        else{
            System.out.println("You cannot embed a predicate in itself.");
        }
    }

    @Override
    public void  addAllEmbedded(ArrayList<AnalogicalObject> analogicalObjects){
        for(AnalogicalObject object: analogicalObjects){
            addEmbedded(object);
        }
    }


    @Override
    public ArrayList<Predicate> getAllChildren(){
        ArrayList<Predicate> output = new ArrayList<>();
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
    public ArrayList<AnalogicalObject> getClauseChildren(){
        ArrayList<AnalogicalObject> clauses = new ArrayList<>();
        for (AnalogicalObject obj: this.getChildren()){
            if(obj instanceof Clause){
                clauses.add( obj);
            }
        }
        return clauses;
    }

    public void removeClauses(){
        this.getChildren().removeIf(obj -> obj instanceof Clause);
    }


    public Iterator<AnalogicalObject> getPreOrderIterator(){
        return new PreOrderAnalogicalObjectIterator(this);
    }

    private static class PreOrderAnalogicalObjectIterator implements Iterator<AnalogicalObject>{
        Stack<AnalogicalObject>  stack;

        public PreOrderAnalogicalObjectIterator(AnalogicalObject analogicalObject){
            stack = new Stack<>();
            stack.push(analogicalObject);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public AnalogicalObject next() {
            AnalogicalObject next = stack.pop();

            if(next instanceof Predicate){
                stack.addAll(((Predicate) next).getChildren().reversed());
            }

            return next;
        }
    }

    public boolean hasParent(){
        return !(this.getParent() == null);
    }


}