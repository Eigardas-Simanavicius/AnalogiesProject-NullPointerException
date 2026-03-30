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
public class convertToPrettifiedAbstractStringTest {

    @Parameter(0)
    public String input;

    @Parameter(1)
    public String output;

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        Object[][] data = new Object[][]{
                {"(work_in scientist (some lab (that (conduct experiment))))","(work_in 0\n\t(some 1\n\t\t(that\n\t\t\t(conduct 2))))"},
                {"(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))","(if\n\t(can\n\t\t(cause.0 *\n\t\t\t(some 0\n\t\t\t\t(when 1\n\t\t\t\t\t(crush 2)))))\n\t(can\n\t\t(succeed_at *\n\t\t\t(crush 2))))"}
        };
        return Arrays.asList(data);
    }

    @Test
    public void BasicTest() throws IllegalAccessException {
        Predicate p = AnalogyManager.ConvertToOOP(input);
        assertEquals(output,AnalogyManager.convertToAbstractString(p,true));
    }
}
