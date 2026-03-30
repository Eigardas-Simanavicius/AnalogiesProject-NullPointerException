package org.main;

import org.main.Interfaces.Predicate;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IllegalArgumentException {

        System.out.println("Flat Abstract String");
        System.out.println(AnalogyManager.convertToFlatAbstractString(AnalogyManager.ConvertToOOP("(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))")));

        System.out.println("\nPrettified Abstract String");
        System.out.println(AnalogyManager.convertToPrettifiedAbstractString(AnalogyManager.ConvertToOOP("(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))")));


        System.out.println("Converting to OOP and then back to String");
        System.out.println(AnalogyManager.ConvertToString(AnalogyManager.ConvertToOOP("(work in scientist (some lab (that (conduct experiment))))"),true));
    }
}
