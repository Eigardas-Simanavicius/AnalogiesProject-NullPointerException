package org.main;

import org.main.Objects.CoalescentMapping;

import java.util.ArrayList;
import java.util.HashMap;

public class InferenceBuilder {

    public InferenceBuilder(){}
    public static ArrayList<String> getInferredAnalogies(CoalescentMapping mapping){
        ArrayList<String> targetTopicAnalogies = AnalogyDataHolder.getAnalogiesFor(mapping.getTarget());

        targetTopicAnalogies = new ArrayList<>(targetTopicAnalogies.stream().filter(x -> !mapping.getCoalescedMapping().containsValue(x)).toList());

        return new ArrayList<>(targetTopicAnalogies.stream().filter(x -> canBeInferred(mapping.getInferredMapping(),x)).toList());
    }

    private static boolean canBeInferred(HashMap<String,String> inferredMapping, String analogy){
        return inferredMapping.values().containsAll(AnalogyManager.getUniqueSubjects(analogy));
    }
}
