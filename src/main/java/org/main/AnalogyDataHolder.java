package org.main;

import org.main.Interfaces.Predicate;
import org.main.Objects.Clause;
import org.main.Objects.Config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

// hold all the analogies, is incharge of parsing and what not
public class AnalogyDataHolder {

    private static final HashMap<String, ArrayList<String>> analogies = new HashMap<>();
    private static HashMap<Integer, ArrayList<String>> structuresHash = new HashMap<>();
    private static final Logger logger = Logger.getLogger(AnalogyDataHolder.class.getName());

    // setting up out threads
    public static void addAnalogiesFromFile(String filename, Config config) throws InterruptedException {
        try (BufferedReader br = new BufferedReader(new FileReader(config.getAnalogiesFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line, config);
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }


    // intaking a line, and trying to get something useful out of it
    public static void processLine(String line, Config config) {

        String[] arr = line.replace("\t", "  ").split(" {2}");
        String topic = arr[0];
        int jump = 3;
        int length;
        if (config.isRewrite()) {
            length = 2;
        } else {
            length = arr.length;
        }
        int i = 0;
        i = 1;
        for (i = 1; i < length; i = i + jump) {
            if (analogies.containsKey(topic)) {
                analogies.get(topic).add(arr[i]);
            } else {
                analogies.put(topic, new ArrayList<String>());
                analogies.get(topic).add(arr[i]);
            }
            if (structuresHash.containsKey(hashPredicate(arr[i]))) {
                structuresHash.get(hashPredicate(arr[i])).add(arr[i]);
            } else {
                structuresHash.put(hashPredicate(arr[i]), new ArrayList<String>());
                structuresHash.get(hashPredicate(arr[i])).add(arr[i]);
            }


        }

        if (config.isRewrite()) {
            ArrayList<String> rewrites = getRewrites(arr[1], config);
            analogies.get(topic).addAll(rewrites);

            for (String rewrite : rewrites) {
                if (structuresHash.containsKey(hashPredicate(rewrite))) {
                    structuresHash.get(hashPredicate(rewrite)).add(rewrite);
                } else {
                    structuresHash.put(hashPredicate(rewrite), new ArrayList<String>());
                    structuresHash.get(hashPredicate(rewrite)).add(rewrite);
                }
            }
        }
    }


    // incase we need all the rewrites
    private static ArrayList<String> getRewrites(String Source, Config config) {
        ArrayList<String> re = new ArrayList<>();
        System.out.println(Source + " changeing");
        ArrayList<Predicate> preds = ReWriter.reWriteAnalogyAllPermutations(config.getRuleSet().getRuleForAnalogy(Source), AnalogyManager.ConvertToOOP(Source));
        if (preds != null) {
            for (Predicate pred : preds) {
                re.add(pred.toString());
            }
        }

        return re;
    }

    private static int hashPredicate(String predicate) {
        Clause pred = (Clause) AnalogyManager.ConvertToOOP(predicate);
        String abstractPred = AnalogyManager.convertToAbstractString(pred, false);
        return abstractPred.hashCode();
    }
}

