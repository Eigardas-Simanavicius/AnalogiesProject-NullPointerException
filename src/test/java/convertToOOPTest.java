import org.junit.Test;
import org.main.AnalogyManager;
import org.main.Clause;
import org.main.Interfaces.AnalogicalObject;
import org.main.Subject;

import java.util.*;

import static org.junit.Assert.*;

public class convertToOOPTest {


    ArrayList<String> names = new ArrayList<>();
    @Test
    public void BasicTest() throws IllegalAccessException {
        String input = "(serve priest (some congregation (that (perform (for (some god)) (some worship)))))";
        Clause Head = (Clause) AnalogyManager.ConvertToOOP(input);

        assertEquals("serve", Head.getName());
        assertEquals("priest",Head.getChildren().getFirst().getName());
    }

    @Test
    public void AssertChildren()  {
        String input = "(serve priest (some congregation (that (perform (for (some god)) (some worship)))))";
        Clause Head = (Clause) AnalogyManager.ConvertToOOP(input);
        names.add("serve");
        names.add("priest");
        names.add("some");
        names.add("congregation");
        names.add("that");
        names.add("perform");
        names.add("for");
        names.add("some");
        names.add("god");
        names.add("some");
        names.add("worship");

        Iterator<AnalogicalObject> it = Head.getPreOrderIterator();

        for (int i = 0; it.hasNext(); i++) {
            assertEquals(it.next().getName(),names.get(i));
        }
    }

    @Test
    public void ParentTest() throws IllegalAccessException {
        String input = "(serve priest (some congregation (that (perform (for (some god)) (some worship)))))";
        Clause Head = (Clause) AnalogyManager.ConvertToOOP(input);
        assertEquals(Head, Head.getChildren().getFirst().getParent());
    }

    @Test
    public void unbalancedBrackets(){
        String input = "(((((((((serve priest (some congregation (that (perform (for (some god)) (some worship))))";
        String input2 = "(((((((((serve priest (some congregation (that (perform (for (some god)) (some worship))))))))))))";
        assertThrows(IllegalArgumentException.class, () -> {AnalogyManager.ConvertToOOP(input);});
        assertThrows(IllegalArgumentException.class, () -> {AnalogyManager.ConvertToOOP(input2);});
    }

    @Test
    public void emptyInput(){
        String input = "";
        assertNull(AnalogyManager.ConvertToOOP(input));
    }

    @Test
    public void hasAsteriskSimpleTest(){
        Clause c1 = (Clause) AnalogyManager.ConvertToOOP("(c1 *s1)");

        assertTrue(((Subject) c1.getChildren().getFirst()).isHasAsterisk());
    }

    @Test
    public void hasAsteriskNestedTest1(){
        Clause c1 = (Clause) AnalogyManager.ConvertToOOP("(c1 *s1(c2 s2))");

        assertTrue(((Subject) c1.getChildren().getFirst()).isHasAsterisk());
    }

    @Test
    public void hasAsteriskNestedTest2(){
        Clause c1 = (Clause) AnalogyManager.ConvertToOOP("(c1 s1(c2 *s2))");

        Clause c2 = (Clause)c1.getChildren().get(1);
        assertTrue(((Subject) c2.getChildren().getFirst()).isHasAsterisk());
    }
}
