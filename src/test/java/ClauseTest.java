import org.junit.Test;
import org.main.Objects.Clause;
import org.main.Interfaces.Predicate;
import org.main.Objects.Subject;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ClauseTest {

    @Test
    public void AddEmbeddedTest() {
        Clause c1 = new Clause("c1");
        Subject s1 = new Subject("s1");
        c1.addEmbedded(s1);
        assertEquals("s1",c1.getChildren().getFirst().getName());
    }

    @Test
    public void getallChildren(){
        ArrayList<String> names = new ArrayList<>();

        names.add("c1");
        names.add("c2");
        names.add("c3");
        names.add("c5");
        names.add("c4");

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
        ArrayList<Predicate> children = c1.getAllChildren();
        assertEquals(5,children.size());

        for (int i = 0; i < children.size(); i++) {
            assertEquals(children.get(i).getName(),names.get(i));
        }
    }



    @Test
    public void depth(){
        Clause c1 = new Clause("c1");
        Clause c2 = new Clause("c2");
        Clause c3 = new Clause("c3");
        Clause c4 = new Clause("c4");
        Clause c5 = new Clause("c5");

        c1.addEmbedded(c2);
        c2.addEmbedded(c3);
        c2.addEmbedded(c4);
        c3.addEmbedded(c5);

        assertEquals(3, c5.getDepth());
    }


    @Test
    public void getParent(){
        Clause c1 = new Clause("c1");
        Clause c2 = new Clause("c2");
        Clause c3 = new Clause("c3");
        Clause c4 = new Clause("c4");
        Clause c5 = new Clause("c5");

        c1.addEmbedded(c2);
        c2.addEmbedded(c3);
        c2.addEmbedded(c4);
        c3.addEmbedded(c5);

        assertEquals(c3, c5.getParent());
    }

    @Test
    public void selfEmbeddingTest(){
        Clause c1 = new Clause("c1");
        c1.addEmbedded(c1);

        assert(c1.getChildren().isEmpty());
    }
}