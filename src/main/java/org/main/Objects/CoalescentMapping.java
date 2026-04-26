package org.main.Objects;

import org.main.CompositeBuilder;
import org.main.MappingManager;

import java.util.ArrayList;
import java.util.HashMap;

public class CoalescentMapping {

    private String source;
    private String target;
    private final int richness;
    ArrayList<String> compositeAnalogy;
    HashMap<String,String> mapping;

    public CoalescentMapping(String source,String target){
        this.source = source;
        this.target = target;

        compositeAnalogy = new CompositeBuilder().buildCompositeAnalogy(source,target);
        richness = MappingManager.getMappingRichness(compositeAnalogy);
    }

    public String getSource(){return source;}
    public String getTarget(){return target;}
    public ArrayList<String> getMapping(){return compositeAnalogy;}
    public int getRichness() {
        return richness;
    }
}
