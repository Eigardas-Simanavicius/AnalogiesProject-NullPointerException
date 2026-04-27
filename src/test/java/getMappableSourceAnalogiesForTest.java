import org.junit.After;
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
        AnalogyDataHolder.addAnalogiesFromFile(new Config());
    }

    @Test
    public void basicTest(){
        long s = System.currentTimeMillis();
        ArrayList<Predicate> c = MappingManager.getMappableSourceAnalogiesFor("AIDS");
        System.out.println((System.currentTimeMillis()-s)/1000.0);
        assertEquals(4583,c.size());
    }

    @After
    public void reset(){
        ConfigSetup.resetConfig();
    }

}
