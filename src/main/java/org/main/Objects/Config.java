package org.main.Objects;

import java.io.File;
import java.util.ArrayList;

public class Config {

    private static boolean rewrite = false;
    private static boolean abstracts = true;
    private static String analogiesFilePath;
    private static RuleSet ruleSet;
    private static ArrayList<String> targets = new ArrayList<>();
    public Config(){

    }

    public  ArrayList<String> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<String> targets) {
        Config.targets = targets;
    }

    public  RuleSet getRuleFilePath() {
        return ruleSet;
    }

    public  void setRuleFilePath(RuleSet ruleSet) {
        Config.ruleSet = ruleSet;
    }

    public  String getAnalogiesFilePath() {
        return analogiesFilePath;
    }

    public  void setAnalogiesFilePath() {
        setAnalogiesFilePath(null);
    }

    public  void setAnalogiesFilePath(String analogiesFilePath) {
        Config.analogiesFilePath = analogiesFilePath;
    }

    public  boolean isAbstracts() {
        return abstracts;
    }

    public void setAbstracts(boolean abstracts) {
        Config.abstracts = abstracts;
    }

    public  boolean isRewrite() {
        return rewrite;
    }

    public void setRewrite(boolean rewrite) {
        Config.rewrite = rewrite;
    }
}
