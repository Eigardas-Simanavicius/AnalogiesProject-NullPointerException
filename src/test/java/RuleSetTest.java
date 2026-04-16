import org.junit.Test;
import org.main.Objects.RewriteRule;
import org.main.Objects.RuleSet;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RuleSetTest {


    @Test
    public void simpleTest() throws FileNotFoundException{
        RuleSet rules = new RuleSet("rewrite_rules.txt");
        assertNotNull(rules);
    }

    @Test
    public void getRuleTest() throws FileNotFoundException{
        RuleSet rules = new RuleSet("rewrite_rules.txt");
        ArrayList<RewriteRule> fetchedRules = rules.getRulesFor("exercise");
        assertFalse(fetchedRules.isEmpty());
    }

    @Test
    public void getRuleTest2() throws FileNotFoundException{
        RuleSet rules = new RuleSet("rewrite_rules.txt");
        ArrayList<RewriteRule> fetchedRules = rules.getRulesFor("exercise");
        assertEquals("perform_of:exercise*&exercising",fetchedRules.getFirst().toString());
    }

    @Test
    public void getRuleAsStringTest() throws FileNotFoundException{
        RuleSet rules = new RuleSet("rewrite_rules.txt");
        ArrayList<String> fetchedRules = rules.getRulesAsStringFor("exercise");
        assertFalse(fetchedRules.isEmpty());
    }

    @Test
    public void getRuleAsStringTest2() throws FileNotFoundException{
        RuleSet rules = new RuleSet("rewrite_rules.txt");
        ArrayList<String> fetchedRules = rules.getRulesAsStringFor("exercise");
        assertEquals("perform_of:exercise*&exercising",fetchedRules.getFirst());
    }

    @Test
    public void multipleRulesCheck() throws FileNotFoundException{
        RuleSet rules = new RuleSet("rewrite_rules.txt");
        ArrayList<String> fetchedRules = rules.getRulesAsStringFor("exercise_on");
        assertEquals(2, fetchedRules.size());
    }
}
