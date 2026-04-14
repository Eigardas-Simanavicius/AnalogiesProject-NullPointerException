package org.main;

import org.main.Interfaces.Predicate;
import org.main.Objects.Clause;
import org.main.Objects.Config;
import org.main.Objects.RewriteRule;

import java.io.IOException;
import java.util.ArrayList;
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
            //config = ConfigSetup.findConfig();
        }else{
            //config = ConfigSetup.applyConfig(args[0]);
        }
        //config = ConfigSetup.applyConfig("/home/eigardas/Documents/Github/AnalogiesProject/config.txt");
        //AnalogyDataHolder.addAnalogiesFromFile("/home/eigardas/Documents/Github/AnalogiesProject/test.txt",config);
        RewriteRule rule1 = new RewriteRule("cause","produce_for:victim&causing");
        ArrayList<RewriteRule> rules = new ArrayList<>();
        rules.add(rule1);
        Predicate pred = (Clause)AnalogyManager.ConvertToOOP("(if (can (cause *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))");
        ArrayList<Predicate> ans = ReWriter.reWriteAnalogyAllPermuatations(rules,pred);
        System.out.println(ans.toString());

    }
}
