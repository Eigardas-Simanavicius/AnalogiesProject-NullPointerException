package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;

import java.util.Iterator;

public class MappingManager {
    public static Boolean canMap(Predicate head1, Predicate head2){
        Iterator<AnalogicalObject> struct1 = ((Clause) head1).getPreOrderIterator();
        Iterator<AnalogicalObject> struct2 = ((Clause) head1).getPreOrderIterator();

        while (struct1.hasNext()){
            AnalogicalObject curr1 = struct1.next();
            AnalogicalObject curr2 = struct2.next();
            System.out.println(curr1.getName() + " " + curr2.getName());
            System.out.println(curr1.getClass() + " " + curr2.getClass());
            // this does not look create right now, will fix later
            if(isClause(curr1) && isClause(curr2)){
                if(!(((Clause) curr1).getName().equals(((Clause) curr2).getName()))){
                    return false;
                }
            }else if(isSubject(curr1) && isSubject(curr2)){
                if((((Subject) curr1).isHasAsterisk() && ((Subject) curr2).isHasAsterisk())){
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

}
