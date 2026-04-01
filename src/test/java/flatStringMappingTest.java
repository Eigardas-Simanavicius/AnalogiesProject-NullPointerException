import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.main.AnalogyManager;
import org.main.Interfaces.Predicate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class flatStringMappingTest {

    @Parameter(0)
    public String inputSource;

    @Parameter(1)
    public String inputTarget;

    @Parameter(2)
    public Map<String,String> output;

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        Object[][] data = new Object[][]{
                {"(work_in scientist (some lab (that (conduct experiment))))","(work_in scientist1 (some lab1 (that (conduct experiment1))))", Map.of("scientist","scientist1","lab","lab1","experiment","experiment1")},
                {"(work_in scientist (some lab (that (conduct lab))))","(work_in 0 (some 1 (that (conduct 1))))",Map.of("scientist","0","lab","1")},
                {"(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))","(if (can (cause.0 * (some 0 (when 1 (crush 2))))) (can (succeed_at * (crush 2))))",Map.of("*AIDS","*","death","0","crushing","1","something","2")}
        };
        return Arrays.asList(data);
    }

    @Test
    public void BasicTest() throws IllegalAccessException {
        assertEquals(output,AnalogyManager.flatStringMapping(inputSource,inputTarget));
    }
}
