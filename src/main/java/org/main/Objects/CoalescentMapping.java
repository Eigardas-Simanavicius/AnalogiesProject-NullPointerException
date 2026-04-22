package org.main.Objects;

import org.main.CompositeBuilder;
import org.main.MappingManager;

import java.util.ArrayList;
import java.util.HashMap;

public class CoalescentMapping {

    String source;
    String traget;
    int richness;
    ArrayList<String> compositeAnalogy;
    HashMap<String,String> mapping;

    public CoalescentMapping(String source,String target){
        this.source = source;
        this.traget = target;
        compositeAnalogy = CompositeBuilder.buildCompositeAnalogy(source,target);
        richness = MappingManager.getMappingRichness(compositeAnalogy);
    }


}
