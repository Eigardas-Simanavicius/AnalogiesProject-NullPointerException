package org.main;

import com.sun.jdi.event.ClassUnloadEvent;
import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;
import org.main.Interfaces.Rule;

import java.util.ArrayList;

public class rewriteRule implements Rule {
    private String originalRule;
    private String originalPredicate;
    private String verbPredicate;
    private String prepositionPredicate;
    private String byArgument;
    private String newArgument;
    private Boolean newArgumentHasAsterisk;
    private ArrayList<String> modifiers;

    public rewriteRule(String rule){
        this.originalRule = rule;
    }

    public String getOriginalPredicate(){
        return originalPredicate;
    }

    public String getVerbPredicate(){
        return verbPredicate;
    }

    @Override
    public Boolean hasModifiers() {
        return !modifiers.isEmpty();
    }

    @Override
    public String getNewArgument() {
        return newArgument;
    }

    @Override
    public Boolean newArgumentHasAsterisk() {
        return newArgumentHasAsterisk;
    }

    @Override
    public ArrayList<String> getModifiers() {
        return modifiers;
    }

    @Override
    public String getByArgument() {
        return byArgument;
    }

    @Override
    public String getPrepositionPredicate() {
        return prepositionPredicate;
    }

    public void testConstructor(String origPred, String verb, String preposition, String byArg, ArrayList<String> mods, String newArg, Boolean newArgAsterisk){
        this.originalPredicate = origPred;
        this.verbPredicate = verb;
        this.prepositionPredicate = preposition;
        this.byArgument = byArg;
        this.modifiers = mods;
        this.newArgument = newArg;
        this.newArgumentHasAsterisk = newArgAsterisk;
    }


    public Predicate rewrite(Predicate source){
        validatePredicate(source);
        //TODO:modifier processing goes here

        Clause output = new Clause("by");
        output.addEmbedded(new Subject(byArgument));
        Clause secondClause = new Clause(verbPredicate);
        AnalogicalObject firstArg = source.getChildren().get(0);
        AnalogicalObject secondArg = source.getChildren().get(1);
        Clause thirdClause = new Clause(prepositionPredicate);
        secondClause.addEmbedded(firstArg);

        if(newArgumentHasAsterisk) {
            secondClause.addEmbedded(new Subject(newArgument));
            thirdClause.addEmbedded(secondArg);
        }
        else{
            secondClause.addEmbedded(secondArg);
            thirdClause.addEmbedded(new Subject(newArgument));
        }

        secondClause.addEmbedded(thirdClause);
        output.addEmbedded(secondClause);


        return output;
    }

    private void validatePredicate(Predicate source){
        if(!source.getName().equals(this.originalPredicate)){
            throw new IllegalArgumentException("Predicates do not match between rule and source");
        }
        for(AnalogicalObject child : source.getChildren()){
            if(!(child instanceof Subject)){
                throw new IllegalArgumentException("Predicate has predicate children, processing undefined");
            }
        }
        if(source.getChildren().size() > 2){
            throw new IllegalArgumentException("Predicate has more than 2 subjects, processing undefined");
        }
    }
}
