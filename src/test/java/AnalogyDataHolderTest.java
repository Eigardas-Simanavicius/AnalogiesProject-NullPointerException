import org.junit.Before;
import org.junit.Test;
import org.main.AnalogyDataHolder;
import org.main.ConfigSetup;
import org.main.MappingManager;
import org.main.Objects.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnalogyDataHolderTest {

    Config testconfig;
    private static final HashMap<String, ArrayList<String>> analogies = new HashMap<>();
    @Before
    public void init(){
        testconfig = ConfigSetup.applyConfig("testconfig.txt");
        analogies.put("AIDS",new ArrayList<>());
        analogies.get("AIDS").add("(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))");
        analogies.get("AIDS").add("(if (by causing (produce *AIDS (some death (when crushing (crush something))) (for victim))) (can (succeed_at *AIDS (crush something))))");
        analogies.put("Adonis",new ArrayList<>());
        analogies.get("Adonis").add("(if (can (exercise.0 *Adonis muscle)) (can (flex.0 *Adonis muscle)))");

    }

    @Test
    public void checkIfAllRead(){
        AnalogyDataHolder.addAnalogiesFromFile(testconfig.getAnalogiesFilePath(),testconfig);
        HashMap<String, ArrayList<String>> analogiesRecieved = AnalogyDataHolder.getAnalogies();
        for(String key: analogiesRecieved.keySet()){
            assertEquals(analogies.get(key),analogiesRecieved.get(key));
        }
    }

    @Test
    public void checkStructureHash(){
        String newAnalogy = "(if (can (exercise.0 *Greg muscle)) (can (flex.0 *Greg muscle)))";
        AnalogyDataHolder.addAnalogiesFromFile(testconfig.getAnalogiesFilePath(),testconfig);
        HashMap<Integer, ArrayList<String>> structuresHash = AnalogyDataHolder.getStructureHash();
        assertEquals("(if (can (exercise.0 *Adonis muscle)) (can (flex.0 *Adonis muscle)))", structuresHash.get(AnalogyDataHolder.hashPredicate(newAnalogy)).getFirst());
    }

}
