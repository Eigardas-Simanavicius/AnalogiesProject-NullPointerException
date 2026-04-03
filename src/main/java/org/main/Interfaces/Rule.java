package org.main.Interfaces;

import java.util.ArrayList;

public interface Rule {
    String getOriginalPredicate();
    String getVerbPredicate();
    String getPrepositionPredicate();
    String getByArgument();
    String getNewArgument();
    Boolean newArgumentHasAsterisk();
    ArrayList<String> getModifiers();
    Boolean hasModifiers();

}
