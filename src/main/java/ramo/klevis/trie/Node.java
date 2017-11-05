package ramo.klevis.trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by klevis.ramo on 11/5/2017.
 */
public class Node {

    private Character character;

    //keep order is easier to test
    private Map<Character, Node> children = new LinkedHashMap<>();

    List<String> words = new ArrayList<>();

    public Node() {
    }


    public Node(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    public Map<Character, Node> getChildren() {
        return children;
    }

    public boolean isRoot() {
        return false;
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    public void addWord(String word) {
        words.add(word);
    }
}
