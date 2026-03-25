package org.main;

import org.main.Interfaces.Predicate;

import java.util.Arrays;


public class AnalogyManager {

    public static Predicate ConvertToOOP(String analogy){
        char[] chars  = analogy.toCharArray();
        String[] words = analogy.replace(")","").split("\\(");
        String[] currWords;
        Predicate head ,curr , next ;
        int count = 1;

        head = new Clause();
        curr = head;
        currWords = words[count].split(" ");
        head.setName(findName(currWords));
        head.setSubject(currWords[currWords.length-1]);
        head.increaseEmbedded();
        count++;

        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == '(') {
                currWords = words[count].split(" ");
                next = new Clause();
                next.setName(findName(currWords));
                if(currWords.length > 1) {
                    next.setSubject(currWords[currWords.length - 1]);
                }
                curr.setEmbedded(next);
                curr = next;
                head.increaseEmbedded();
                count++;
            }
        }

        return head;
    }

    public static String ConvertToString(Predicate predicate){
        StringBuilder output = new StringBuilder();
        Predicate current = predicate;
        int counter = 0;
        while(current != null){
            output.append("(").append(current.getName());
            if(current.getSubject() != null){
                output.append(" ").append(current.getSubject());
            }
            counter++;
            current = current.getEmbedded();
        }
        for(int i = 0; i < counter; i++){
            output.append(")");
        }

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

}
