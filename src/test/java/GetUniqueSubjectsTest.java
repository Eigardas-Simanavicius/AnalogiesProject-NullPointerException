import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.main.AnalogyManager;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class GetUniqueSubjectsTest {

    @Parameterized.Parameter(0)
    public String input;

    @Parameterized.Parameter(1)
    public HashSet<String> output;

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        Object[][] data = new Object[][]{
                {"(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))",new HashSet<>(List.of("*AIDS","death","crushing","something"))},
                {"(if (train.0 *barbarian self) (display *barbarian self))",new HashSet<>(List.of("*barbarian","self"))}
        };
        return Arrays.asList(data);
    }

    @Test
    public void basicTest(){
        assertEquals(output, AnalogyManager.getUniqueSubjects(input));
    }
}
