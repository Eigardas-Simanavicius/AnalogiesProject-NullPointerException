package org.main;

import org.main.Objects.RewriteRule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

// hold all the analogies, is incharge of parsing and what not
public class AnalogyDataHolder {

    private static final ConcurrentHashMap<String, ArrayList<String>> Analogies = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(RewriteRule.class.getName());

    public static void addAnalogiesFromFile(String filename,int threadsUsed,boolean reWriteAll) throws InterruptedException {
        if(threadsUsed < 1){
            threadsUsed = 1;
            logger.warning("Threads given was less then zero, will default to one thread");
        }
        CountDownLatch latch = new CountDownLatch(threadsUsed);
       int linesNumber = getLineNumber(filename);
       int sets = linesNumber/threadsUsed;
       int fixnumbers = 0;
       for(int i = 0;  i < threadsUsed; i++){
            if (i == threadsUsed-i){
                // this is to make up for possible loss of lines when using integer division
                fixnumbers = linesNumber - ((sets/threadsUsed) * threadsUsed);
            }
            Thread t = new sortAnalogies(filename,sets*i,sets*(i+1)+fixnumbers,latch,reWriteAll);
            t.start();
        }
       try {
           latch.await();
       }catch (Exception e){
           logger.warning("Threads given was less then zero, will default to one thread");
       }
        System.out.println("here");

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

    private static void addAnalogy(){

    }

    static class sortAnalogies extends Thread{
        String filename;
        int startLine;
        int endline;
        CountDownLatch latch;
        boolean rewrite;
        sortAnalogies(String filename,int startLine,int endline,CountDownLatch latch,boolean rewrite){
            this.filename = filename;
            this.endline = endline;
            this.startLine = startLine;
            this.latch = latch;
            this.rewrite = rewrite;
        }

        public void run(){
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line ;
                for (int i = 0; i < endline; i++){
                    System.out.println(startLine  + " " + endline);
                    if(i >= startLine){
                        line = br.readLine();
                        processLine(line);
                    }
                }
            } catch (IOException e) {
                logger.warning("File not found, no rules will be written to");
            }
            latch.countDown();
        }

        public static void processLine(String line){
            String[] arr = line.replace("\t","  ").split(" {2}");
            String topic = arr[0];
            int lenght = 0;
            int jump;
            if(false){
                jump = 2;
            }else {
                jump = 3;
            }
            if(true){
                lenght = (arr.length-1)/jump;
            }else {
                lenght = 1;
            }

            for(int i = 1; i < arr.length-1; i = i + jump){
                System.out.println(i + " " + lenght);
                if(Analogies.containsKey(topic)){
                    Analogies.get(topic).add(arr[i]);
                }else {
                    Analogies.put(topic, new ArrayList<String>());
                    Analogies.get(topic).add(arr[i]);
                }
            }
            System.out.println(Analogies.toString());

        }

    }



}
