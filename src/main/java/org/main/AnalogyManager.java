package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;

import java.util.*;


public class AnalogyManager {
    //In essence , Open bracket, down the tree, closed bracket means we go back up the tree
    public static Predicate ConvertToOOP(String analogy) throws IllegalArgumentException {
        String[] words = (analogy.split("\\("));
        char[] brackets = analogy.replaceAll("[^()]", "").toCharArray();
        try {

            if (!bracketMatch(brackets)) {
                throw new IllegalArgumentException("Brackets Dont match");
            }
            String[] currWords;
            Predicate curr = null;
            int count = 1;

            for (char bracket : brackets) {
                if (bracket == '(') {
                    currWords = words[count].split(" ");
                    if (Objects.equals(currWords[0], ")")) {
                        throw new InputMismatchException("Null input");
                    }
                   curr = predicateBuilder(curr,currWords);
                    count++;
                } else {
                    if (bracket == ')') {
                        if (curr.getParent() != null) {
                            curr = curr.getParent();
                        }
                    }
                }

            }

            return curr;

        } catch (Exception e) {
            if (e instanceof InputMismatchException) {
                System.out.println("Null Input, Returned Clause will be null");
            }else{
                throw e;
            }
        }
        return null;
    }

    private static boolean bracketMatch(char[] brackets){
        Stack<Character> stack = new Stack<>();
        for (char bracket:brackets){
            if(bracket == '('){stack.push(bracket);
            } else if (bracket == ')' ) {
                if(stack.empty()){
                    return false;
                }
              stack.pop();
            }
        }
        return stack.isEmpty();
    }

    private static Predicate predicateBuilder(Predicate parent,String[] currWords){
        Predicate next = new Clause(currWords[0]);
        AnalogicalObject subject;
        for (int i = 1; i < currWords.length; i++) {
            subject = new Subject(currWords[i]);
            subject.setParent(next);
            next.addEmbedded(subject);
        }
        if (parent != null) {
            next.setParent(parent);
            parent.addEmbedded(next);
        }
        return next;
    }



    public static String ConvertToString(Predicate predicate, Boolean prettify){
        StringBuilder output = new StringBuilder();
        ArrayList<AnalogicalObject> clauseList = predicate.getAllChildren();
        int endParenthesesCounter = 0;
        int tabulationFixer = 0;
        for(int i = 0; i < clauseList.size(); i++){
            Predicate current = clauseList.get(i);
            output.append("(");
            if(current.getName() != null){
                output.append(current.getName());
            }
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
                        tabulationFixer = counter-1;
                    }
                    if(prettify) {
                        output.append("\n");
                        output.repeat("\t", i+1-tabulationFixer);
                        tabulationFixer = 0;
                    }
                }

            }
            endParenthesesCounter++;
        }

        output.repeat(")", endParenthesesCounter);

        return output.toString();
    }


    public static String convertToFlatAbstractString(Predicate predicate){
        HashMap<String,Integer> abstractionMapping = getAbstractionMappings(predicate);

        return convertToFlatAbstractStringHelper(predicate,abstractionMapping);
    }

    private static String convertToFlatAbstractStringHelper(Predicate predicate, HashMap<String, Integer> abstractionMapping){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("(");

        stringBuilder.append(predicate.getName().trim());

        if(predicate.getSubject() != null){
            stringBuilder.append(" ");
            if(predicate.getSubject().contains("*")){
                stringBuilder.append("*");
            }else{
                stringBuilder.append(abstractionMapping.get(predicate.getSubject().trim()));
            }
        }

        for(Predicate child : predicate.getChildren()){
            stringBuilder.append(" ");
            stringBuilder.append(convertToFlatAbstractStringHelper(child,abstractionMapping));
        }

        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    public static String convertToPrettifiedAbstractString(Predicate predicate){
        HashMap<String,Integer> abstractionMapping = getAbstractionMappings(predicate);

        return convertToPrettifiedAbstractStringHelper(predicate,abstractionMapping);
    }

    private static String convertToPrettifiedAbstractStringHelper(Predicate predicate, HashMap<String,Integer> abstractionMapping){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\t".repeat(predicate.depth()));

        stringBuilder.append("(");

        stringBuilder.append(predicate.getName().trim());

        if(predicate.getSubject() != null){
            stringBuilder.append(" ");
            if(predicate.getSubject().contains("*")){
                stringBuilder.append("*");
            }else{
                stringBuilder.append(abstractionMapping.get(predicate.getSubject().trim()));
            }

        }

        for(Predicate child : predicate.getChildren()){
            stringBuilder.append("\n");
            stringBuilder.append(convertToPrettifiedAbstractStringHelper(child,abstractionMapping));
        }

        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    private static HashMap<String,Integer> getAbstractionMappings(Predicate predicate){
        HashMap<String,Integer> abstractionMapping = new HashMap<>();
        int currentMapping = 0;

        if(predicate.getSubject() != null && !predicate.getSubject().contains("*") ){
            abstractionMapping.put(predicate.getSubject().trim(),currentMapping++);
        }

        for(Predicate child : predicate.getAllChildren()){
            if(child.getSubject() != null && !child.getSubject().contains("*")  && !abstractionMapping.containsKey(child.getSubject().trim())){
                abstractionMapping.put(child.getSubject().trim(), currentMapping++);
            }
        }

        return abstractionMapping;
    }
}

