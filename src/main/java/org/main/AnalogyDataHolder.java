package org.main;

import org.main.Objects.RewriteRule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

// hold all the analogies, is incharge of parsing and what not
public class AnalogyDataHolder {

    ConcurrentHashMap<String,String> Analogies = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(RewriteRule.class.getName());

    public static void addAnalogiesFromFile(String filename,int threadsUsed) throws InterruptedException {
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
            Thread t = new sortAnalogies(filename,sets*i,sets*(i+1)+fixnumbers,latch);
            t.start();
        }
       try {
           latch.await();
       }catch (Exception e){
           logger.warning("Threads given was less then zero, will default to one thread");
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


    static class sortAnalogies extends Thread{
        String filename;
        int startLine;
        int endline;
        CountDownLatch latch;
        sortAnalogies(String filename,int startLine,int endline,CountDownLatch latch){

        }

        public void run(){

        }


    }



}
