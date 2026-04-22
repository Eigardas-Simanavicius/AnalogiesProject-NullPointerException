package org.main;

import org.main.Objects.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompositeBuilder {
    private static HashMap<Double, ArrayList<String>> sourceAnalogiesMap = new HashMap<>();
    private static HashMap<Double, ArrayList<String>> targetAnalogiesMap = new HashMap<>();
    private static final Logger logger = Logger.getLogger(CompositeBuilder.class.getName());
    private static HashMap<Subject, Subject> forwardMap;
    private static HashMap<Subject, Subject> backwardMap;

    //runs N times, starting from the next highest richness value every time
    public static ArrayList<String> buildCompositeAnalogy(String source, String target){
        setup(source, target);
        List<Double> index = sourceAnalogiesMap.keySet().stream().sorted().toList(); //controls which source analogy is currently being looked at
        return(searchOnce(index, 0));
    }

    public static ArrayList<ArrayList<String>> buildMultipleCompositeAnalogies(String source, String target, int N){
        setup(source, target);
        List<Double> index = sourceAnalogiesMap.keySet().stream().sorted().toList(); //controls which source analogy is currently being looked at

        if(N > index.size()){
            logger.log(Level.WARNING, "Requested more greedy runs than there are mappable analogies, defaulting to maximum possible value");
            N = index.size();
        }

        ArrayList<ArrayList<String>> listOfComposites = new ArrayList<>();

        for(int i = 0; i < N; i++){
            listOfComposites.add(searchOnce(index, i));
        }

        return listOfComposites;
    }

    private static ArrayList<String> searchOnce(List<Double> index, int start){
        ArrayList<String> compositeAnalogy = new ArrayList<>();
        boolean wasReset = false;

        //Iterates over richness maps
        for (int i = start; i < index.size(); i++) {
            //avoid adding duplicates
            if(wasReset && i == start){
                continue;
            }
            double currIndex = index.get(i);
            ArrayList<String> currentSources = sourceAnalogiesMap.get(currIndex);
            ArrayList<String> currentTargets = targetAnalogiesMap.get(currIndex);
            for (int j = 0; j < currentSources.size(); j++) {
                if (j >= currentTargets.size()) {
                    break;
                }
                //checks if two current analogies can be added to the composite, if yes then adds them (and adds all of their subject mappings to the subject map)
                if (coalesce(currentSources.get(j), currentTargets.get(j))) {
                    compositeAnalogy.add(currentSources.get(j));
                    compositeAnalogy.add(currentTargets.get(j));
                }
            }
            //considers all analogies "above" the one we started with
            //Since we're often starting with not the richest analogy, this goes back up the list and checks the richer ones as well.
            if(!wasReset && start != 0){ //avoid resetting if we start at 0
                i = -1;
                wasReset = true;
            }
        }

        return compositeAnalogy;
    }

    private static void setup(String source, String target){
        ArrayList<String> sourceAnalogiesArr = AnalogyDataHolder.getAnalogiesFor(source);
        ArrayList<String> targetAnalogiesArr = AnalogyDataHolder.getAnalogiesFor(target);
        sourceAnalogiesMap = mapByRichness(sourceAnalogiesArr);
        targetAnalogiesMap = mapByRichness(targetAnalogiesArr);
        sanitizeInputAnalogies();

        forwardMap = new HashMap<>(); //resets static hashmap of subjects
        backwardMap = new HashMap<>();
    }

    //makes sure there are no "unmappable" analogies in either set, by comparing structure richness
    private static void sanitizeInputAnalogies(){
        for(Double key : sourceAnalogiesMap.keySet()){
            if(targetAnalogiesMap.get(key) == null){
                sourceAnalogiesMap.remove(key);
            }
        }
        for(Double key : targetAnalogiesMap.keySet()){
            if(sourceAnalogiesMap.get(key) == null){
                targetAnalogiesMap.remove(key);
            }
        }

    }

    private static HashMap<Double, ArrayList<String>> mapByRichness(ArrayList<String> inputAnalogies){
        HashMap<Double, ArrayList<String>> richnessMap = new HashMap<>();
        for(String analogy : inputAnalogies){
            double richness = AnalogyManager.getPredicateRichness(AnalogyManager.ConvertToOOP(analogy));
            ArrayList<String> arr;

            if(richnessMap.get(richness) == null){
                arr = new ArrayList<>();
            }
            else{
                arr = richnessMap.get(richness);
            }
            arr.add(analogy);
            richnessMap.put(richness,arr);
        }
        return richnessMap;
    }

    private static boolean coalesce(String source, String target){
        //mapAnalogies also checks if they two inputs are mappable, extra sanitation not needed
        HashMap<Subject, Subject> mapping = MappingManager.mapAnalogies(AnalogyManager.ConvertToOOP(source), AnalogyManager.ConvertToOOP(target));
        if(mapping == null){
            return false;
        }

        for(Subject subject : mapping.keySet()){
            Subject secondSubject = backwardMap.get(subject);
            if(secondSubject != null){
                //Checks that subject from analogy set 1 always maps to the same subject in analogy set 2. Also checks that subjects from set 2 always map to the same subject in set 1
                if(!(secondSubject.equals(forwardMap.get(subject))) || !(subject.equals(backwardMap.get(secondSubject)))){
                    return false;
                }
            }
        }

        for(Subject subject : mapping.keySet()){
            forwardMap.put(subject, mapping.get(subject));
            backwardMap.put(mapping.get(subject), subject);
        }
        return true;
    }


}
