package org.main;

import org.main.Objects.CoalescentMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InferenceBuilder {

    public InferenceBuilder(){}
    public static ArrayList<String> getInferredAnalogies(CoalescentMapping mapping){
        ArrayList<String> sourceTopicAnalogies = AnalogyDataHolder.getAnalogiesFor(mapping.getSource());

        sourceTopicAnalogies = new ArrayList<>(sourceTopicAnalogies.stream().filter(source -> !mapping.getCoalescedMapping().containsKey(source)).toList());

        return new ArrayList<>(sourceTopicAnalogies.stream().filter(source -> canBeInferred(mapping.getSubjectMapping(),source)).toList());
    }

    private static boolean canBeInferred(HashMap<String,String> inferredMapping, String analogy){
        return inferredMapping.keySet().containsAll(AnalogyManager.getUniqueSubjects(analogy));
    }

    public static HashMap<String,String> getInferredMappings(CoalescentMapping mapping){
        HashMap<String,String> inferredMappedAnalogies = new HashMap<>();

        for(String mappableSource: getInferredAnalogies(mapping)){
            inferredMappedAnalogies.put(mappableSource,getInferredTarget(mapping.getSubjectMapping(),mappableSource));
        }

        return inferredMappedAnalogies;
    }

    private static String getInferredTarget(HashMap<String,String> inferredMapping, String analogy){
        String[] separatedAnalogy = analogy.split("(?<=[ (])|(?=[ )])");
        StringBuilder inferredString = new StringBuilder();

        for(String token: separatedAnalogy){
            inferredString.append(inferredMapping.getOrDefault(token, token));
        }

        return inferredString.toString();
    }

    public CoalescentMapping updateCompositeWithInferences(CoalescentMapping initialMapping){
        HashMap<String, String> inferences = getInferredMappings(initialMapping);
        ArrayList<String> analogies = initialMapping.getAnalogies();
        HashMap<String, String> mappedAnalogies = initialMapping.getCoalescedMapping();

        List<String> sources = inferences.keySet().stream().toList();
        for(String source : sources){
            mappedAnalogies.put(source, inferences.get(source));
            analogies.add(source);
            analogies.add(inferences.get(source));
        }
        initialMapping.setCoalescedMapping(mappedAnalogies);
        initialMapping.setAnalogies(analogies);
        return initialMapping;
    }
}
