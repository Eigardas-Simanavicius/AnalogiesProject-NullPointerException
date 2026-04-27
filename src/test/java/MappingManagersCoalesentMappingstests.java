import org.junit.After;
import org.junit.Test;
import org.main.AnalogyDataHolder;
import org.main.CompositeBuilder;
import org.main.ConfigSetup;
import org.main.MappingManager;
import org.main.Objects.CoalescentMapping;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MappingManagersCoalesentMappingstests {

    @Test
    public void getMappingRichnessTest(){
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");
        CompositeBuilder comp = new CompositeBuilder();
        ArrayList<String> composite = comp.buildCompositeAnalogy("Adonis", "barbarian");
        assertEquals(2, MappingManager.getMappingRichness(composite));
    }

    @Test
    public void createNewCoalesentMapTest(){
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");
        ArrayList<String> string = new ArrayList<>();
        string.add("(if (train.0 *Adonis body) (display *Adonis body))");
        string.add("(if (train.0 *barbarian self) (display *barbarian self))");
        CoalescentMapping mapping = MappingManager.createNewCoalesentMapping("Adonis","barbarian");
        assertEquals(mapping.getMapping(), string);
    }

    @After
    public void reset(){
        ConfigSetup.resetConfig();
    }
}
