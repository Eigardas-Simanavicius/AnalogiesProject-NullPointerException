package org.main;

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
}
