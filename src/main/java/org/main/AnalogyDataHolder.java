package org.main;

import org.main.Interfaces.Predicate;
import org.main.Objects.Clause;
import org.main.Objects.Config;
import org.main.Objects.RewriteRule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

// hold all the analogies, is incharge of parsing and what not
public class AnalogyDataHolder {

    private static final ConcurrentHashMap<String, ArrayList<String>> analogies = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer,ArrayList<String>> structureHash = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(AnalogyDataHolder.class.getName());
    // setting up out threads
    public static void addAnalogiesFromFile(String filename, Config config) throws InterruptedException {
        if(config.getThreadsUsed() < 1){
            config.setThreadsUsed(1);
            logger.warning("Threads given was less then zero, will default to one thread");
        }
        CountDownLatch latch = new CountDownLatch(config.getThreadsUsed());
       int linesNumber = getLineNumber(filename);
       int sets = linesNumber/config.getThreadsUsed();
       int fixnumbers = 0;
       for(int i = 0;  i < config.getThreadsUsed(); i++){
            if (i == config.getThreadsUsed()-i){
                // this is to make up for possible loss of lines when using integer division
                fixnumbers = linesNumber - ((sets/config.getThreadsUsed()) * config.getThreadsUsed());
            }
            Thread t = new sortAnalogies(filename,sets*i,sets*(i+1)+fixnumbers,latch,config);
            t.start();
        }
       try {
           latch.await();
       }catch (Exception e){
           logger.warning("latch exploded");
       }

    }

    private static int getLineNumber(String filename){
        int counter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while (br.readLine() != null) {
                counter++;
            }
        } catch (IOException e) {
            logger.warning("File not found, no rules will be written to");
        }
        return counter;
    }

    public static ConcurrentHashMap<String, ArrayList<String>> getAnalogies(){
        return analogies;
    }
    public static ConcurrentHashMap<Integer, ArrayList<String>> getStructreHashes(){
        return structureHash;
    }

    //the main function, each thread will read their part of the file and work with the analogies
    static class sortAnalogies extends Thread{
        String filename;
        int startLine;
        int endline;
        CountDownLatch latch;
        Config config;

        sortAnalogies(String filename,int startLine,int endline,CountDownLatch latch,Config config){
            this.filename = filename;
            this.endline = endline;
            this.startLine = startLine;
            this.latch = latch;
            this.config = config;
        }

        public void run(){
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line ;
                for (int i = 0; i < endline; i++){
                    if(i >= startLine){
                        line = br.readLine();
                        processLine(line,config);
                    }
                }
            } catch (IOException e) {
                logger.warning("File not found, no rules will be written to");
            }
            latch.countDown();

        }

        // intaking a line, and trying to get something useful out of it
        public static void processLine(String line,Config config){

            String[] arr = line.replace("\t","  ").split(" {2}");
            String topic = arr[0];
            int jump = 4;
            int lenght;
            if(config.isRewrite()){
                lenght =  2;
            }else{
                lenght = arr.length;
            }

            for (int i = 1; i < lenght; i = i + jump) {
                if (analogies.containsKey(topic)) {
                    analogies.get(topic).add(arr[i]);
                } else {
                    analogies.put(topic, new ArrayList<String>());
                    analogies.get(topic).add(arr[i]);
                }
                if (structureHash.containsKey(hashPredicate(arr[i]))) {
                    structureHash.get(hashPredicate(arr[i])).add(arr[i]);
                } else {
                    structureHash.put(hashPredicate(arr[i]), new ArrayList<String>());
                    structureHash.get(hashPredicate(arr[i])).add(arr[i]);
                }

            }

            if(config.isRewrite()) {
                ArrayList<String> rewrites = getRewrites(arr[1], config);
                analogies.get(topic).addAll(rewrites);

                for (String rewrite: rewrites){
                    if (structureHash.containsKey(hashPredicate(rewrite))) {
                        structureHash.get(hashPredicate(rewrite)).add(rewrite);
                    } else {
                        structureHash.put(hashPredicate(rewrite), new ArrayList<String>());
                        structureHash.get(hashPredicate(rewrite)).add(rewrite);
                    }
                }
            }

        }


        // incase we need all the rewrites
        private static ArrayList<String> getRewrites(String Source, Config config){
            ArrayList<String> re = new ArrayList<>();
            ArrayList<Predicate> preds = ReWriter.reWriteAnalogyAllPermutations(config.getRuleSet().getRuleForAnalogy(Source),AnalogyManager.ConvertToOOP(Source));
            if(preds != null) {
                for (Predicate pred : preds) {
                    re.add(pred.toString());
                }
            }

            return re;
        }

        private static int hashPredicate(String predicate){
            Clause pred = (Clause)AnalogyManager.ConvertToOOP(predicate);
            String abstractPred = AnalogyManager.convertToAbstractString(pred, false);
            return abstractPred.hashCode();
        }

    }



}
