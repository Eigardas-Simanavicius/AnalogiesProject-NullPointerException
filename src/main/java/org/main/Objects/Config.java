package org.main.Objects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private static boolean rewrite = false;
    private static String analogiesFilePath;
    private static RuleSet ruleSet;
    private static List<String> targets = null;
    private static int jump = 3;
    public Config(){

    }

    public  int getJumps() {
        return jump;
    }
    public  void setJumps(int jumps) {
        Config.jump = jumps;
    }

    public  List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        Config.targets = targets;
    }

    public  RuleSet getRuleSet() {
        return ruleSet;
    }

    public  void setRuleSet(RuleSet ruleSet) {
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

    public  boolean isRewrite() {
        return rewrite;
    }

    public void setRewrite(boolean rewrite) {
        Config.rewrite = rewrite;
    }
}
