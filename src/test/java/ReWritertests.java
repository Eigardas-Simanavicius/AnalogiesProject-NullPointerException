import org.jspecify.annotations.NonNull;
import org.junit.After;
import org.junit.Test;
import org.main.AnalogyManager;
import org.main.ConfigSetup;
import org.main.Objects.Clause;
import org.main.Interfaces.Predicate;
import org.main.ReWriter;
import org.main.Objects.RewriteRule;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReWritertests  {


    @Test
    public void retainChildrenTest(){
        RewriteRule rule1 = new RewriteRule("exercise","preform_of:exercise*&exercising");
        RewriteRule rule2 = new RewriteRule("explode","destroy_of:explode*&exploding");
        ArrayList<RewriteRule> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        Clause testClause2 = (Clause)AnalogyManager.ConvertToOOP("(Sigma male (Whopper jr) (exercise.0 athelete muscle) (explode Gregs legs))");
        ArrayList<Predicate> ans = ReWriter.reWriteAnalogyAllPermutations(rules,testClause2);
        System.out.println(ans.toString());
        assertEquals("(Sigma male(Whopper jr)(by exercising(preform athelete exercise(of muscle)))(by exploding(destroy Gregs explode(of legs))))", ((Clause) (ans.getFirst())).toString());
    }

    @Test
    public void allPermutationsTest(){
        ArrayList<RewriteRule> rules = getRewriteRules();

        Clause testClause1 = (Clause)AnalogyManager.ConvertToOOP("(Sigma male (exercise.0 athelete muscle) (explode Gregs legs))");
        Clause testClause2 = (Clause)AnalogyManager.ConvertToOOP("(Sigma male (exercise.0 athelete muscle) (explode Gregs legs) (walk Steve road))");
        ArrayList<Predicate> ans = ReWriter.reWriteAnalogyAllPermutations(rules,testClause1);
        ArrayList<Predicate> ans2 = ReWriter.reWriteAnalogyAllPermutations(rules,testClause2);
        assertEquals(4, ans.size());
        assertEquals(8, ans2.size());

    }

    @Test
    public void testNullRewriteLog(){
        ArrayList<RewriteRule> rules = new ArrayList<>();
        RewriteRule rule1 = new RewriteRule("explode","preform_of:exercise*&exercising");
        rules.add(rule1);
        Clause testClause1 = (Clause)AnalogyManager.ConvertToOOP("(Sigma male (exercise Gregs legs) )");
        assertNull(ReWriter.reWriteAnalogyAllPermutations(rules,testClause1));


    }

    private static @NonNull ArrayList<RewriteRule> getRewriteRules() {
        RewriteRule rule1 = new RewriteRule("exercise","preform_of:exercise*&exercising");
        RewriteRule rule2 = new RewriteRule("explode","destroy_of:explode*&exploding");
        RewriteRule rule3 = new RewriteRule("explode","boom_of:boom*&booming");
        RewriteRule rule4 = new RewriteRule("walk","run_of:sprint*&sprinting");
        RewriteRule rule5 = new RewriteRule("walk","fly_with:fly*:flying");
        RewriteRule rule6 = new RewriteRule("exercise","lift_of:lift*&lifting");
        RewriteRule rule7 = new RewriteRule("garbage","lift_of:lift*&lifting");

        ArrayList<RewriteRule> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        rules.add(rule3);
        rules.add(rule4);
        rules.add(rule5);
        rules.add(rule6);
        rules.add(rule7);
        return rules;
    }
}
