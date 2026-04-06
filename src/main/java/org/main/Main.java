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

        Clause h = (Clause) AnalogyManager.ConvertToOOP("(1 2 (3 4 (5 (6 (7 (8 9)) (10 11)))))");
        Clause h2 = (Clause) AnalogyManager.ConvertToOOP("(1 2 (3 d (5 (6 (7 (8 i)) (10 k)))))");
        Iterator<AnalogicalObject> it = h.getPreOrderIterator();
        while (it.hasNext()){
            String n = it.next().getName();
            System.out.println(n);
        }
        System.out.println("\n");



       HashMap<String,String> map =  MappingManager.flatStringMapping("(1 2 (3 *4 (5 (6 (7 (8 9)) (10 11)))))","(1 2 (3 *d (5 (6 (7 (8 i)) (10 k)))))");
       for(String s: map.keySet()){
           System.out.println("key: " + s + " value: " + map.get(s));
       }

       rewriteRule rule1 = new rewriteRule("exercise perform_of");
       rule1.testConstructor("exercise", "perform", "of", "exercising", null, "exercise", true);
       Clause testClause = (Clause)AnalogyManager.ConvertToOOP("(exercise athelete muscle)");
        System.out.println(((Clause)rule1.rewrite(testClause)).toIndentedString());
    }
}
