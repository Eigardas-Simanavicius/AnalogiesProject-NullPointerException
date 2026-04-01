import org.junit.Test;
import org.main.AnalogyManager;
import org.main.Clause;
import org.main.MappingManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CanMapTest {

    @Test
    public void simpleMapping(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1)");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2)");

        assertEquals(true, MappingManager.canMap(c1,c2));
    }

    @Test
    public void simpleStructureMismatch(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1)");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2)");

        assertEquals(false, MappingManager.canMap(c1,c2));
    }

    @Test
    public void simpleCommutativity(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1)");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2)");

        assertEquals(true, MappingManager.canMap(c1,c2));
        assertEquals(true, MappingManager.canMap(c2,c1));
    }

    @Test
    public void differentLengthClauses1(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1(predicate2 subject3))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2)");

        assertEquals(false, MappingManager.canMap(c1,c2));
    }

    @Test
    public void differentLengthClauses2(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1)");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2(predicate2 subject3))");

        assertEquals(false, MappingManager.canMap(c1,c2));
    }

    @Test
    public void differentPredicates(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate2 subject1)");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2)");

        assertEquals(false, MappingManager.canMap(c1,c2));
    }

    @Test
    public void differentPredicatesCommutative(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1)");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate4 subject2)");

        assertEquals(false, MappingManager.canMap(c1,c2));
        assertEquals(false, MappingManager.canMap(c2,c1));
    }

    @Test
    public void simpleNestedPredicates(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1(predicate2 subject4(p3 s5)))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2(predicate2 subject3(p3 s6)))");

        assertEquals(true, MappingManager.canMap(c1,c2));
    }

    @Test
    public void simpleNestedPredicateCommutativity(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1(predicate2 subject4(p3 s5)))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2(predicate2 subject3(p3 s6)))");

        assertEquals(true, MappingManager.canMap(c1,c2));
        assertEquals(true, MappingManager.canMap(c2,c1));
    }

    @Test
    public void simpleNestedPredicatesNonMatching(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1(predicate2 subject4(p7 s5)))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2(predicate2 subject3(p3 s6)))");

        assertEquals(false, MappingManager.canMap(c1,c2));
    }

    @Test
    public void simpleNestedPredicatesNonMatchingCommutative(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1(predicate2 subject4(p7 s5)))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2(predicate4 subject3(p3 s6)))");

        assertEquals(false, MappingManager.canMap(c1,c2));
        assertEquals(false, MappingManager.canMap(c2,c1));
    }

    @Test
    public void noSubjectMatching(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 (predicate2 subject4(p3 s5)))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 (predicate2 subject3(p3 s6)))");

        assertEquals(true, MappingManager.canMap(c1,c2));
    }

    @Test
    public void complexNestingMatching(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1(predicate2 (p3 s5)(p4 s7))(p9 s10))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2(predicate2 (p3 s6)(p4 s8))(p9 s11))");

        assertEquals(true, MappingManager.canMap(c1,c2));
    }

    @Test
    public void complexNestingNonMatching(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1(predicate2 (p4 s5)(p4 s7))(p9 s10))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2(predicate2 (p3 s6)(p4 s8))(p9 s11))");

        assertEquals(false, MappingManager.canMap(c1,c2));
    }

    @Test
    public void complexNestingCommutativity(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1(predicate2 (p3 s5)(p4 s7))(p9 s10))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2(predicate2 (p3 s6)(p4 s8))(p9 s11))");

        assertEquals(true, MappingManager.canMap(c1,c2));
        assertEquals(true, MappingManager.canMap(c2,c1));

        Clause c3 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1(predicate2 (p3 s5)(p4 s7))(p9 s10))");
        Clause c4 = (Clause)AnalogyManager.ConvertToOOP("(predicate7 subject2(predicate2 (p3 s6)(p4 s8))(p9 s11))");

        assertEquals(false, MappingManager.canMap(c3,c4));
        assertEquals(false, MappingManager.canMap(c4,c3));
    }

    @Test
    public void multipleSubjectsMatching(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1 subject1-2(predicate2 (p3 s5)(p4 s7))(p9 s10))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2 subject2-2(predicate2 (p3 s6)(p4 s8))(p9 s11))");

        assertEquals(true, MappingManager.canMap(c1,c2));
    }

    @Test
    public void multipleSubjectsNonMatching(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1 subject1-2 subject1-3(predicate2 (p3 s5)(p4 s7))(p9 s10))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2 subject2-2(predicate2 (p3 s6)(p4 s8))(p9 s11))");

        assertEquals(false, MappingManager.canMap(c1,c2));
    }

    @Test
    public void multipleSubjectsMatchingCommutative(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1 subject1-2(predicate2 (p3 s5 s5-2)(p4 s7))(p9 s10))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2 subject2-2(predicate2 (p3 s6 s6-2)(p4 s8))(p9 s11))");

        assertEquals(true, MappingManager.canMap(c1,c2));
        assertEquals(true, MappingManager.canMap(c2,c1));
    }

    @Test
    public void multipleSubjectsNonMatchingCommutative(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1 subject1-2 subject1-3(predicate2 (p3 s5)(p4 s7))(p9 s10))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject2 subject2-2(predicate2 (p3 s6 s6-2)(p4 s8))(p9 s11))");

        assertEquals(false, MappingManager.canMap(c1,c2));
        assertEquals(false, MappingManager.canMap(c2,c1));

    }

    @Test
    public void asteriskSubjectMatching(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 *subject1(predicate2 subject4(p3 s5)))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 *subject2(predicate2 subject3(p3 s6)))");

        assertTrue(MappingManager.canMap(c1,c2));
    }

    @Test
    public void asteriskSubjectNonMatching(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1(predicate2 subject4(p3 s5)))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 *subject2(predicate2 subject3(p3 s6)))");

        assertEquals(false, MappingManager.canMap(c1,c2));
    }

    @Test
    public void nullParameterTest(){
        Clause c1 = (Clause)AnalogyManager.ConvertToOOP("(predicate1 subject1(predicate2 (p3 s5)(p4 s7))(p9 s10))");

        assertEquals(false, MappingManager.canMap(c1,null));
    }


}
