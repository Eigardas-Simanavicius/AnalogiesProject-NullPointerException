package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;
import java.util.*;

public class ReWriter {
    public static Predicate reWriteAnalogy(HashMap<String,rewriteRule> rulesMap, Predicate source)  {
        // I am going to make the assumption that each clause can atmost have one rewrite.
        Predicate reWrite = AnalogyManager.ConvertToOOP(source.toString());
        Iterator<AnalogicalObject> it = ((Clause)reWrite).getPreOrderIterator();
        AnalogicalObject curr = null;
        Predicate replacement = null;
        Predicate head = null;

        while (it.hasNext()){
            curr = it.next();
            if(curr.getClass().equals(Clause.class)){
                rulesMap.get(curr.getName());
                if(rulesMap.containsKey(curr.getName())){
                    Predicate parent = curr.getParent();
                    curr.setParent(null);
                    parent.getChildren().remove(curr);
                    replacement = rulesMap.get(curr.getName()).rewrite((Predicate) curr);
                    replacement.setParent(parent);
                    parent.addEmbedded(replacement);
                }

                if(head == null) {
                    head = (Predicate) curr;
                }
            }
        }
        return head;
    }

    public static ArrayList<Predicate> reWriteAnalogyAllPermuatations(ArrayList<rewriteRule> rules,Predicate source)  {
        // I am going to make the assumption that each clause can atmost have one rewrite.
        removeNumbers((Clause) source);
        LinkedHashMap<String,ArrayList<rewriteRule>> rulesMap = mapRulesMany(rules);
        ArrayList<Predicate> permutations = new ArrayList<>();
        int[] maxCount = new int[rulesMap.size()];
        int[] currCount = new int[rulesMap.size()];
        int permutationCount = 1;
        int n = 0;

        for (ArrayList<rewriteRule> arrs: rulesMap.values()) {
            permutationCount = permutationCount * arrs.size();
            maxCount[n] = arrs.size();
            n++;
        }

        for (int i = 0; i < permutationCount; i++) {
            permutations.add(reWriteAnalogy(mapRulesSingle(rulesMap,currCount), AnalogyManager.ConvertToOOP(source.toString())));
            updatePermutation(currCount,currCount.length-1,maxCount);
        }

        return permutations;
    }

    private static LinkedHashMap<String,ArrayList<rewriteRule>> mapRulesMany(ArrayList<rewriteRule> rules){
        LinkedHashMap<String,ArrayList<rewriteRule>> map = new LinkedHashMap<>();
        for(rewriteRule r: rules){
            if(!map.containsKey(r.getOriginalPredicate())) {
                map.put(r.getOriginalPredicate(), new ArrayList<rewriteRule>());
            }
            map.get(r.getOriginalPredicate()).add(r);
        }
        return map;
    }

    private static HashMap<String,rewriteRule> mapRulesSingle(LinkedHashMap<String,ArrayList<rewriteRule>> rules, int[] count){
        HashMap<String,rewriteRule> map = new HashMap<>();
        Iterator<ArrayList<rewriteRule>> it = rules.values().iterator();
        Iterator<String> sIt = rules.keySet().iterator();
        for (int i = 0; i < rules.size(); i++) {
            map.put(sIt.next(),it.next().get(count[i]));
        }

        return map;
    }


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
            if(curr.getName().contains(".0")){
                curr.setName(curr.getName().replace(".0",""));
            }
        }
    }

}
