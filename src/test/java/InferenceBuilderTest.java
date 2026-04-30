import org.junit.After;
import org.junit.Test;
import org.main.AnalogyDataHolder;
import org.main.ConfigSetup;
import org.main.InferenceBuilder;
import org.main.MappingManager;
import org.main.Objects.CoalescentMapping;
import org.main.Objects.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InferenceBuilderTest {

    @Test
    public void inferredAnalogiesTest(){
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (can (train.0 *Adonis body) ) (then (display *Adonis body)))");
        AnalogyDataHolder.addAnalogyToHash("(if (can (train.0 *Adonis body) ) (then (display *Adonis arm)))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");

        ArrayList<String> mapping = new ArrayList<>(Collections.singleton("(if (can (train.0 *Adonis body) ) (then (display *Adonis body)))"));

        CoalescentMapping coalescentMapping = MappingManager.createNewCoalesentMapping("Adonis","barbarian");
        ArrayList<String> inferenceSources = InferenceBuilder.getInferredAnalogies(coalescentMapping);
        assertEquals(mapping, inferenceSources);
        assertEquals("(if (can (train.0 *barbarian self) ) (then (display *barbarian self)))", InferenceBuilder.getInferredMappings(coalescentMapping).get(inferenceSources.get(0)));
    }

    @Test
    public void inferredAnalogiesTest2(){
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian arm) (display *barbarian arm))");
        AnalogyDataHolder.addAnalogyToHash("(if (can (train.0 *Adonis body) ) (then (display *Adonis body)))");
        AnalogyDataHolder.addAnalogyToHash("(if (can (train.0 *Adonis body) ) (then (display *Adonis leg)))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis leg) (display *Adonis leg))");

        ArrayList<String> mapping = new ArrayList<>(List.of("(if (can (train.0 *Adonis body) ) (then (display *Adonis body)))","(if (can (train.0 *Adonis body) ) (then (display *Adonis leg)))"));

        CoalescentMapping coalescentMapping = MappingManager.createNewCoalesentMapping("Adonis","barbarian");

        assertEquals(mapping, InferenceBuilder.getInferredAnalogies(coalescentMapping));
    }


    @Test
    public void comprehensiveTest(){
        ConfigSetup.resetConfig();
        Config t = ConfigSetup.applyConfig("config.txt");
        AnalogyDataHolder.addAnalogiesFromFile(t);

        CoalescentMapping coalescentMapping = MappingManager.createNewCoalesentMapping("destruction","AIDS");

        assertEquals(28,InferenceBuilder.getInferredAnalogies(coalescentMapping).size());
    }



    @Test
    public void inferredMappingsTest(){
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (can (train.0 *Adonis body) ) (then (display *Adonis body)))");
        AnalogyDataHolder.addAnalogyToHash("(if (can (train.0 *Adonis body) ) (then (display *Adonis arm)))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");

        HashMap<String,String> mapping = new HashMap<>();

        mapping.put("(if (can (train.0 *Adonis body) ) (then (display *Adonis body)))","(if (can (train.0 *barbarian self) ) (then (display *barbarian self)))");

        CoalescentMapping coalescentMapping = MappingManager.createNewCoalesentMapping("Adonis","barbarian");

        assertEquals(mapping, InferenceBuilder.getInferredMappings(coalescentMapping));
    }

    @Test
    public void inferredMappingsTest2(){
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian arm) (display *barbarian arm))");
        AnalogyDataHolder.addAnalogyToHash("(if (can (train.0 *Adonis body) ) (then (display *Adonis body)))");
        AnalogyDataHolder.addAnalogyToHash("(if (can (train.0 *Adonis body) ) (then (display *Adonis leg)))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis leg) (display *Adonis leg))");

        HashMap<String,String> mapping = new HashMap<>();

        mapping.put("(if (can (train.0 *Adonis body) ) (then (display *Adonis body)))","(if (can (train.0 *barbarian self) ) (then (display *barbarian self)))");
        mapping.put("(if (can (train.0 *Adonis body) ) (then (display *Adonis leg)))","(if (can (train.0 *barbarian self) ) (then (display *barbarian arm)))");

        CoalescentMapping coalescentMapping = MappingManager.createNewCoalesentMapping("Adonis","barbarian");

        assertEquals(mapping, InferenceBuilder.getInferredMappings(coalescentMapping));
    }

    @After
    public void reset(){
        ConfigSetup.resetConfig();
    }
}
