import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.main.AnalogyManager;
import org.main.Interfaces.Predicate;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class getPredicateRichnessTest {

    @Parameter(0)
    public Predicate input;

    @Parameter(1)
    public double output;

    private double delta = 0.002;

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {AnalogyManager.ConvertToOOP("(by working (perform scientist (some work (for laboratory (that (conduct experiment))))))"), 5.049},
                {AnalogyManager.ConvertToOOP("(work_in scientist (some laboratory (that (conduct experiment))))"), 3.049},
                {AnalogyManager.ConvertToOOP("(Serve Priest Congregation)"), 0.301029995664},
                {null, -1.0},
                {AnalogyManager.ConvertToOOP("(empty)"), Double.NEGATIVE_INFINITY}
        };
        return Arrays.asList(data);
    }

    @Test
    public void basicTest() {
        assertEquals(output, AnalogyManager.getPredicateRichness(input), delta);
    }
}
