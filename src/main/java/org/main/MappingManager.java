package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;
import org.main.Objects.Clause;
import org.main.Objects.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class MappingManager {
    public static Boolean canMap(Predicate head1, Predicate head2){
        if(head1 == null || head2 == null){
            System.out.println("One of the input predicates is null, cannot attempt mapping.");
            return false;
        }

        Iterator<AnalogicalObject> struct1 = ((Clause) head1).getPreOrderIterator();
        Iterator<AnalogicalObject> struct2 = ((Clause) head2).getPreOrderIterator();
        AnalogicalObject curr1 = null;
        AnalogicalObject curr2 = null;

        while (struct1.hasNext()){
            if(!struct2.hasNext()){
                return false;
            }
            curr1 = struct1.next();
            curr2 = struct2.next();

            if(isClause(curr1) && isClause(curr2)){
                if(!(sameNames((Clause) curr1,(Clause) curr2))){
                    return false;
                }
            }else if(isSubject(curr1) && isSubject(curr2)){
                if(!asterisksMatch((Subject) curr1, (Subject) curr2)){
                  return false;
                }
            }else{
                return false;
            }
        }
        return !struct2.hasNext();
    }

    private static boolean isClause(AnalogicalObject clause){
        return  clause.getClass().equals(Clause.class);
    }

    private static boolean isSubject(AnalogicalObject subject){
        return  subject.getClass().equals(Subject.class);
    }

    private static boolean asterisksMatch(Subject a, Subject b){
        return a.isHasAsterisk() == b.isHasAsterisk();
    }

    private static boolean sameNames(Clause a,Clause b){
        return (a).getName().equals((b).getName());
    }


    public static HashMap<Subject,Subject> mapAnalogies(Predicate head1, Predicate head2){
        if(canMap(head1,head2)){
            Iterator<AnalogicalObject> struct1 = ((Clause) head1).getPreOrderIterator();
            Iterator<AnalogicalObject> struct2 = ((Clause) head2).getPreOrderIterator();
            HashMap<Subject,Subject> mapping = new HashMap<>();
            while (struct1.hasNext()){
                AnalogicalObject obj1 = struct1.next();
                AnalogicalObject obj2 = struct2.next();
                if(isSubject(obj1)) {
                    mapping.put((Subject) obj1, (Subject) obj2);
                }
            }
            return mapping;
        }
        else{
            return null;
        }
    }

    public static void printMapping(HashMap<Subject,Subject> mapping){
        for (Subject s: mapping.keySet() ){
            System.out.println("key: " + s.getName() + " value: " + mapping.get(s).getName());
        }
    }


    public static HashMap<String,String> flatStringMapping(String s1, String s2){
        if(s1 == null || s2 == null){
            throw new IllegalArgumentException("Null inputs are not allowed");
        }

        HashMap<String,String> mapping = new HashMap<>();
        String[] words1 = (s1.replace(")","").split("\\("));
        char[] brackets1 = s1.replaceAll("[^()]", "").toCharArray();

        String[] words2 = (s2.replace(")","").split("\\("));
        char[] brackets2 = s2.replaceAll("[^()]", "").toCharArray();

        String[] currwords;
        String[] currwords2;


        if(!bracketMatch(brackets1,brackets2)){
            return null;
        }

        //Starts at 1 because split adds a blank string at first, and only half the brackets since we need to ignore closing brackets, and then +1 to compensate for the starting at 1
        for(int i = 1; i < ((brackets1.length/2) + 1);i++){
            currwords = words1[i].split(" ");
            currwords2 = words2[i].split(" ");
            if(currwords2.length != currwords.length || currwords.length < 1){
                return null;
            }
            if(!currwords[0].equals(currwords2[0])){
                return null;
            }
            for(int j = 1; j < currwords.length;j++){
                if(checkEquality(currwords[j],currwords2[j])){
                    mapping.put(currwords[j],currwords2[j]);
                }else {
                    return null;
                }
            }
        }
        return mapping;
    }

    public static ArrayList<Predicate> getMappableSourceAnalogiesFor(String targetTopic){
        ArrayList<String> sourceTopics = new ArrayList<>(AnalogyDataHolder.getMappableConcepts(targetTopic).stream().map(x -> x.replaceAll("\\*","")).toList());

        ArrayList<String> targetAnalogies = AnalogyDataHolder.getAnalogiesFor(targetTopic);
        TreeMap<Double,Predicate> mappableSourceAnalogies = new TreeMap<>();

        ArrayList<String> sourceAnalogies;

        for(String sourceTopic : sourceTopics){
            if(sourceTopic == null)continue;
            sourceAnalogies = AnalogyDataHolder.getAnalogiesFor(sourceTopic);
            for(String sourceAnalogy : sourceAnalogies){
                if(sourceAnalogy == null)continue;
                for(String targetAnalogy : targetAnalogies){
                    if(targetAnalogy == null)continue;
                    Predicate a = AnalogyManager.ConvertToOOP(sourceAnalogy);
                    if(canMap(a,AnalogyManager.ConvertToOOP(targetAnalogy))){
                        mappableSourceAnalogies.put(AnalogyManager.getPredicateRichness(a),a);
                    }
                }
            }
        }

        return new ArrayList<>(mappableSourceAnalogies.values());
    }

    private static boolean bracketMatch(char[] brackets1,char[] brackets2){
        if(brackets1.length != brackets2.length){
            return false;
        }
        for (int i = 0; i < brackets1.length; i++) {
            if(brackets1[i] != brackets2[i]){
                return false;
            }
        }
        return true;
    }
    // right now this will only check asterisks, im sure there will be more conditions in the future
    private static boolean checkEquality(String s1,String s2){
        return (s1.toCharArray()[0] == '*') == (s2.toCharArray()[0] == '*');
    }
}
