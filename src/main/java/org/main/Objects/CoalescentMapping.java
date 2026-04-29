package org.main.Objects;

import org.main.AnalogyManager;
import org.main.CompositeBuilder;
import org.main.MappingManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CoalescentMapping {

    private String source;
    private String target;
    private final double richness;
    private ArrayList<String> analogies;
    private HashMap<String,String> coalescedMapping;
    private HashMap<String, HashSet<String>> inferredMapping;

    public CoalescentMapping(String source,String target){
        this.source = source;
        this.target = target;

        analogies = new CompositeBuilder().buildCompositeAnalogy(source,target);
        richness = MappingManager.getMappingRichness(analogies);
    }

    public String getSource(){return source;}
    public String getTarget(){return target;}
    public ArrayList<String> getAnalogies(){return analogies;}
    public double getRichness() {
        return richness;
    }

    public HashMap<String,String> getCoalescedMapping(){
        if(coalescedMapping == null){
            createCoalescedMapping();
        }

        return coalescedMapping;
    }

    private void createCoalescedMapping(){
        coalescedMapping = new HashMap<>();
        for(int i = 1; i < analogies.size(); i += 2){
            coalescedMapping.put(analogies.get(i-1),analogies.get(i));
        }
    }

    public HashMap<String,HashSet<String>> getInferredMapping(){
        if(inferredMapping == null){
            createInferredMapping();
        }
        return inferredMapping;
    }

    private void createInferredMapping(){
        inferredMapping = new HashMap<>();

        if(coalescedMapping == null) createCoalescedMapping();

        coalescedMapping.forEach(this::createInferredMappingHelper);
    }

    private void createInferredMappingHelper(String sourceAnalogy, String targetAnalogy){
        HashMap<String,String> map = MappingManager.flatStringMapping(sourceAnalogy,targetAnalogy);
        if(map != null){
            map.forEach(
                    (String source, String target) -> {
                        if(!inferredMapping.containsKey(source)){
                            inferredMapping.put(source,new HashSet<>());
                        }
                        inferredMapping.get(source).add(target);
                }
            );
        }
    }
}
