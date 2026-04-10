package org.main;
import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;
import org.main.Interfaces.Rule;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.logging.Logger;


//README!!
//The written rule structure is as follows:
// [original predicate]         {modifiers}[verbPredicate]_[prepositionPredicate]:[newArgument]{optional asterisk}&[byArgument]
//Hope this helps when writing a parser

public class RewriteRule implements Rule {
    private String originalPredicate;
    private String verbPredicate;
    private String prepositionPredicate;
    private String byArgument;
    private String newArgument;
    private Boolean newArgumentHasAsterisk;
    private Boolean negation;
    private Boolean exponent;
    private Boolean lessThan;

    private static final Logger logger = Logger.getLogger(RewriteRule.class.getName());

    public RewriteRule(String originalPredicate, String rule){
        // ^<!provide_to:benefactor*&denying

        this.originalPredicate = originalPredicate;

        List<String> ruleSubParts = List.of(rule.split("[:&]"));


        if(ruleSubParts.size() != 3){
            for (String s: ruleSubParts){
                System.out.println(s);
            }
            throw new InvalidParameterException("The rewrite rule given has an invalid structure.");
        }

        for(String ruleSubPart : ruleSubParts){
            if(ruleSubPart.isBlank()){
                throw new InvalidParameterException("The rewrite rule given has an invalid structure.");
            }
        }

        List<String> predicatePair = (List.of(ruleSubParts.getFirst().split("_",2)));

        if(predicatePair.size() != 2){
            throw new InvalidParameterException("The rewrite rule given has an invalid structure");
        }

        verbPredicate = predicatePair.getFirst();

        negation = verbPredicate.contains("!");
        exponent = verbPredicate.contains("^");
        lessThan = verbPredicate.contains("<");

        verbPredicate = verbPredicate.replaceAll("[!<^]","");

        prepositionPredicate = predicatePair.get(1);

        newArgument = ruleSubParts.get(1);

        newArgumentHasAsterisk = newArgument.contains("*");
        newArgument = newArgument.replaceAll("\\*","");

        byArgument = ruleSubParts.get(2);
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


    public Predicate rewrite(Predicate source){
        if(!validatePredicate(source)){
            System.out.println("Input validation failed, check log file for details.");
            return null;
        }

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

    private boolean validatePredicate(Predicate source){
        if(source == null){
            logger.warning("Null input passed as predicate input to rewriteRule");
            return false;
        }
        if(!source.getName().equals(this.originalPredicate)){
            logger.warning("Rule not applicable to input predicate, as predicates are mismatched.");
            return false;
        }
        if(source.getChildren().size() > 2){
            logger.warning("Input predicate has more than 2 children, rule does not apply");
            return false;
        }
        return true;
    }

}