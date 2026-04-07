package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws IllegalArgumentException {

        System.out.println("Flat Abstract String");
        System.out.println(AnalogyManager.convertToAbstractString(AnalogyManager.ConvertToOOP("(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))"),false));

        System.out.println("\nPrettified Abstract String");
        System.out.println(AnalogyManager.convertToAbstractString(AnalogyManager.ConvertToOOP("(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))"),true));


        System.out.println("Converting to OOP and then back to String");
        //System.out.println(AnalogyManager.ConvertToString(AnalogyManager.ConvertToOOP("(work in scientist (some lab (that (conduct experiment))))"),true));

        // [original predicate]         {modifiers}[verbPredicate]_[prepositionPredicate]:[newArgument]{optional asterisk}&[byArgument]
        rewriteRule rule1 = new rewriteRule("exercise","preform_of:exercise*&exercising");
        rewriteRule rule2 = new rewriteRule("explode","destroy_of:explode*&exploding");
        rewriteRule rule3 = new rewriteRule("explode","boom_of:explode*:booming");
//        rule1.testConstructor("exercise", "perform", "of", "exercising", null, "exercise", true);
//        rule2.testConstructor("explode","destroy","of","exploding",null,"explode",true);
//        rule3.testConstructor("explode","boom","of","booming",null,"explode",true);
        ArrayList<rewriteRule> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        Clause testClause2 = (Clause)AnalogyManager.ConvertToOOP("(Sigma male (Whopper jr) (exercise.0 athelete muscle (big mac)) (explode Gregs legs))");
        ArrayList<Predicate> ans = ReWriter.reWriteAnalogyAllPermuatations(rules,testClause2);
        System.out.println(ans.size());
        System.out.println(ans.toString());
        System.out.println(((Clause) (ans.getFirst())).toIndentedString());
    }
}
