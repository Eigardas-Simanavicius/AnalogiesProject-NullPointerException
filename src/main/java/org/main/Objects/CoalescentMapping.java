package org.main.Objects;

import org.main.CompositeBuilder;
import org.main.MappingManager;

import java.util.ArrayList;
import java.util.HashMap;

public class CoalescentMapping {

    private String source;
    private String target;
    private final double richness;
    private double improvedRichness;
    private ArrayList<String> analogies;
    private final ArrayList<String> infferedAnalogies = new ArrayList<>();
    private HashMap<String,String> coalescedMapping;
    private HashMap<String, String> subjectMapping;

    public CoalescentMapping(String source,String target){
        this.source = source;
        this.target = target;

        analogies = new CompositeBuilder().buildCompositeAnalogy(source,target);
        richness = MappingManager.getMappingRichness(analogies);
        improvedRichness = richness;
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

    // Used to lazily instantiate coalesced mapping (From analogies in the source to analogies in the target)
    private void createCoalescedMapping(){
        coalescedMapping = new HashMap<>();
        for(int i = 1; i < analogies.size(); i += 2){
            coalescedMapping.put(analogies.get(i-1),analogies.get(i));
        }
    }

    public HashMap<String,String> getSubjectMapping(){
        if(subjectMapping == null){
            createSubjectMapping();
        }
        return subjectMapping;
    }

    // Used lazily instantiates inferred mapping (from subjects in the source to subjects in the target) using the coalesced mappings
    private void createSubjectMapping(){
        subjectMapping = new HashMap<>();

        if(coalescedMapping == null) createCoalescedMapping();

        coalescedMapping.forEach(this::createSubjectMappingHelper);
        improvedRichness = richness + MappingManager.getMappingRichness(infferedAnalogies);
    }

    private void createSubjectMappingHelper(String sourceAnalogy, String targetAnalogy){
        HashMap<String,String> map = MappingManager.flatStringMapping(sourceAnalogy,targetAnalogy);
        if(map != null){
            subjectMapping.putAll(map);
            infferedAnalogies.add(sourceAnalogy);
            infferedAnalogies.add(targetAnalogy);
            subjectMapping.putAll(map);
        }
    }

    public double getImprovedRichness(){return improvedRichness;}
    public void setAnalogies(ArrayList<String> analogies){
        this.analogies = analogies;
    }

    public void setCoalescedMapping(HashMap<String, String> mapping){
        this.coalescedMapping = mapping;
    }
}