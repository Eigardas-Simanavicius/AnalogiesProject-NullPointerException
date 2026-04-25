package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;
import org.main.Objects.Clause;
import org.main.Objects.CoalescentMapping;
import org.main.Objects.Subject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MappingManager {
    private static HashMap<String,ArrayList<CoalescentMapping>> coalesentMappings = new HashMap<>();

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
        // Gets analogies for the target topic
        final ArrayList<Predicate> targetAnalogies = new ArrayList<>(
                AnalogyDataHolder
                        .getAnalogiesFor(targetTopic)
                        .stream()
                        .map(AnalogyManager::ConvertToOOP)
                        .toList()
        );

        ArrayList<Predicate> sourceAnalogies = new ArrayList<>();

        // Creates sourceTopics with concepts that can be mapped to the target topic (Also removes the * that in the topic names)
        ArrayList<String> sourceTopics = new ArrayList<>(
                AnalogyDataHolder
                        .getMappableConcepts(targetTopic)
                        .stream()
                        .map(x -> x.replaceFirst("\\*",""))
                        .toList()
        );

        // For each sourceTopic, gets the analogies for that topic, converts them to OOP representation and stores them in sourceAnalogies
        for(String source: sourceTopics){
            sourceAnalogies
                    .addAll(
                            AnalogyDataHolder.getAnalogiesFor(source)
                            .stream()
                            .map(AnalogyManager::ConvertToOOP)
                            .toList()
                    );
        }

        // Filters sourceAnalogies for source analogies that can be mapped to any target analogies, and then gets their richness (Parallelized using parallel stream)
        ArrayList<AbstractMap.SimpleEntry<Double,Predicate>> mappableAnalogies = new ArrayList (
                sourceAnalogies
                        .parallelStream()
                        .filter(x -> targetAnalogies.stream().anyMatch(y -> MappingManager.canMap(x,y)))
                        .map(y -> new AbstractMap.SimpleEntry<Double,Predicate>(AnalogyManager.getPredicateRichness(y),y))
                        .toList()
        );

        // Sorts analogies based on their richness
        mappableAnalogies.sort(
                (x,y)->
                Double.compare(y.getKey(),x.getKey())
        );

        // Removes the calculated richness values from the analogies and returns them in order
        return new ArrayList<>(mappableAnalogies.stream().map(AbstractMap.SimpleEntry::getValue).toList());
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

    public static int getMappingRichness(ArrayList<String> mapping){
        int richness = 0;
        for (int i = 0; i < mapping.size(); i+=2) {
            richness += flatStringMapping(mapping.get(i),mapping.get(i+1)).size();
        }
        return richness;
    }

    public static void createNewCoalesentMapping(String source, String target){
        if(!coalesentMappings.containsKey(source)){
            coalesentMappings.put(source,new ArrayList<CoalescentMapping>());
        }
        coalesentMappings.get(source).add(new CoalescentMapping(source,target));
    }

    public static ArrayList<ArrayList<String>>  rankBestAnalogiesSingleTarget(String source, String target){
        CompositeBuilder compBuilder = new CompositeBuilder();
        ArrayList<ArrayList<String>> mappings = compBuilder.buildMultipleCompositeAnalogies(source,target,5);
        mappings.sort(
                Comparator.comparingInt(MappingManager::getMappingRichness)
        );
        return mappings;
    }

    public static ArrayList<CoalescentMapping> rankBestCoalesentMapping(String source){
        ArrayList<CoalescentMapping> mappings = coalesentMappings.get(source);
        mappings.sort(
                Comparator.comparingInt(CoalescentMapping::getRichness)
        );
        return mappings;
    }

    public static ArrayList<String> getNBestSourcesFor(String targetTopic, int n){
        if(n <= 0)return new ArrayList<>();
        if(targetTopic.isBlank()) return new ArrayList<>();

        ArrayList<CoalescentMapping> sourceTopicMappings = new ArrayList<>(AnalogyDataHolder
                .getMappableConcepts(targetTopic)
                .parallelStream()
                .map(
                        x -> new CoalescentMapping(x,targetTopic)
                )
                .toList()
        );

        sourceTopicMappings.sort(Comparator.comparingDouble(CoalescentMapping::getRichness));


        return new ArrayList<>(sourceTopicMappings.stream().limit(n).map(CoalescentMapping::getSource).toList());
    }
}
