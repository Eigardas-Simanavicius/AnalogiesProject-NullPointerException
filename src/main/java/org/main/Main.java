package org.main;

import org.main.Interfaces.Predicate;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IllegalArgumentException {
        Clause c1 = new Clause("serve", "priest");
        Clause c2 = new Clause("some", "congregation");
        Clause c3 = new Clause();
        c3.setName("that");
        Clause c4 = new Clause();
        c4.setName("perform");
        Clause c5 = new Clause();
        c5.setName("for");
        Clause c6 = new Clause("some", "god");
        Clause c7 = new Clause("some", "worship");
        c1.addEmbedded(c2);
        c2.addEmbedded(c3);
        c3.addEmbedded(c4);
        c4.addEmbedded(c5);
        c4.addEmbedded(c7);
        c5.addEmbedded(c6);

        System.out.println("Flat Abstract String");
        System.out.println(AnalogyManager.convertToFlatAbstractString(AnalogyManager.ConvertToOOP("(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))")));

        System.out.println("\nPrettified Abstract String");
        System.out.println(AnalogyManager.convertToPrettifiedAbstractString(AnalogyManager.ConvertToOOP("(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))")));


        System.out.println("Converting to OOP and then back to String");
        System.out.println(AnalogyManager.ConvertToString(AnalogyManager.ConvertToOOP("(work in scientist (some lab (that (conduct experiment))))"),true));
    }
}
