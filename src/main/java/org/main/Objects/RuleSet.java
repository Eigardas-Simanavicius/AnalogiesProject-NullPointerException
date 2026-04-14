package org.main.Objects;

import org.main.Interfaces.AnalogicalObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.*;

public class RuleSet {

    // Variables are static to reduce memory usage across multiple instances
    private static TreeMap<String, ArrayList<String>> stringRules = new TreeMap<>();     // Represents rules as strings (Populated on first instantiation)
    private static TreeMap<String, ArrayList<RewriteRule>> parsedRules = new TreeMap<>();  // Represents rules using rewriteRule class (Populated as needed)
    private static ArrayList<String> loadedFiles = new ArrayList<>();


    public RuleSet(String fileDir) throws FileNotFoundException {
        if (!loadedFiles.contains(fileDir)) { // Creates and populates the stringRules with content from a file if not done so already
            loadRulesFromFile(fileDir);
            loadedFiles.add(fileDir);
        }
    }

    private void loadRulesFromFile(String fileDir) throws FileNotFoundException {
        File ruleFile = new File(fileDir);
        Scanner scanner = new Scanner(ruleFile);

        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            ArrayList<String> delimitedLine = new ArrayList<>(
                    Arrays.stream(
                                    line.split("[ \t]"))
                            .map(x -> x.replace(",", "").trim())
                            .toList());


            if (delimitedLine.size() > 1) {
                String verb = delimitedLine.removeFirst();
                delimitedLine.removeIf(x -> !x.matches("^.*_.*:.*&.*$"));

                if (!delimitedLine.isEmpty() && stringRules.containsKey(delimitedLine.getFirst())) {
                    stringRules.get(verb).addAll(delimitedLine);
                    parsedRules.remove(verb); // Removes parsed rules as they will be incorrect if more rules apply then there are currently
                } else if (!delimitedLine.isEmpty()) {
                    stringRules.put(
                            verb,
                            delimitedLine
                    );
                }
            }
        }
        System.out.println(stringRules);
    }

    // Returns string representation of rewriting rules for a given verb
    public ArrayList<String> getRulesAsStringFor(String ruleVerb) {
        return stringRules.get(ruleVerb);
    }

    // Returns rewriteRule representation for rewriting rules for a given verb
    public ArrayList<RewriteRule> getRulesFor(String ruleVerb) {
        ArrayList<RewriteRule> r;
        if ((r = parsedRules.get(ruleVerb)) != null) { // Only parses rule string as they are needed
            return r;
        } else {
            return parseRules(ruleVerb, stringRules.get(ruleVerb));
        }
    }


    public ArrayList<RewriteRule> getRuleForAnalogy(String analogy) {
        ArrayList<RewriteRule> rules = new ArrayList<>();
        String[] words = analogy.split(" ");
        for (String word : words) {
            if (word.charAt(0) == '(') {
                rules.addAll(getRulesFor(word.replace("(", "").replace(".0", "")));
            }
        }
        return rules;
    }

    private ArrayList<RewriteRule> parseRules(String ruleSubject, List<String> stringRewriteRules) {
        if (stringRewriteRules == null) return new ArrayList<>();

        ArrayList<RewriteRule> RewriteRules = new ArrayList<>(stringRewriteRules.size());

        for (String stringRewriteRule : stringRewriteRules) {
            try {
                RewriteRules.add(new RewriteRule(ruleSubject, stringRewriteRule));
            } catch (InvalidParameterException e) { // Prints error message if rule is written with invalid syntax
                e.printStackTrace();
                System.out.println(e.getMessage() + " : " + stringRewriteRule);
            } // Continues parsing valid rules rather than crashing
        }

        parsedRules.put(ruleSubject, RewriteRules);
        return RewriteRules;
    }
}


