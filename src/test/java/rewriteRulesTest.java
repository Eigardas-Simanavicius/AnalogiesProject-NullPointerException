import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.main.AnalogyManager;
import org.main.Interfaces.Predicate;
import org.main.rewriteRule;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class rewriteRulesTest {

    @Parameterized.Parameter(0)
    public String input;

    @Parameterized.Parameter(1)
    public String output;

    @Parameterized.Parameter(2)
    public rewriteRule rule;

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        Object[][] data = new Object[][]{
                //[original predicate]         {modifiers}[verbPredicate]_[prepositionPredicate]:[newArgument]{optional asterisk}&[byArgument]
                {"(exercise *athlete muscle) ","(by exercising( perform *athlete exercise(of muscle)))",new rewriteRule("exercise"," perform_of:exercise*&exercising")},
                {"(flex *athlete muscle)","(by flexing(display *athlete muscle(with effort)))", new rewriteRule("flex","display_with:effort&flexing")},
                {"(dislike *rival colleague)","(by disliking(not(respect *rival colleague(as friend))))",new rewriteRule("dislike","!respect_as:friend&disliking")},
               // {"(lose_control_over *captain mutineer)","(by rejecting_control(not (respect mutineer *captain (as leader))))", new rewriteRule("lose_control_over","<!respect_as:leader&rejecting_control")},
                {"(flunk *student exam)","(by flunking(reject teacher *student(for exam)))", new rewriteRule("flunk","^reject_for:teacher&flunking")}
        };
        return Arrays.asList(data);
    }

    @Test
    public void reWriteTest() {
        Object[][] data = new Object[][]{
                //[original predicate]         {modifiers}[verbPredicate]_[prepositionPredicate]:[newArgument]{optional asterisk}&[byArgument]
                {"(exercise *athlete muscle) ","(by exercising( perform *athlete exercise(of muscle)))",new rewriteRule("exercise"," perform_of:exercise*&exercising")},
                {"(flex *athlete muscle)","(by flexing(display *athlete muscle(with effort)))", new rewriteRule("flex","display_with:effort&flexing")},
                {"(dislike *rival colleague)","(by disliking(not(respect *rival colleague(as friend))))",new rewriteRule("dislike","!respect_as:friend&disliking")},
                {"(lose_control_over *captain mutineer)","(by rejecting_control(not (respect mutineer *captain (as leader))))", new rewriteRule("lose_control_over","<!respect_as:leader&rejecting_control")},
                {"(flunk *student exam)","(by flunking(reject teacher *student(for exam)))", new rewriteRule("flunk","^reject_for:teacher&flunking")}
        };
        Predicate p = AnalogyManager.ConvertToOOP(input);
        assertEquals(output,rule.rewrite(p).toString());
    }


        @Test
        public void hasPredicateChildrenTest() {
            Predicate p = AnalogyManager.ConvertToOOP("(exercise athelete muscle (big mac))");
            assertThrows(IllegalArgumentException.class, () -> {
                new rewriteRule("exercise", " perform_of:exercise*&exercising").rewrite(p);
            });
        }

        @Test
        public void nonMatchingRuleException() {
            Predicate p = AnalogyManager.ConvertToOOP("(exercise athelete muscle (big mac))");
            assertThrows(IllegalArgumentException.class, () -> {
                new rewriteRule("explode", " perform_of:exercise*&exercising").rewrite(p);
            });
        }
}
