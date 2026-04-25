import org.junit.Before;
import org.junit.Test;
import org.main.AnalogyDataHolder;
import org.main.CompositeBuilder;
import org.main.ConfigSetup;
import org.main.Objects.Config;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompositeBuilderTest {
    Config testconfig;

    @Test
    public void simpleTest(){
        testconfig = ConfigSetup.applyConfig("testconfig.txt");
        ArrayList<String> t = new ArrayList<String>(testconfig.getTargets());
        t.add("barbarian");
        testconfig.setTargets(t);
        AnalogyDataHolder.addAnalogiesFromFile(testconfig);
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");
        CompositeBuilder comp = new CompositeBuilder();
        ArrayList<String> composite = comp.buildCompositeAnalogy("Adonis", "barbarian");
        assertEquals(4, composite.size());
        assertTrue(composite.contains("(if (train.0 *Adonis body) (display *Adonis body))"));
    }

    @Test
    public void multipleSearchesDifferentStructures(){
        //first two that map to each other on the first pass
        AnalogyDataHolder.addAnalogyToHash("(p1 *s1 (p-1 (p2 s2) (p3 s3)))");
        AnalogyDataHolder.addAnalogyToHash("(p1 *s1.2 (p-1 (p2 s2.2) (p3 s3.2)))");
        //next four analogies are unmappable if the first two are mapped to each other
        AnalogyDataHolder.addAnalogyToHash("(p4 *s1 (p5 s2) (p10 s45))");
        AnalogyDataHolder.addAnalogyToHash("(p4 *s1.2 (p5 s2) (p10 s45))");
        AnalogyDataHolder.addAnalogyToHash("(p6 *s1 (p7 s2))");
        AnalogyDataHolder.addAnalogyToHash("(p6 *s1.2 (p7 s2))");
        CompositeBuilder comp = new CompositeBuilder();
        ArrayList<ArrayList<String>> composite = comp.buildMultipleCompositeAnalogies("s1", "s1.2", 2);
        assertEquals(2, composite.size());
        assertEquals(4, composite.get(1).size());
        assertEquals(2, composite.getFirst().size());
    }

    @Test
    public void multipleSearchesSameStructures(){
        //first two that map to each other on the first pass
        AnalogyDataHolder.addAnalogyToHash("(p1.1 *s1.1 (p-1.1 (p2.1 s2.1)))");
        AnalogyDataHolder.addAnalogyToHash("(p1.1 *s1.2.1 (p-1.1 (p2.1 s2.2.1)))");
        //next four analogies are unmappable if the first two are mapped to each other
        AnalogyDataHolder.addAnalogyToHash("(p4 *s1.1 (p5 s2.1) (p10 s45.1))");
        AnalogyDataHolder.addAnalogyToHash("(p4 *s1.2.1 (p5 s2.2.2) (p10 s45.2))");
        AnalogyDataHolder.addAnalogyToHash("(p6 *s1.1 (p7 s2.1) (p10 s45.3))");
        AnalogyDataHolder.addAnalogyToHash("(p6 *s1.2.1 (p7 s2.2.2) (p10 s45.4))");
        CompositeBuilder comp = new CompositeBuilder();
        ArrayList<ArrayList<String>> composite = comp.buildMultipleCompositeAnalogies("s1.1", "s1.2.1", 2);
        assertEquals(2, composite.size());
        if(composite.getFirst().size() == 2){
            assertEquals(4,composite.get(1).size());
            System.out.println();
        }
        else{
            assertEquals(4, composite.getFirst().size());
            assertEquals(2, composite.get(1).size());
        }
    }

    @Test
    public void sameRichnessStructures(){
        AnalogyDataHolder.addAnalogyToHash("(sameRichness1 *1.1.0 (sameRichness2 2.1.0))");
        AnalogyDataHolder.addAnalogyToHash("(sameRichness1 *1.2.0 (sameRichness2 2.2.0))");
        AnalogyDataHolder.addAnalogyToHash("(sameRichness3 *1.1.0 (sameRichness4 4.1.0))");
        AnalogyDataHolder.addAnalogyToHash("(sameRichness3 *1.2.0 (sameRichness4 5.1.0))");
        CompositeBuilder comp = new CompositeBuilder();
        ArrayList<String> composite = comp.buildCompositeAnalogy("1.1.0", "1.2.0");
        assertEquals(4, composite.size());
    }


    @Test
    public void unmappableStructures(){
        AnalogyDataHolder.addAnalogyToHash("(explode (completely *building))");
        AnalogyDataHolder.addAnalogyToHash("(detonate *location (completely *location))");
        CompositeBuilder comp = new CompositeBuilder();
        assertTrue(comp.buildCompositeAnalogy("building", "location").isEmpty());
    }

    @Test
    public void subjectMismatch(){
        AnalogyDataHolder.addAnalogyToHash("(modify (with ink (completely *narrative)))");
        AnalogyDataHolder.addAnalogyToHash("(modify (with *story (completely *story)))");
        CompositeBuilder comp = new CompositeBuilder();
        assertTrue(comp.buildCompositeAnalogy("story", "narrative").isEmpty());
    }

    @Test
    public void partialSubjectMismatch(){
        AnalogyDataHolder.addAnalogyToHash("(detonate *place (completely *place))");
        AnalogyDataHolder.addAnalogyToHash("(detonate *locale (completely *locale))");
        AnalogyDataHolder.addAnalogyToHash("(if person (can (find *place)) (can (rest person (in *place))))");
        AnalogyDataHolder.addAnalogyToHash("(if person (can (find *locale)) (can (rest person (in *locale))))");
        AnalogyDataHolder.addAnalogyToHash("(can person (get_lost finding *place))");
        AnalogyDataHolder.addAnalogyToHash("(can creature (get_lost finding *locale))");
        CompositeBuilder comp = new CompositeBuilder();
        ArrayList<String> composite = comp.buildCompositeAnalogy("place", "locale");

        assertEquals(4, composite.size());
        assertTrue(!composite.contains("(can creature (get_lost finding *location))"));
    }

    @Test
    public void requestMoreSearchesThanPossible(){
        AnalogyDataHolder.addAnalogyToHash("(be *Geoff beta)");
        AnalogyDataHolder.addAnalogyToHash("(be *George sigma)");
        AnalogyDataHolder.addAnalogyToHash("(be *George Delta)");
        ArrayList<ArrayList<String>> composite = new CompositeBuilder().buildMultipleCompositeAnalogies("George","Geoff",45);
        assertEquals(2, composite.size());
    }

    @Test
    public void diffNumAnalogiesForSameRichnessLevel(){
        AnalogyDataHolder.addAnalogyToHash("(be *Jeff beta)");
        AnalogyDataHolder.addAnalogyToHash("(be *Greg sigma)");
        AnalogyDataHolder.addAnalogyToHash("(be *Greg Delta)");
        ArrayList<ArrayList<String>> composite = new CompositeBuilder().buildMultipleCompositeAnalogies("Greg","Jeff",2);
        assertEquals(2, composite.size());
    }

}
