import org.junit.Before;
import org.junit.Test;
import org.main.AnalogyDataHolder;
import org.main.CompositeBuilder;
import org.main.ConfigSetup;
import org.main.MappingManager;
import org.main.Objects.Config;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class getNBestSourcesForTest {
    @Before
    public void setup(){
        ConfigSetup.setupConfig(new File("config.txt"));
        AnalogyDataHolder.addAnalogiesFromFile(new Config());
    }

    @Test
    public void basicTest1(){
        long s = System.currentTimeMillis();
        ArrayList<String> c = MappingManager.getNBestSourcesFor("AIDS",10);
        System.out.println("Fake Input Test 1 : " + (System.currentTimeMillis()-s)/1000.0);
        assertEquals(10,c.size());
        assertFalse(c.contains(null));
    }

    @Test
    public void basicTest2(){
        long s = System.currentTimeMillis();
        ArrayList<String> c = MappingManager.getNBestSourcesFor("Adonis",30);
        System.out.println("Basic Input Test 2 : " + (System.currentTimeMillis()-s)/1000.0);
        assertEquals(30,c.size());
        assertFalse(c.contains(null));
    }

    @Test
    public void largeRequestTest(){
        long s = System.currentTimeMillis();
        ArrayList<String> c = MappingManager.getNBestSourcesFor("AIDS",100000000);
        System.out.println("Large Request Test : " + (System.currentTimeMillis()-s)/1000.0);
        assertTrue(AnalogyDataHolder.getAnalogies().size()>c.size());
        assertFalse(c.contains(null));
    }

    @Test
    public void fakeInputTest(){
        long s = System.currentTimeMillis();
        ArrayList<String> c = MappingManager.getNBestSourcesFor("FakeTopicAlert",100000000);
        System.out.println("Fake Input Test : " + (System.currentTimeMillis()-s)/1000.0);
        assertEquals(0,c.size());
        assertFalse(c.contains(null));
    }

    @Test
    public void blankInputTest(){
        long s = System.currentTimeMillis();
        ArrayList<String> c = MappingManager.getNBestSourcesFor("",100000000);
        System.out.println("Blank Input Test : " + (System.currentTimeMillis()-s)/1000.0);
        assertEquals(0,c.size());
        assertFalse(c.contains(null));
    }


}
