import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.main.AnalogyManager;
import org.main.Clause;
import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.InputMismatchException;

import static org.junit.Assert.*;

public class convertToOOPTest {


    ArrayList<String> names = new ArrayList<>();
    @Test
    public void BasicTest() throws IllegalAccessException {
        String input = "(serve priest (some congregation (that (perform (for (some god)) (some worship)))))";
        Clause Head = (Clause) AnalogyManager.ConvertToOOP(input);

        assertEquals("serve", Head.getName());
        assertEquals("some",Head.getChildren().getFirst().getName());
    }

    @Test
    public void AssertChildren() throws IllegalAccessException {
        String input = "(serve priest (some congregation (that (perform (for (some god)) (some worship)))))";
        Clause Head = (Clause) AnalogyManager.ConvertToOOP(input);
        names.add("serve");
        names.add("some");
        names.add("that");
        names.add("perform");
        names.add("for");
        names.add("some");
        names.add("some");

        ArrayList<AnalogicalObject> children = Head.getAllChildren();
        for (int i = 0; i < children.size(); i++) {
            assertEquals(children.get(i).getName(),names.get(i));
        }
    }

    @Test
    public void ParentTest() throws IllegalAccessException {
        String input = "(serve priest (some congregation (that (perform (for (some god)) (some worship)))))";
        Clause Head = (Clause) AnalogyManager.ConvertToOOP(input);
        assertEquals(Head, Head.getChildren().getFirst().getParent());
    }

    @Test
    public void nullSubjectTest() throws IllegalAccessException {
        String input = "(serve priest (some congregation (that (perform (for (some god)) (some worship)))))";
        Clause Head = (Clause) AnalogyManager.ConvertToOOP(input);
        Head = (Clause) AnalogyManager.ConvertToOOP("(One (Two 2))" );
        assertNull(Head.getChildren().get(0));
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


}
