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

    public static Config findConfig(){
        Config config;
        try {
            File configFile = new File("config.txt");
            if(configFile.createNewFile()) {
                //config = createDefaultConfig(configFile);
            }else { config = setupConfig(configFile);}
        }catch (Exception e){

        }
        //return config;
        return null;
    }

    private static Config createDefaultConfig(File configFile) throws IOException {

        logger.log(Level.WARNING, "No config found or provided, will create default file, empty by default,nothing will be loaded");
        //return configFile.createNewFile();
        return null;
    }

    public static Config setupConfig(File configFile) {
        System.out.println("here");
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
