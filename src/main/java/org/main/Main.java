package org.main;

import org.main.Interfaces.Predicate;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IllegalArgumentException {

        System.out.println("Flat Abstract String");
        System.out.println(AnalogyManager.convertToAbstractString(AnalogyManager.ConvertToOOP("(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))"),false));

        System.out.println("\nPrettified Abstract String");
        System.out.println(AnalogyManager.convertToAbstractString(AnalogyManager.ConvertToOOP("(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))"),true));


        System.out.println("Converting to OOP and then back to String");
        //System.out.println(AnalogyManager.ConvertToString(AnalogyManager.ConvertToOOP("(work in scientist (some lab (that (conduct experiment))))"),true));

        // [original predicate]         {modifiers}[verbPredicate]_[prepositionPredicate]:[newArgument]{optional asterisk}&[byArgument]
        RewriteRule rule1 = new RewriteRule("exercise","preform_of:exercise*&exercising");
        RewriteRule rule2 = new RewriteRule("explode","destroy_of:explode*&exploding");
        RewriteRule rule3 = new RewriteRule("explode","boom_of:boom*&booming");
        RewriteRule rule4 = new RewriteRule("walk","run_of:sprint*&sprinting");
        RewriteRule rule5 = new RewriteRule("walk","fly_with:fly*:flying");

        ArrayList<RewriteRule> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        rules.add(rule3);
        rules.add(rule4);
        rules.add(rule5);
        Clause testClause2 = (Clause)AnalogyManager.ConvertToOOP("(Sigma male (exercise.0 athelete muscle) (explode Gregs legs) (walk Steve road) (explode gregs feet))");
        ArrayList<Predicate> ans = ReWriter.reWriteAnalogyAllPermuatations(rules,testClause2);
        for (Predicate pred: ans){
            System.out.println(pred.toString());
        }

    }
}
