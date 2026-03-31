package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;

import java.util.*;


public class AnalogyManager {
    //In essence , Open bracket, down the tree, closed bracket means we go back up the tree
    public static Predicate ConvertToOOP(String analogy) throws IllegalArgumentException {
        String[] words = (analogy.replace(")","").split("\\("));
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







    public static String convertToAbstractString(Predicate analogicalObject, Boolean prettified){
        HashMap<String,Integer> abstractionMapping = new HashMap<>();
        Stack<AnalogicalObject> stack = new Stack<>();

        stack.add(analogicalObject);

        StringBuilder stringBuilder = new StringBuilder();

        int mappingsCount = 0;
        AnalogicalObject next;
        while(!stack.empty()){
            next = stack.pop();

            if(next instanceof Predicate){
                stringBuilder.append(" (");
                stringBuilder.append(next.getName());

                stack.add(null);
                stack.addAll(((Predicate) next).getChildren().reversed());

            }else if(next == null){
                stringBuilder.append(")");
            }else{
                if(next.getName().contains("*")){
                    stringBuilder.append(" *");
                }else if(abstractionMapping.containsKey(next.getName())){
                    stringBuilder.append(" ");
                    stringBuilder.append(abstractionMapping.get(next.getName()));
                }else{
                    abstractionMapping.put(next.getName(),mappingsCount++);
                    stringBuilder.append(" ");
                    stringBuilder.append(mappingsCount - 1);
                }
            }

        }

        if(prettified){
            return prettify(stringBuilder.toString().trim());
        }else{
            return stringBuilder.toString().trim();
        }


    }

    private static String prettify(String input){
        StringBuilder prettifiedStringBuilder =new StringBuilder(input);

        int depth = 0;

        for(int curr = 1; curr < prettifiedStringBuilder.length(); curr++){
            if(prettifiedStringBuilder.charAt(curr) == '('){

                while(prettifiedStringBuilder.charAt(curr-1) == ' '){prettifiedStringBuilder.deleteCharAt(--curr);}

                prettifiedStringBuilder.insert(curr++,"\n");
                prettifiedStringBuilder.insert(curr,"\t".repeat(++depth));

                curr += depth;
            }else if(prettifiedStringBuilder.charAt(curr) == ')'){
                depth--;
            }
        }

        return prettifiedStringBuilder.toString();
    }
}

