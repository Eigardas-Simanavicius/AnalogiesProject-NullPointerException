package org.main;

import com.sun.jdi.event.ClassUnloadEvent;
import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;
import org.main.Interfaces.Rule;

import java.util.ArrayList;


//README!!
//The written rule structure is as follows:
// [original predicate]         {modifiers}[verbPredicate]_[prepositionPredicate]:[newArgument]{optional asterisk}&[byArgument]
//Hope this helps when writing a parser

public class rewriteRule implements Rule {
    private String originalRule;
    private String originalPredicate;
    private String verbPredicate;
    private String prepositionPredicate;
    private String byArgument;
    private String newArgument;
    private Boolean newArgumentHasAsterisk;
    private Boolean negation = false;
    private Boolean exponent = false;
    private Boolean lessThan = false;

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
        return negation || exponent || lessThan;
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
    public String getByArgument() {
        return byArgument;
    }

    @Override
    public String getPrepositionPredicate() {
        return prepositionPredicate;
    }

    public void testConstructor(String origPred, String verb, String preposition, String byArg, String modifier, String newArg, Boolean newArgAsterisk){
        this.originalPredicate = origPred;
        this.verbPredicate = verb;
        this.prepositionPredicate = preposition;
        this.byArgument = byArg;
        this.newArgument = newArg;
        this.newArgumentHasAsterisk = newArgAsterisk;
        if(modifier != null) {
            if (modifier.equals("!")) {
                this.negation = true;
            } else if (modifier.equals("^")) {
                this.exponent = true;
            } else if (modifier.equals("<")) {
                this.lessThan = true;
            }
        }
    }


    public Predicate rewrite(Predicate source){
        validatePredicate(source);

        //instantiate all "building blocks" for the resulting structure
        Clause output = new Clause("by");
        output.addEmbedded(new Subject(byArgument));
        Clause secondClause = new Clause(verbPredicate);
        Clause thirdClause = new Clause(prepositionPredicate);
        AnalogicalObject firstArg = source.getChildren().get(0);
        AnalogicalObject secondArg = source.getChildren().get(1);

        //swaps arguments around if the '<' modifier is present
        if(lessThan){
            firstArg = source.getChildren().get(1);
            secondArg = source.getChildren().get(0);
        }

        //asterisk and '^' processing is bundled together, because they seem to be mutually exclusive
        if(newArgumentHasAsterisk) {
            secondClause.addEmbedded(firstArg);
            secondClause.addEmbedded(new Subject(newArgument));
            thirdClause.addEmbedded(secondArg);
        }
        else if(exponent){
            secondClause.addEmbedded(new Subject(newArgument));
            secondClause.addEmbedded(firstArg);
            thirdClause.addEmbedded(secondArg);
        }
        else{
            secondClause.addEmbedded(firstArg);
            secondClause.addEmbedded(secondArg);
            thirdClause.addEmbedded(new Subject(newArgument));
        }

        //wraps everything in negation if necessary
        if(negation){
            Clause negateWrapper = new Clause("not");
            output.addEmbedded(negateWrapper);
            negateWrapper.addEmbedded(secondClause);
        }
        else{
            output.addEmbedded(secondClause);
        }

        secondClause.addEmbedded(thirdClause);
        return output;
    }

    private void validatePredicate(Predicate source){
        if(!source.getName().equals(this.originalPredicate)){
            throw new IllegalArgumentException("Predicates do not match between rule and source");
        }
        if(source.getChildren().size() > 2){
            throw new IllegalArgumentException("Predicate has more than 2 children, processing undefined");
        }
    }

}
