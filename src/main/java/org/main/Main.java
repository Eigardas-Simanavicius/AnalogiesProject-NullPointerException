package org.main;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Clause c1 = new Clause("serve", "priest");
        Clause c2 = new Clause("some", "congregation");
        Clause c3 = new Clause();
        c3.setName("that");
        Clause c4 = new Clause("perform", "worship");
        c1.setEmbedded(c2);
        c2.setEmbedded(c3);
        c3.setEmbedded(c4);

        System.out.println(AnalogyManager.ConvertToString(c1));
    }
}
