import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.main.AnalogyManager;
import org.main.Interfaces.Predicate;
import org.main.rewriteRule;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.fail;

public class rewriteRulesTest {

    static Stream<Arguments> simpleRules () {
        return Stream.of(
                Arguments.of("(exercise *athlete muscle) ","(by exercising( perform *athlete exercise(of muscle)))",new rewriteRule("exercise"," perform_of:exercise*&exercising")),
                Arguments.of("(flex *athlete muscle)","(by flexing(display *athlete muscle(with effort)))", new rewriteRule("flex","display_with:effort&flexing")),
                Arguments.of("(dislike *rival colleague)","(by disliking(not(respect *rival colleague(as friend))))",new rewriteRule("dislike","!respect_as:friend&disliking")),
                Arguments.of("(lose_control_over *captain mutineer)","(by rejecting_control(not(respect mutineer *captain(as leader))))", new rewriteRule("lose_control_over","<!respect_as:leader&rejecting_control")),
                Arguments.of("(flunk *student exam)","(by flunking(reject teacher *student(for exam)))", new rewriteRule("flunk","^reject_for:teacher&flunking")),
                Arguments.of("(step_down_from *president presidency)","(by stepping_down(reject *president presidency(as way_of_life)))",new rewriteRule("step_down_from","reject_as:way_of_life&stepping_down"))
        );
    }

    @ParameterizedTest
    @MethodSource ("simpleRules")
    public void BasicTest(String input, String output, rewriteRule rule) throws IllegalAccessException {
        Predicate p = AnalogyManager.ConvertToOOP(input);
        assertEquals(output,rule.rewrite(p).toString());
    }

    static Stream<Arguments> inputValidation(){
        return Stream.of(
                //null input
                Arguments.of(null, new rewriteRule("dislike","!respect_as:friend&disliking")),
                //mistmatched predicates
                Arguments.of(AnalogyManager.ConvertToOOP("(step_down_from *president presidency)"), new rewriteRule("dislike","!respect_as:friend&disliking")),
                //too many children
                Arguments.of(AnalogyManager.ConvertToOOP("(step_down_from *president presidency presidential)"), new rewriteRule("dislike","!respect_as:friend&disliking"))
        );
    }

    @ParameterizedTest
    @MethodSource ("inputValidation")
    public void inputValidation(Predicate input, rewriteRule rule){
        assertThrows(IllegalArgumentException.class, () -> {
            rule.rewrite(input);
        });
    }

    static Stream<Arguments> nestedRules(){
        return Stream.of(
                Arguments.of("(flex (flex *athlete muscle)(dislike *rival colleague))","(by flexing(display(flex *athlete muscle)(dislike *rival colleague)(with effort)))", new rewriteRule("flex","display_with:effort&flexing"))
                );
    }

    @ParameterizedTest
    @MethodSource("nestedRules")
    public void nestedRules(String input, String output, rewriteRule rule){
        Predicate p = AnalogyManager.ConvertToOOP(input);
        assertEquals(output,rule.rewrite(p).toString());
    }

    static Stream<Arguments> badRuleConstructors(){
        return Stream.of(
                Arguments.of("exercise", "test"),
                Arguments.of("exercise", "test_ _ _  _ & &::::"),
                Arguments.of("exercise", "perform:exercise&exercising"),
                Arguments.of("exercise", "perform_of:exercise")
        );
    }

    @ParameterizedTest
    @MethodSource("badRuleConstructors")
    public void badConstructors(String originalPredicate, String rule){
        assertThrows(InvalidParameterException.class, () -> {
            new rewriteRule(originalPredicate, rule);
        });
    }

}
