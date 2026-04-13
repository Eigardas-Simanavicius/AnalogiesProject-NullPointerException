package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;
import org.main.Objects.Clause;
import org.main.Objects.RewriteRule;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReWriter {
    private static final Logger logger = Logger.getLogger(ReWriter.class.getName());
    // This takes an analogy and re-writes according to the provided rule set
    private static Predicate reWriteAnalogy(LinkedHashMap<String,ArrayList<RewriteRule>> rulesMap, Predicate source, int[] currCount, ArrayList<Integer> locations)  {
        Predicate reWrite = AnalogyManager.ConvertToOOP(source.toString());
        ArrayList<Predicate> predicates = reWrite.getAllChildren();
        Predicate curr = null;
        // we just fetch the predicates we need to rewrite and apply the rules, we use count to know which rule to apply from the list
        for(int i = 0; i < locations.size();i++){
            curr = predicates.get(locations.get(i));
            replacePredicate(curr,rulesMap.get(curr.getName()).get(currCount[i]));
        }

        return reWrite;
    }

    private static void replacePredicate(Predicate curr, RewriteRule rule){
        ArrayList<AnalogicalObject> children = null;
        Predicate replacement = null;

        Predicate parent = curr.getParent();
        curr.setParent(null);
        children = ((Clause) curr).getClauseChildren();
        ((Clause) curr).removeClauses();
        parent.getChildren().remove(curr);
        replacement = rule.rewrite((Predicate) curr);
        if(replacement != null) {
            replacement.setParent(parent);
            replacement.addAllEmbedded(children);
            parent.addEmbedded(replacement);
        }else{

            logger.log(Level.WARNING, "rules rewrite failed, predicate " + curr.getName() + " will not be rewritten with rule " + rule);
        }
    }
    // this is the main controller function,
    public static ArrayList<Predicate> reWriteAnalogyAllPermuatations(ArrayList<RewriteRule> rules, Predicate source)  {

        removeNumbers((Clause) source);
        // Linked hash maps ensure items are returned in order of insertion, very important here
        LinkedHashMap<String,ArrayList<RewriteRule>> rulesMap = mapAllRules(rules,source);
        if(rulesMap.isEmpty()){
            logger.log(Level.WARNING, "no relevant rules for analogy " + source + " returning nothing");
            return null;
        }
        ArrayList<Integer> targets = findPredicatestoChange(source,rulesMap);
        ArrayList<Predicate> permutations = new ArrayList<>();
        int[] maxCount = createMaxCount(targets,source,rulesMap);
        int permutationCount = getPermutationsCount(maxCount);
        int[] currCount = new int[targets.size()];

        for (int i = 0; i < permutationCount; i++) {
            permutations.add(reWriteAnalogy(rulesMap, AnalogyManager.ConvertToOOP(source.toString()),currCount,targets));
            updatePermutation(currCount,currCount.length-1,maxCount);
        }


        return permutations;
    }
    // we need to know what predicates inside the analogy the rules will be applied to, since the structure is the same evertime thsis how we will do it
    private static ArrayList<Integer> findPredicatestoChange(Predicate head,LinkedHashMap<String,ArrayList<RewriteRule>> rules){
        ArrayList<Integer> locations = new ArrayList<Integer>();
        Set<String> keys = rules.keySet();
        ArrayList<Predicate> children = head.getAllChildren();
        for(int i = 0;i < children.size();i++){
            if(keys.contains(children.get(i).getName())){
                locations.add(i);
            }
        }
        return locations;
    }

    private static int[] createMaxCount(ArrayList<Integer> locations,Predicate head,LinkedHashMap<String,ArrayList<RewriteRule>> rules){
        int[] maxCount = new int[locations.size()];
        ArrayList<Predicate> children = head.getAllChildren();
        for (int i = 0; i < locations.size(); i++) {
            maxCount[i] = rules.get(children.get(locations.get(i)).getName()).size()-1;
        }
        return maxCount;
    }
    //how many permutations we need
    private static int getPermutationsCount(int[] maxCount){
        int permutationCount = 1;
        for(int i : maxCount){
            permutationCount *= i+1;
        }
        return permutationCount;
    }

    // maps all rules to their respective predicate, only keeping the ones that apply to this predicate
    private static LinkedHashMap<String,ArrayList<RewriteRule>> mapAllRules(ArrayList<RewriteRule> rules, Predicate source){

        LinkedHashMap<String,ArrayList<RewriteRule>> releventRules = new LinkedHashMap<>();
        ArrayList<String> releventPredicates = new ArrayList<>();
        for(Predicate pred: source.getAllChildren()){
            releventPredicates.add(pred.getName());
        }

        for(RewriteRule r: rules){
            if(releventPredicates.contains(r.getOriginalPredicate())) {
                if (!releventRules.containsKey(r.getOriginalPredicate())) {
                    releventRules.put(r.getOriginalPredicate(), new ArrayList<RewriteRule>());
                }
                releventRules.get(r.getOriginalPredicate()).add(r);
            }
        }
        return releventRules;
    }


    // we can iterate through the permutations in a bit of a similar way you'd construct a bit table
    // but instead of carrying the bit evertime we only do it when we hit maxcount, each value corresponding to one of the rewrite rules from the list
    // If we have arraylists of size 2,2,3
    // we will write them in the following way
    // 0,0,0 -> 0,0,1 -> 0,0,2 -> 0,0,3 -> 0,1,0 .. etc etc , this way we create all permutations
    private static void updatePermutation(int[] currCount,int n,int[] maxCount){
        if(currCount[n] < maxCount[n]){
            currCount[n]++;
        }else if(n != 0){
            currCount[n] = 0;
            updatePermutation(currCount,n-1,maxCount);
        }
    }
    private static void removeNumbers(Clause source){
        Iterator<AnalogicalObject> it = source.getPreOrderIterator();
        AnalogicalObject curr = null;
        while (it.hasNext()){
            curr = it.next();
            if(curr.getName().contains(".")){
                String[] split = curr.getName().split("\\.");
                curr.setName(split[0]);
            }
        }
    }

}


