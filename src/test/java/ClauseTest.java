import org.junit.Test;
import org.main.AnalogyManager;
import org.main.Clause;
import org.main.Interfaces.Predicate;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ClauseTest {

    @Test
    public void AddEmbeddedTest() {
        Clause c1 = new Clause("c1","c1");
        Clause c2 = new Clause("c2","c2");
        c1.addEmbedded(c2);
        assertEquals("c2",c1.getChildren().getFirst().getName());
    }

    @Test
    public void getallChildren(){
        ArrayList<String> names = new ArrayList<>();

        names.add("c1");
        names.add("c2");
        names.add("c3");
        names.add("c5");
        names.add("c4");

        Clause c1 = new Clause("c1","c1");
        Clause c2 = new Clause("c2","c2");
        Clause c3 = new Clause("c3","c3");
        Clause c4 = new Clause("c4","c4");
        Clause c5 = new Clause("c5","c5");

        c1.addEmbedded(c2);
        c2.addEmbedded(c3);
        c2.addEmbedded(c4);
        c3.addEmbedded(c5);
        ArrayList<Predicate> children = c1.getAllChildren();
        assertEquals(5,children.size());

        for (int i = 0; i < children.size(); i++) {
            assertEquals(children.get(i).getName(),names.get(i));
        }
    }

    @Test
    public void depth(){
        Clause c1 = new Clause("c1","c1");
        Clause c2 = new Clause("c2","c2");
        Clause c3 = new Clause("c3","c3");
        Clause c4 = new Clause("c4","c4");
        Clause c5 = new Clause("c5","c5");

        c1.addEmbedded(c2);
        c2.addEmbedded(c3);
        c2.addEmbedded(c4);
        c3.addEmbedded(c5);

        assertEquals(3, c5.depth());
    }

    @Test
    public void getParent(){
        Clause c1 = new Clause("c1","c1");
        Clause c2 = new Clause("c2","c2");
        Clause c3 = new Clause("c3","c3");
        Clause c4 = new Clause("c4","c4");
        Clause c5 = new Clause("c5","c5");

        c1.addEmbedded(c2);
        c2.addEmbedded(c3);
        c2.addEmbedded(c4);
        c3.addEmbedded(c5);

        assertEquals(c3, c5.getParent());
    }

    @Test
    public void selfEmbeddingTest(){
        Clause c1 = new Clause("c1", "c1");
        c1.addEmbedded(c1);

        assert(c1.getChildren().isEmpty());
    }
}