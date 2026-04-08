package org.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.*;

public class RuleSet {

    // Variables are static to reduce memory usage across multiple instances
    private static TreeMap<String, ArrayList<String>> stringRules = new TreeMap<>();     // Represents rules as strings (Populated on first instantiation)
    private static TreeMap<String,ArrayList<rewriteRule>> parsedRules = new TreeMap<>();  // Represents rules using rewriteRule class (Populated as needed)
    private static ArrayList<String> loadedFiles = new ArrayList<>();


    public RuleSet(String fileDir) throws FileNotFoundException {
        if(!loadedFiles.contains(fileDir)){ // Creates and populates the stringRules with content from a file if not done so already
            loadRulesFromFile(fileDir);
            loadedFiles.add(fileDir);
        }
    }

    private void loadRulesFromFile(String fileDir) throws FileNotFoundException {
        File ruleFile = new File(fileDir); // Replace with the location of file on your system (Is currently a placeholder)
        Scanner scanner = new Scanner(ruleFile);

        String line;
        while(scanner.hasNextLine()){
            line = scanner.nextLine();

            ArrayList<String> delimitedLine = new ArrayList<>(List.of(line.split(" ")));
            String verb = delimitedLine.removeFirst();

            if(stringRules.containsKey(delimitedLine.getFirst())){
                stringRules.get(verb).addAll(delimitedLine);
                parsedRules.remove(verb); // Removes parsed rules as they will be incorrect if more rules apply then there are currently
            }else {
                stringRules.put(
                        verb,
                        (ArrayList<String>) delimitedLine.stream().map(x -> x.replace(",", "").trim()).toList()
                );
            }
        }
    }

    // Returns string representation of rewriting rules for a given verb
    public ArrayList<String> getRulesAsStringFor(String ruleVerb){
        return stringRules.get(ruleVerb);
    }

    // Returns rewriteRule representation for rewriting rules for a given verb
    public ArrayList<rewriteRule> getRulesFor(String ruleVerb){
        ArrayList<rewriteRule> r;
        if((r = parsedRules.get(ruleVerb)) != null ){ // Only parses rule string as they are needed
            return r;
        }else{
            return parseRules(ruleVerb, stringRules.get(ruleVerb));
        }
    }

    private ArrayList<rewriteRule> parseRules(String ruleSubject,List<String> stringRewriteRules){
        if(stringRewriteRules == null) return new ArrayList<>();

        ArrayList<rewriteRule> rewriteRules = new ArrayList<>(stringRewriteRules.size());

        for(String stringRewriteRule : stringRewriteRules){
            try {
                rewriteRules.add(new rewriteRule(ruleSubject, stringRewriteRule));
            }catch(InvalidParameterException e){ // Prints error message if rule is written with invalid syntax
                e.printStackTrace();
                System.out.println(e.getMessage() + " : " + stringRewriteRule);
            } // Continues parsing valid rules rather than crashing
        }

        parsedRules.put(ruleSubject,rewriteRules);
        return rewriteRules;
    }
}
