import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.main.AnalogyManager;
import org.main.Interfaces.Predicate;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Parameterized.class)
public class convertToOOPTest {

    @Parameterized.Parameter(0)
    public String input;

    @Parameterized.Parameter(1)
    public String output;

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        Object[][] data = new Object[][]{
                {"(work in scientist (some lab (that (conduct experiment))))","(work in 0 (some 1 (that (conduct 2))))"}
        };
        return Arrays.asList(data);
    }

    @Test
    public void BasicTest(){
        Predicate p = AnalogyManager.ConvertToOOP(input);
        assertEquals(output,AnalogyManager.convertToFlatAbstractString(p));
    }
}
