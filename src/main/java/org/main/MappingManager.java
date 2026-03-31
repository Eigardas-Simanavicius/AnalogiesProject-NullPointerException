package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class MappingManager {
    public static Boolean canMap(Predicate head1, Predicate head2){

        Iterator<AnalogicalObject> struct1 = ((Clause) head1).getPreOrderIterator();
        Iterator<AnalogicalObject> struct2 = ((Clause) head2).getPreOrderIterator();
        AnalogicalObject curr1 = null;
        AnalogicalObject curr2 = null;

        while (struct1.hasNext()){
            curr1 = struct1.next();
            curr2 = struct2.next();

            if(isClause(curr1) && isClause(curr2)){
                if(!(sameNames((Clause) curr1,(Clause) curr2))){
                    return false;
                }
            }else if(isSubject(curr1) && isSubject(curr2)){
                if(bothHaveAsterisks((Subject) curr1, (Subject) curr2)){
                  return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }

    private static boolean isClause(AnalogicalObject clause){
        return  clause.getClass().getName().equals(Clause.class.getName());
    }

    private static boolean isSubject(AnalogicalObject subject){
        return  subject.getClass().getName().equals(Subject.class.getName());
    }

    private static boolean bothHaveAsterisks(Subject a,Subject b){
        return a.isHasAsterisk() && b.isHasAsterisk();
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
}
