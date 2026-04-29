import org.junit.After;
import org.junit.Test;
import org.main.AnalogyDataHolder;
import org.main.ConfigSetup;
import org.main.MappingManager;
import org.main.Objects.CoalescentMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CoalescentMappingGetInferredMappingTest {
    @Test
    public void basicTest(){
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");

        HashMap<String, String> mapping = new HashMap<>();

        mapping.put("body","self");
        mapping.put("*Adonis","*barbarian");

        CoalescentMapping coalescentMapping = MappingManager.createNewCoalesentMapping("Adonis","barbarian");

        assertEquals(mapping,coalescentMapping.getInferredMapping());
    }

    @Test
    public void basicTest2(){
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian arm) (display *barbarian arm))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis leg) (display *Adonis leg))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis leg))");

        HashMap<String, String> mapping = new HashMap<>();

        mapping.put("body","self");
        mapping.put("leg","arm");
        mapping.put("*Adonis","*barbarian");

        CoalescentMapping coalescentMapping = MappingManager.createNewCoalesentMapping("Adonis","barbarian");

        assertEquals(mapping,coalescentMapping.getInferredMapping());
    }

    @After
    public void reset(){
        ConfigSetup.resetConfig();
    }
}
