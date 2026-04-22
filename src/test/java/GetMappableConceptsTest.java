import org.junit.Before;
import org.junit.Test;
import org.main.AnalogyDataHolder;
import org.main.AnalogyManager;
import org.main.ConfigSetup;
import org.main.Objects.Clause;
import org.main.Objects.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GetMappableConceptsTest {

    Config testconfig;
    private static final HashMap<String, ArrayList<String>> analogies = new HashMap<>();
    @Before
    public void init(){
        testconfig = ConfigSetup.applyConfig("testconfig.txt");
        ArrayList<String> t = new ArrayList<String>(testconfig.getTargets());
        t.add("barbarian");
        testconfig.setTargets(t);
        AnalogyDataHolder.addAnalogiesFromFile(testconfig);
    }

    @Test
    public void topicRetrievalSimpleCheck(){
        assertEquals(1,AnalogyDataHolder.getMappableConcepts("Adonis").size());
        assertTrue(true);
    }

    @Test
    public void hashingTest1(){
        assertEquals(4, AnalogyDataHolder.getStructureHash().size());
    }

    @Test
    public void hashesMatch(){
        ArrayList<String> matchingHashes = AnalogyDataHolder.getStructureHash().get(-2035237565);
        Clause pred1 = (Clause)AnalogyManager.ConvertToOOP(matchingHashes.get(0));
        Clause pred2 = (Clause)AnalogyManager.ConvertToOOP(matchingHashes.get(1));
        assertEquals(AnalogyManager.convertToAbstractString(pred1, false), AnalogyManager.convertToAbstractString(pred2, false));
    }

    @Test
    public void nonMatchingHashes(){
        String str1 = AnalogyDataHolder.getStructureHash().get(-2035237565).getFirst();
        String str2 = AnalogyDataHolder.getStructureHash().get(-1135976958).getFirst();
        Clause pred1 = (Clause)AnalogyManager.ConvertToOOP(str1);
        Clause pred2 = (Clause)AnalogyManager.ConvertToOOP(str2);

        assertNotEquals(AnalogyManager.convertToAbstractString(pred1, false), AnalogyManager.convertToAbstractString(pred2, false));
    }
}
