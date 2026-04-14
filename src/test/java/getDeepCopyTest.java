import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.main.AnalogyManager;
import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;

import javax.swing.*;
import java.util.*;

@RunWith(Parameterized.class)
public class getDeepCopyTest {

    @Parameter
    public AnalogicalObject input;

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {AnalogyManager.ConvertToOOP("(by working (perform scientist (some work (for laboratory (that (conduct experiment))))))")},
                {AnalogyManager.ConvertToOOP("(work_in scientist (some laboratory (that (conduct experiment))))")},
                {AnalogyManager.ConvertToOOP("(Serve Priest Congregation)")},
                {AnalogyManager.ConvertToOOP("(empty)")}
        };
        return Arrays.asList(data);
    }

    @Test
    public void deepCopyTest() {
        if(input instanceof Predicate){
            Predicate copy = (Predicate) input.getDeepCopy();
            assert copy != input;

            Queue<AnalogicalObject> inputChildren = new ArrayDeque<>(((Predicate)input).getChildren());
            Queue<AnalogicalObject> copyChildren = new ArrayDeque<>(copy.getChildren());

            while(!inputChildren.isEmpty() && !copyChildren.isEmpty()){
                assert inputChildren.size() == copyChildren.size();

                AnalogicalObject inputChild = inputChildren.remove();
                AnalogicalObject copyChild = copyChildren.remove();

                if(inputChild instanceof Predicate){
                    assert copyChild instanceof Predicate;

                    inputChildren.addAll(((Predicate) inputChild).getChildren());
                    copyChildren.addAll(((Predicate) copyChild).getChildren());
                }else{
                    assert !(copyChild instanceof Predicate);
                }

                assert inputChild != copyChild;
                assert inputChild.getName().equals(copyChild.getName());

            }

            assert inputChildren.size() == copyChildren.size();

        }else{
            AnalogicalObject copy = input.getDeepCopy();
            assert copy != input;
            assert copy.getName().equals(input.getName());
        }
    }
}
