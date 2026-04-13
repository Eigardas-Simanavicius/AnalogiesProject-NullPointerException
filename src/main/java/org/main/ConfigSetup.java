package org.main;

import org.main.Objects.Config;
import org.main.Objects.RuleSet;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

// exists to read config , and do stuff with it
public class ConfigSetup {
    private static final Logger logger = Logger.getLogger(ConfigSetup.class.getName());
    public static void applyConfig(String filepath){
        try{
            File configFile = new File(filepath);
            setupConfig(configFile);
        }catch (Exception e){
            findConfig();
        }
    }

    public static void findConfig(){
        try {
            File configFile = new File("config.txt");
            if(configFile.createNewFile()) {
                createDefaultConfig(configFile);
            }else { setupConfig(configFile);}
        }catch (Exception e){

        }

    }

    private static void createDefaultConfig(File configFile) throws IOException {

        configFile.createNewFile();
        logger.log(Level.WARNING, "No config found or provided, will create default file, empty by default,nothing will be loaded");
    }

    private static Config setupConfig(File configFile) {
        System.out.println("here");
        Config config = new Config();
        try (Scanner myReader = new Scanner(configFile)) {
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] currLine = line.replace(" ","").split("=");
                if(currLine[0].equals("rules")){
                    config.setRuleFilePath(new RuleSet(currLine[1]));
                }else if(currLine[0].equals("analogies")){
                    config.setAnalogiesFilePath(currLine[1]);
                }else if(currLine[0].equals("rewrite")){
                    config.setRewrite(Boolean.parseBoolean(currLine[1]));
                }else if(currLine[0].equals("abstracts")){
                    config.setRewrite(Boolean.parseBoolean(currLine[1]));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return config;
    }
}
