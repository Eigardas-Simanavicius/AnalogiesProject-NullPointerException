import org.junit.Test;
import org.main.Objects.Clause;
import org.main.Objects.Subject;

import static org.junit.Assert.assertEquals;

public class ConvertToStringTest {

    @Test
    public void SimpleConversion(){
        Clause c1 = new Clause("c1");
        Subject s1 = new Subject("s1");
        Clause c2 = new Clause("c2");
        Subject s2 = new Subject("s2");

        c1.addEmbedded(s1);
        c1.addEmbedded(c2);
        c2.addEmbedded(s2);

        assertEquals("(c1 s1(c2 s2))", c1.toString());
    }


    @Test
    public void NullSubject(){
        Clause c1 = new Clause("c1");
        Clause c2 = new Clause("c2");
        Subject s1 = new Subject("s1");
        c1.addEmbedded(s1);
        c1.addEmbedded(c2);

        assertEquals("(c1 s1(c2))", c1.toString());
    }


    @Test
    public void Indentation(){
        Clause c1 = new Clause("c1");
        Subject s1 = new Subject("s1");
        Clause c2 = new Clause("c2");
        Subject s2 = new Subject("s2");

        c1.addEmbedded(s1);
        c1.addEmbedded(c2);
        c2.addEmbedded(s2);

        assertEquals("(c1 s1\n\t(c2 s2))", c1.toIndentedString());
    }

    @Test
    public void MultipleChildren(){
        Clause c1 = new Clause("c1");
        Subject s1 = new Subject("s1");
        Clause c2 = new Clause("c2");
        Subject s2 = new Subject("s2");
        Clause c3 = new Clause("c3");
        Subject s3 = new Subject("s3");
        Clause c4 = new Clause("c4");
        Subject s4 = new Subject("s4");
        Clause c5 = new Clause("c5");
        Subject s5 = new Subject("s5");

        c1.addEmbedded(s1);
        c1.addEmbedded(c2);
        c2.addEmbedded(s2);
        c2.addEmbedded(c3);
        c2.addEmbedded(c4);
        c4.addEmbedded(s4);
        c3.addEmbedded(s3);
        c3.addEmbedded(c5);
        c5.addEmbedded(s5);

        assertEquals("(c1 s1(c2 s2(c3 s3(c5 s5))(c4 s4)))", c1.toString());
    }


    @Test
    public void MultipleChildrenIndented(){
        Clause c1 = new Clause("c1");
        Subject s1 = new Subject("s1");
        Clause c2 = new Clause("c2");
        Subject s2 = new Subject("s2");
        Clause c3 = new Clause("c3");
        Subject s3 = new Subject("s3");
        Clause c4 = new Clause("c4");
        Subject s4 = new Subject("s4");
        Clause c5 = new Clause("c5");
        Subject s5 = new Subject("s5");

        c1.addEmbedded(s1);
        c1.addEmbedded(c2);
        c2.addEmbedded(s2);
        c2.addEmbedded(c3);
        c2.addEmbedded(c4);
        c4.addEmbedded(s4);
        c3.addEmbedded(s3);
        c3.addEmbedded(c5);
        c5.addEmbedded(s5);

        assertEquals("(c1 s1\n\t(c2 s2\n\t\t(c3 s3\n\t\t\t(c5 s5))\n\t\t\t(c4 s4)))", c1.toIndentedString());
    }


    @Test
    public void NoSubjectIndentation(){
        Clause c1 = new Clause("c1");
        Subject s1 = new Subject("s1");
        Clause c2 = new Clause("c2");
        Clause c3 = new Clause("c3");
        Clause c4 = new Clause("c4");
        Subject s4 = new Subject("s4");
        Clause c5 = new Clause("c5");
        Subject s5 = new Subject("s5");

        c1.addEmbedded(s1);
        c1.addEmbedded(c2);
        c2.addEmbedded(c3);
        c2.addEmbedded(c4);
        c4.addEmbedded(s4);
        c3.addEmbedded(c5);
        c5.addEmbedded(s5);

        assertEquals("(c1 s1\n\t(c2(c3(c5 s5))\n\t\t\t(c4 s4)))", c1.toIndentedString());
    }

}
