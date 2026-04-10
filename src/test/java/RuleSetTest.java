import org.junit.Test;
import org.main.RuleSet;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RuleSetTest {


    @Test
    public void simpleTest() throws FileNotFoundException{
        RuleSet rules = new RuleSet("rewrite rules.txt");
        assertTrue(true);
    }
}
