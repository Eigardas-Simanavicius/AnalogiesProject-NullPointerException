import org.junit.Test;
import org.main.AnalogyManager;
import org.main.Interfaces.Predicate;
import org.main.Objects.Clause;
import org.main.Interfaces.AnalogicalObject;
import org.main.Objects.Subject;
import static org.junit.Assert.assertEquals;

public class getPredicateRichnessTest {
    @Test
    public void basicTest1(){
        Predicate predicate = AnalogyManager.ConvertToOOP("(by working (perform scientist (some work (for laboratory (that (conduct experiment))))))");
        assertEquals(5.049,AnalogyManager.getPredicateRichness(predicate),0.002);
    }

    @Test
    public void basicTest2(){
        Predicate predicate = AnalogyManager.ConvertToOOP("(work_in scientist (some laboratory (that (conduct experiment))))");
        assertEquals(3.049,AnalogyManager.getPredicateRichness(predicate),0.002);
    }

    @Test
    public void basicTest3(){
        Predicate predicate = AnalogyManager.ConvertToOOP("(Serve Priest Congregation)");
        assertEquals(0.301029995664,AnalogyManager.getPredicateRichness(predicate),0.002);
    }
}
