import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.main.AnalogyManager;
import org.main.Interfaces.Predicate;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class convertToFlatAbstractStringTest {

    @Parameter(0)
    public String input;

    @Parameter(1)
    public String output;

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        Object[][] data = new Object[][]{
                {"(work in scientist (some lab (that (conduct experiment))))","(work in 0 (some 1 (that (conduct 2))))"},
                {"(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))","(if (can (cause.0 * (some 0 (when 1 (crush 2))))) (can (succeed_at * (crush 2))))"}
        };
        return Arrays.asList(data);
    }

    @Test
    public void BasicTest(){
        Predicate p = AnalogyManager.ConvertToOOP(input);
        assertEquals(output,AnalogyManager.convertToFlatAbstractString(p));
    }
}
