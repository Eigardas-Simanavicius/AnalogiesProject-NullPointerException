package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;
import java.util.*;

public class ReWriter {

    // This takes an analogy and re-writes according to the provided rule set
    private static Predicate reWriteAnalogy(HashMap<String,rewriteRule> rulesMap, Predicate source)  {
        Predicate reWrite = AnalogyManager.ConvertToOOP(source.toString());
        Iterator<AnalogicalObject> it = ((Clause)reWrite).getPreOrderIterator();
        AnalogicalObject curr = null;
        Predicate head = null;
        // essentially just check if we need to rewrite this predicate, and if we do we remove the old one and attach the rewritten one
        while (it.hasNext()){
            curr = it.next();
            if(curr.getClass().equals(Clause.class)){
                rulesMap.get(curr.getName());
                if(rulesMap.containsKey(curr.getName())){
                    replacePredicate((Clause) curr,rulesMap);
                }

                if(head == null) {
                    head = (Predicate) curr;
                }
            }
        }
        return head;
    }

    private static void replacePredicate(Clause curr,HashMap<String,rewriteRule> rulesMap){
        ArrayList<AnalogicalObject> children = null;
        Predicate replacement = null;

        Predicate parent = curr.getParent();
        curr.setParent(null);
        children = ((Clause) curr).getClauseChildren();
        ((Clause) curr).removeClauses();
        parent.getChildren().remove(curr);

        replacement = rulesMap.get(curr.getName()).rewrite((Predicate) curr);
        replacement.setParent(parent);
        replacement.addAllEmbedded(children);
        parent.addEmbedded(replacement);

    }
    // this is the main controller function,
    public static ArrayList<Predicate> reWriteAnalogyAllPermuatations(ArrayList<rewriteRule> rules,Predicate source)  {
        removeNumbers((Clause) source);
        // Linked hash maps ensure items are returned in order of insertion, very important here
        LinkedHashMap<String,ArrayList<rewriteRule>> rulesMap = mapAllRules(rules);
        ArrayList<Predicate> permutations = new ArrayList<>();
        int[] maxCount = new int[rulesMap.size()];
        int[] currCount = new int[rulesMap.size()];
        int permutationCount = 1;
        int n = 0;

        // maxcount keeps track of how many rules are bound to each predicate, so we can correctly create permutations
        for (ArrayList<rewriteRule> arrs: rulesMap.values()) {
            permutationCount = permutationCount * arrs.size();
            maxCount[n] = arrs.size();
            n++;
        }

        for (int i = 0; i < permutationCount; i++) {
            permutations.add(reWriteAnalogy(mapSingleRulePermutation(rulesMap,currCount), AnalogyManager.ConvertToOOP(source.toString())));
            updatePermutation(currCount,currCount.length-1,maxCount);
        }

        return permutations;
    }

    // maps all rules to their respective predicate
    private static LinkedHashMap<String,ArrayList<rewriteRule>> mapAllRules(ArrayList<rewriteRule> rules){
        LinkedHashMap<String,ArrayList<rewriteRule>> map = new LinkedHashMap<>();
        for(rewriteRule r: rules){
            if(!map.containsKey(r.getOriginalPredicate())) {
                map.put(r.getOriginalPredicate(), new ArrayList<rewriteRule>());
            }
            map.get(r.getOriginalPredicate()).add(r);
        }
        return map;
    }

    // this creates a map of rules based on the permutation from count
    private static HashMap<String,rewriteRule> mapSingleRulePermutation(LinkedHashMap<String,ArrayList<rewriteRule>> rules, int[] count){
        HashMap<String,rewriteRule> map = new HashMap<>();
        Iterator<ArrayList<rewriteRule>> it = rules.values().iterator();
        Iterator<String> sIt = rules.keySet().iterator();
        for (int i = 0; i < rules.size(); i++) {
            map.put(sIt.next(),it.next().get(count[i]));
        }

        return map;
    }

    // we can iterate through the permutations in a bit of a similar way you'd construct a bit table
    // but instead of carrying the bit evertime we only do it when we hit maxcount, each value corresponding to one of the rewrite rules from the list
    // If we have arraylists of size 2,2,3
    // we will write them in the following way
    // 0,0,0 -> 0,0,1 -> 0,0,2 -> 0,0,3 -> 0,1,0 .. etc etc , this way we create all permutations
    private static void updatePermutation(int[] currCount,int n,int[] maxCount){
        if(currCount[n] < maxCount[n]){
            currCount[n]++;
        }else{
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


