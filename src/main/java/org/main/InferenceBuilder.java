package org.main;

import org.main.Objects.CoalescentMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InferenceBuilder {

    public InferenceBuilder(){}

    // Returns a list of analogies in the source of `mapping` that can be inferred to be able to map to the target
    public static ArrayList<String> getInferredAnalogies(CoalescentMapping mapping){
        ArrayList<String> sourceTopicAnalogies = AnalogyDataHolder.getAnalogiesFor(mapping.getSource());

        // Removes analogies already known to be mappable
        sourceTopicAnalogies = new ArrayList<>(sourceTopicAnalogies.stream().filter(source -> !mapping.getCoalescedMapping().containsKey(source)).toList());

        // Filters for analogies that can be inferred to map to the target (i.e. all subjects are known to be mappable)
        return new ArrayList<>(sourceTopicAnalogies.stream().filter(source -> canBeInferred(mapping.getSubjectMapping(),source)).toList());
    }

    // Returns whether an analogy can be inferred to map to the target (i.e. all subjects are known to be mappable)
    private static boolean canBeInferred(HashMap<String,String> subjectMapping, String analogy){
        // Only if all subjects can be mapped (exist in the subject mapping) is the analogy mappable
        return subjectMapping.keySet().containsAll(AnalogyManager.getUniqueSubjects(analogy));
    }

    // Creates a mapping from the inferred to be mappable source analogies and their corresponding target analogy (source analogies with the subjects substituted)
    public static HashMap<String,String> getInferredMappings(CoalescentMapping mapping){
        HashMap<String,String> inferredMappedAnalogies = new HashMap<>();

        for(String mappableSource: getInferredAnalogies(mapping)){
            inferredMappedAnalogies.put(mappableSource,getInferredTarget(mapping.getSubjectMapping(),mappableSource));
        }

        return inferredMappedAnalogies;
    }

    // Substitutes the subjects of an analogy with the subjects from a subject mapping (Should be provided from a coalescedMapping object)
    private static String getInferredTarget(HashMap<String,String> subjectMapping, String analogy){
        String[] separatedAnalogy = analogy.split("(?<=[ (])|(?=[ )])");
        StringBuilder inferredString = new StringBuilder();

        for(String token: separatedAnalogy){
            inferredString.append(subjectMapping.getOrDefault(token, token));
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
