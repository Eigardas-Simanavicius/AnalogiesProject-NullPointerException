import org.junit.Before;
import org.junit.Test;
import org.main.AnalogyDataHolder;
import org.main.ConfigSetup;
import org.main.Interfaces.Predicate;
import org.main.MappingManager;
import org.main.Objects.Config;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class getMappableSourceAnalogiesForTest {

    @Before
    public void setup(){
        ConfigSetup.setupConfig(new File("config.txt"));
        AnalogyDataHolder.addAnalogiesFromFile("structured domains.txt",new Config());
    }

    @Test
    public void basicTest(){
        ArrayList<Predicate> c = MappingManager.getMappableSourceAnalogiesFor("AIDS");
        assertEquals(21431,c.size());
    }

}
