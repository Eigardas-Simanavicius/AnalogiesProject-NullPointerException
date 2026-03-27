package org.main;

import org.main.Interfaces.Predicate;

import java.util.ArrayList;
import java.util.HashMap;


public class AnalogyManager {
    //In essence , Open bracket, down the tree, closed bracket means we go back up the tree
    public static Predicate ConvertToOOP(String analogy){
        String[] words = (analogy.split("\\("));
        char[] brackets = analogy.replaceAll("[^()]","").toCharArray();
        String[] currWords;
        Predicate curr = null, next ;
        int count = 1;
        for(int i = 0; i < brackets.length; i++){
            if(brackets[i] == '('){
                next = new Clause();
                if(curr != null){
                    next.setParent(curr);
                    curr.addEmbedded(next);
                }
                curr = next;

                currWords = words[count].split(" ");

                curr.setName(findName(currWords));
                count++;
                if(currWords.length > 1){
                    curr.setSubject(currWords[currWords.length-1].replace("\\)",""));
                }

            } else {
                if (brackets[i] == ')') {
                    assert curr != null;
                    if (curr.getParent() != null) {
                        curr = curr.getParent();
                    }
                }
            }

        }

        return curr;
    }

    public static String ConvertToString(Predicate predicate, Boolean prettify){
        StringBuilder output = new StringBuilder();
        ArrayList<Predicate> clauseList = predicate.getAllChildren();
        int endParenthesesCounter = 0;
        for(int i = 0; i < clauseList.size(); i++){
            Predicate current = clauseList.get(i);
            output.append("(").append(current.getName());
            if(current.getSubject() != null){
                output.append(" ").append(current.getSubject());

                //Assuming that parantheses only need to be closed if the predicate has a subject
                if(i != clauseList.size() -1){
                    Predicate next = clauseList.get(i+1);
                    if(next.getParent() != current){
                        int counter = 1;
                        Predicate possibleParent = current.getParent();
                        while(next.getParent() != possibleParent){
                            possibleParent = possibleParent.getParent();
                            counter++;
                        }
                        output.repeat(")",counter);
                        endParenthesesCounter -= counter;
                    }
                    if(prettify) {
                        output.append("\n");
                        output.repeat("\t", i+1);
                    }
                }

            }
            endParenthesesCounter++;
        }

        output.repeat(")", endParenthesesCounter);

        return output.toString();
    }

    private static String findName(String[] str){

        if(str.length ==  1){
            return str[0].concat(" ");
        }
        String str2 = "";
        for (int i = 0; i < str.length - 1; i++) {
           str2 = str2.concat(str[i]);
           str2 = str2.concat(" ");
        }
        return str2;
    }

    public static String convertToFlatAbstractString(Predicate predicate){
        HashMap<String,Integer> abstractionMapping = getAbstractionMappings(predicate);

        return convertToFlatAbstractStringHelper(predicate,abstractionMapping);
    }

    private static String convertToFlatAbstractStringHelper(Predicate predicate, HashMap<String, Integer> abstractionMapping){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("(");

        stringBuilder.append(predicate.getName());

        if(predicate.getSubject() != null){
            stringBuilder.append(" ");
            stringBuilder.append(abstractionMapping.get(predicate.getSubject()));
        }

        for(Predicate child : predicate.getChildren()){
            stringBuilder.append(" ");
            stringBuilder.append(convertToFlatAbstractStringHelper(child,abstractionMapping));
        }

        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    private static HashMap<String,Integer> getAbstractionMappings(Predicate predicate){
        HashMap<String,Integer> abstractionMapping = new HashMap<>();
        int currentMapping = 0;

        if(predicate.getSubject() != null){
            abstractionMapping.put(predicate.getSubject(),currentMapping++);
        }

        for(Predicate child : predicate.getAllChildren()){
            if(child.getSubject() != null && !abstractionMapping.containsKey(child.getSubject())){
                abstractionMapping.put(child.getSubject(), currentMapping++);
            }
        }

        return abstractionMapping;
    }
}
