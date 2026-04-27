package org.main;

import org.main.Objects.CoalescentMapping;
import org.main.Objects.Subject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompositeBuilder {
    private ConcurrentHashMap<Double, ArrayList<String>> sourceAnalogiesMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Double, ArrayList<String>> targetAnalogiesMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Double, ArrayList<ArrayList<Pair>>> pairMap = null;
    private static final Logger logger = Logger.getLogger(CompositeBuilder.class.getName());
    private HashMap<String, String> forwardMap;
    private HashMap<String, String> backwardMap;

    //runs N times, starting from the next highest richness value every time
    public ArrayList<String> buildCompositeAnalogy(String source, String target){
        setup(source, target);
        List<Double> index = this.sourceAnalogiesMap.keySet().stream().sorted().toList(); //controls which source analogy is currently being looked at
        return(searchOnce(index, 0, 0));
    }

    public ArrayList<ArrayList<String>> buildMultipleCompositeAnalogies(String source, String target, int N){
        setup(source, target);
        List<Double> index = this.sourceAnalogiesMap.keySet().stream().sorted().toList().reversed(); //controls which source analogy is currently being looked at

        ArrayList<ArrayList<String>> listOfComposites = new ArrayList<>();

        //effectively 2D array coordinates
        int startI = 0;
        int startJ = 0;

        for(int i = 0; i < N; i++){
            if(startI >= index.size()){
                logger.log(Level.WARNING, "Requested more permutations than possible, stopping early.");
                break;
            }
            ArrayList<String> arr = searchOnce(index, startI, startJ);
            //if the set of analogies tied to the current richness is greater than startJ, increment J instead of I
            //Basically makes it so that the greedy searches travel down the source analogies one analogy at a time, instead of one richness level at a time
            //Can be thought of as flattening the source analogies map values into one long array.
            if(sourceAnalogiesMap.get(index.get(startI)).size() > startJ+1){ //+1 to avoid index out of bounds errors
                startJ++;
            }
            else{
                startI++;
            }
            if(arr != null) {
                listOfComposites.add(arr);
            }
        }
        pairMap.clear();
        return listOfComposites;
    }

    private ArrayList<String> searchOnce(List<Double> index, int startI, int startJ){
        forwardMap.clear();
        backwardMap.clear();
        ArrayList<String> compositeAnalogy = new ArrayList<>();
        ArrayList<Pair> mappingPairs = new ArrayList<>();
        boolean wasReset = false;
        //Iterates over richness maps
        for (int i = startI; i < index.size(); i++) {
            //avoid adding duplicates
            if(wasReset && i == startI){
                continue;
            }
            double currIndex = index.get(i); //richness of the current set of source/target to be fetched from the maps
            ArrayList<String> currentSources = this.sourceAnalogiesMap.get(currIndex);
            ArrayList<String> currentTargets = this.targetAnalogiesMap.get(currIndex);
            if(currentTargets.size() > currentSources.size()){
                logger.log(Level.INFO, "More targets than sources for richness " + currIndex + ", some targets will be ignored.");
            }
            boolean firstLoopReset = false;
            for (int j = 0; j < currentSources.size(); j++) {
                //if this is the first outer loop, set j to its correct initial index
                if(!firstLoopReset && i == startI){
                    j = startJ;
                    firstLoopReset = true;
                }
                if (j-startJ >= currentTargets.size()) {
                    break;
                }
                //checks if two current analogies can be added to the composite, if yes then adds them (and adds all of their subject mappings to the subject map)
                if (coalesce(currentSources.get(j), currentTargets.get(j-startJ))) {
                    compositeAnalogy.add(currentSources.get(j));
                    compositeAnalogy.add(currentTargets.get(j-startJ));
                    mappingPairs.add(new Pair(currIndex,j));
                }
            }
            //considers all analogies "above" the one we started with
            //Since we're often starting with not the richest analogy, this goes back up the list and checks the richer ones as well.
            if(!wasReset && startI != 0){ //avoid resetting if we start at 0
                i = -1;
                wasReset = true;
            }
        }

        if(notDuplicate(mappingPairs)) {
            return compositeAnalogy;
        }else {
            return null;
        }

    }

    private void setup(String source, String target){
        ArrayList<String> sourceAnalogiesArr = AnalogyDataHolder.getAnalogiesFor(source);
        ArrayList<String> targetAnalogiesArr = AnalogyDataHolder.getAnalogiesFor(target);
        this.sourceAnalogiesMap = mapByRichness(sourceAnalogiesArr);
        this.targetAnalogiesMap = mapByRichness(targetAnalogiesArr);
        pairMap = new ConcurrentHashMap<>();
        sanitizeInputAnalogies();
        this.forwardMap = new HashMap<>(); //resets static hashmap of subjects
        this.backwardMap = new HashMap<>();
    }

    //makes sure there are no "unmappable" analogies in either set, by comparing structure richness
    private void sanitizeInputAnalogies(){
        for(Double key : this.sourceAnalogiesMap.keySet()){
            if(this.targetAnalogiesMap.get(key) == null){
                this.sourceAnalogiesMap.remove(key);
            }
        }
        for(Double key : this.targetAnalogiesMap.keySet()){
            if(this.sourceAnalogiesMap.get(key) == null){
                this.targetAnalogiesMap.remove(key);
            }
        }

    }

    private static ConcurrentHashMap<Double, ArrayList<String>> mapByRichness(ArrayList<String> inputAnalogies){
        ConcurrentHashMap<Double, ArrayList<String>> richnessMap = new ConcurrentHashMap<>();
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

    private boolean coalesce(String source, String target){
        //mapAnalogies also checks if they two inputs are mappable, extra sanitation not needed
        HashMap<Subject, Subject> mapping = MappingManager.mapAnalogies(AnalogyManager.ConvertToOOP(source), AnalogyManager.ConvertToOOP(target));
        if(mapping == null){
            return false;
        }

        for(Subject subject : mapping.keySet()){
            String firstSubject = subject.getName();
            String secondSubject = mapping.get(subject).getName();
            if(this.forwardMap.get(firstSubject) != null){
                //Checks that subject from analogy set 1 always maps to the same subject in analogy set 2. Also checks that subjects from set 2 always map to the same subject in set 1
                if(!(secondSubject.equals(this.forwardMap.get(firstSubject))) || !(firstSubject.equals(this.backwardMap.get(secondSubject)))){
                    return false;
                }
            }
        }

        for(Subject subject : mapping.keySet()){
            String subjectName = subject.getName();
            this.forwardMap.put(subjectName.intern(), mapping.get(subject).getName().intern());
            this.backwardMap.put(mapping.get(subject).getName().intern(), subjectName.intern());
        }
        return true;
    }

    private boolean notDuplicate(ArrayList<Pair> mappingPairs){
        double hash = pairValue(mappingPairs);
        if(!pairMap.containsKey(hash)){
            pairMap.put(hash,new ArrayList<>());
            pairMap.get(hash).add(mappingPairs);
            return true;
        }else{
            if(checkIfDuplicate(mappingPairs,pairMap.get(hash))){
                return false;
            }else{
                pairMap.get(hash).add(mappingPairs);
                return true;
            }
        }
    }
    // the two mapping can only be the same if both pairs have the same hash so to speak, we will use this to wittle down our options
    private double pairValue(ArrayList<Pair> mapping){
        double total = 0;
        for(Pair pair: mapping){
            total += pair.getRichness();
        }
        return total;
    }

    // this will check the new pairList against any exisitng to ones to make sure the new one is unique
    private boolean checkIfDuplicate(ArrayList<Pair> mappingPairs,ArrayList<ArrayList<Pair>> possibleDuplicates){
        Comparator<Pair> comparator = new sortPairs();
        mappingPairs.sort(comparator);
        boolean isDuplicate = true;
        for(ArrayList<Pair> list : possibleDuplicates) {
            if (list.size() == mappingPairs.size()) {
                isDuplicate = true;
                list.sort(comparator);
                for (int i = 0; i < mappingPairs.size(); i++) {
                    if(!mappingPairs.get(i).equals(list.get(i))){
                        isDuplicate = false;
                        break;
                    }
                }
            }
        }
        return isDuplicate;
    }


    // represent the pair the two analogies mapped together, the numbers representing their location in their respective analogies arralists
    private class Pair{
        double richness;
        int target;

        public Pair(Double s, int t){
            richness = s;
            target = t;
        }
        public double getRichness(){
            return richness;
        }
        public int getTarget(){
            return target;
        }

        public boolean equals(Pair p){
            return this.richness == p.getRichness() && this.target == p.getTarget();
        }
        public String toString(){
            return "<" + richness + " " + target + ">";
        }
    }

    private class sortPairs implements Comparator<Pair>{

        @Override
        public int compare(Pair a, Pair b) {
            return Double.compare(a.getRichness(), b.getRichness());

        }
    }


}
