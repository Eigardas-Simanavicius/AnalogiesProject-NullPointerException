package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;
import org.main.Objects.Clause;
import org.main.Objects.Config;
import org.main.Objects.RewriteRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.*;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        //logger initialization
        Logger rootLogger = Logger.getLogger("");
        try{
            //getting rid of logging errors to console in order to isolate them to the log file
            for(Handler handler : rootLogger.getHandlers()){
                rootLogger.removeHandler(handler);
            }

            FileHandler fileHandler = new FileHandler("logs/errorLog.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);
            rootLogger.setLevel(Level.WARNING);
        }catch(IOException e){
            System.out.println("failed config " + e.getMessage());
        }

        //Config Init
        // no given config
        Config config;
        if(args.length == 0){
            config = ConfigSetup.findConfig();
        }else{
            config = ConfigSetup.applyConfig(args[0]);
        }
        System.out.println(config.getAnalogiesFilePath());

    }
}
