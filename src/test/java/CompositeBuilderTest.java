import org.junit.Before;
import org.junit.Test;
import org.main.AnalogyDataHolder;
import org.main.CompositeBuilder;
import org.main.ConfigSetup;
import org.main.Objects.Config;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class CompositeBuilderTest {
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
    public void simpleTest(){
        System.out.println(CompositeBuilder.buildCompositeAnalogy("Adonis", "barbarian"));
        assertTrue(true);
    }
}
