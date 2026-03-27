package org.main;

import org.main.Interfaces.Predicate;

import java.util.ArrayList;


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
                    curr.addChild(next);
                }
                curr = next;

                currWords = words[count].split(" ");

                curr.setName(findName(currWords));
                count++;
                if(currWords.length > 1){
                    curr.setSubject(currWords[currWords.length-1]);
                }

            } else if (brackets[i] == ')') {
                assert curr != null; // this should literally never happen
                curr = curr.getParent();
            }

        }
        return curr;
    }

    public static String ConvertToString(Predicate predicate, Boolean prettify){
        StringBuilder output = new StringBuilder();
        ArrayList<Predicate> clauseList = predicate.getAllChildren();
        for(int i = 0; i < clauseList.size(); i++){
            output.append("(").append(clauseList.get(i).getName());
            if(clauseList.get(i).getSubject() != null){
                output.append(" ").append(clauseList.get(i).getSubject());
                if(prettify && i != clauseList.size() -1) {
                    output.append("\n");
                    output.repeat("\t", i+1);
                }
            }
        }

        output.repeat(")", clauseList.size());

        return output.toString();
    }

    private static String findName(String[] str){

        if(str.length ==  1){
            return str[0];
        }
        String str2 = "";
        for (int i = 0; i < str.length - 1; i++) {
            str2 = str2.concat(" ");
           str2 = str2.concat(str[i]);
        }
        return str2;
    }

    public static String convertToFlatAbstractString(Predicate predicate){
        StringBuilder flatAbstractString = new StringBuilder();
        Predicate currentPredicate = predicate;

        if (predicate == null) return "";

        int subject = 0;
        do{
            flatAbstractString.append("(");
            flatAbstractString.append(currentPredicate.getName().trim());

            if(currentPredicate.getSubject() != null && !currentPredicate.getSubject().isBlank()){
                flatAbstractString.append(" ");
                flatAbstractString.append(subject++);
            }

            currentPredicate = currentPredicate.getChildren().getFirst();

            if(currentPredicate != null){
                flatAbstractString.append(" ");
            }

        }while(currentPredicate != null);

        flatAbstractString.append(")".repeat(predicate.getPredicatesEmbedded() + 1));

        return flatAbstractString.toString();
    }

    public static String convertToIndentedAbstractString(Predicate predicate){
        StringBuilder indentedAbstractString = new StringBuilder();
        Predicate currentPredicate = predicate;

        if (predicate == null) return "";

        int subject = 0;
        int greatestDepth = currentPredicate.getPredicatesEmbedded();
        do{
            indentedAbstractString.append("\t".repeat(greatestDepth - currentPredicate.getPredicatesEmbedded()));
            indentedAbstractString.append("(");
            indentedAbstractString.append(currentPredicate.getName());

            if(currentPredicate.getSubject() != null && !currentPredicate.getSubject().isBlank()){
                indentedAbstractString.append(" ");
                indentedAbstractString.append(subject++);
            }

            currentPredicate = currentPredicate.getChildren().getFirst();

            if(currentPredicate != null && currentPredicate.getChildren() != null){
                indentedAbstractString.append("\n");
            }else if (currentPredicate != null && currentPredicate.getChildren() == null){
                greatestDepth = 0;
                indentedAbstractString.append(" ");
            }

        }while(currentPredicate != null);

        indentedAbstractString.append(")".repeat(predicate.getPredicatesEmbedded() + 1));

        return indentedAbstractString.toString();
    }
}
