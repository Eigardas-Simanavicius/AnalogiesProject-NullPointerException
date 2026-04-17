package org.main;

import org.main.Objects.Config;
import org.main.Objects.RuleSet;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

// exists to read config , and do stuff with it
public class ConfigSetup {
    private static final Logger logger = Logger.getLogger(ConfigSetup.class.getName());
    public static Config applyConfig(String filepath){
        Config config;
        try{
            File configFile = new File(filepath);
            config = setupConfig(configFile);
        }catch (Exception e){
           config = findConfig();
        }
        return config;
    }
    // if no config was given we are going to try out best to find it
    public static Config findConfig(){
        Config config = null;
        try {
            File configFile = new File("config.txt");
            if(configFile.createNewFile()) {
                config = createDefaultConfig(configFile);
            }else { 
                config = setupConfig(configFile);}
        }catch (Exception e){
            logger.log(Level.WARNING, "Config setup failure, no config was found, created or ");
        }
        return config;
 
    }

    private static Config createDefaultConfig(File configFile) throws IOException {
        Config newConfig = new Config();
        try {
            FileWriter myWriter = new FileWriter("config.txt");
            myWriter.write("rewrite= \nrules= \ntargets= \nanalogies= \n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newConfig;
    }

    public static Config setupConfig(File configFile) {
        Config config = new Config();
        try (Scanner myReader = new Scanner(configFile)) {
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] currLine = line.replace(" ","").split("=");
                if(currLine[0].equals("rules")){
                    config.setRuleSet(new RuleSet(currLine[1]));
                }else if(currLine[0].equals("analogies")){
                    config.setAnalogiesFilePath(currLine[1]);
                }else if(currLine[0].equals("rewrite")){
                    config.setRewrite(Boolean.parseBoolean(currLine[1]));
                }else if(currLine[0].equals("targets")){
                    config.setTargets( Arrays.asList(currLine[1].split(",")));
                } else if (currLine[0].equals("jumps")) {
                    config.setJumps(Integer.parseInt(currLine[1]));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return config;
    }
}
