import org.junit.After;
import org.junit.Test;
import org.main.AnalogyDataHolder;
import org.main.ConfigSetup;
import org.main.MappingManager;
import org.main.Objects.CoalescentMapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CoalescentMappingGetMappingTest {
    @Test
    public void basicTest(){
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");

        HashMap<String,String> mapping = new HashMap<>();
        mapping.put("(if (train.0 *Adonis body) (display *Adonis body))","(if (train.0 *barbarian self) (display *barbarian self))");

        CoalescentMapping coalescentMapping = MappingManager.createNewCoalesentMapping("Adonis","barbarian");

        assertEquals(mapping,coalescentMapping.getCoalescedMapping());
    }

    @Test
    public void basicTest2(){
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian arm) (display *barbarian arm))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis leg) (display *Adonis leg))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis leg))");

        HashMap<String,String> mapping = new HashMap<>();
        mapping.put("(if (train.0 *Adonis body) (display *Adonis body))","(if (train.0 *barbarian self) (display *barbarian self))");
        mapping.put("(if (train.0 *Adonis leg) (display *Adonis leg))","(if (train.0 *barbarian arm) (display *barbarian arm))");

        CoalescentMapping coalescentMapping = MappingManager.createNewCoalesentMapping("Adonis","barbarian");

        assertEquals(mapping,coalescentMapping.getCoalescedMapping());
    }

    @After
    public void reset(){
        ConfigSetup.resetConfig();
    }
}
