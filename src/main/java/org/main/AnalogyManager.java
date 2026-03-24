package org.main;

import org.main.Interfaces.Predicate;

import java.util.ArrayList;

public class AnalogyManager {

    // given
    public Predicate ConvertToOOP(String analogy){
        char[] chars  = analogy.toCharArray();
        ArrayList<String> words = new ArrayList<>();
        Predicate head = null,curr = null,next = null;
        for (char aChar : chars) {
            if (aChar == '(') {
                next = new Predicate();
                if(curr != null) {
                    curr.setName(getName(words));
                    if (words.size() > 1) {
                        curr.setSubject(words.getLast());
                    }
                    curr.setEmbedded(next);
                }
                curr = next;
                if (head == null) {
                    head = curr;
                }
                head.increaseEmbedded();
            }
        }
        return head;
    }

    private String getName(ArrayList<String> words){
        if(words.size() == 1){
            return words.getFirst();
        }
        String str = "";
        for (int i = 0; i < words.size() - 1; i++) {
           str = str.concat(words.get(i));
        }
        return str;
    }
}
