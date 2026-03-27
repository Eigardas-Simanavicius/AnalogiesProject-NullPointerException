package org.main;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
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

        System.out.println(AnalogyManager.ConvertToString(c1, true));

        /*
        System.out.println(AnalogyManager.convertToIndentedAbstractString(c1));
        // System.out.println(AnalogyManager.ConvertToString(c1));

        String input = "(work in scientist (some lab (that (conduct experiment))))";
        Clause c5 = (Clause) AnalogyManager.ConvertToOOP(input);
        //System.out.println(c5.getName() + " which has " + c5.getSubject() + " and  " +c5.getEmbedded().getName());
        //System.out.println(AnalogyManager.ConvertToString(c5));
        System.out.println(c5.get(2).length() + " and " + c5.length());
        */


    }
}
