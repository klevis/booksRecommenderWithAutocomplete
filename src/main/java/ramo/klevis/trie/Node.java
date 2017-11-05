package ramo.klevis.trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by klevis.ramo on 11/5/2017.
 */
public class Node<T> {

    private Character character;

    //keep order is easier to test
    private Map<Character, Node> children = new LinkedHashMap<>();

    Map<T, T> suggestionObject = new HashMap<T, T>();

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

    public List<T> getSuggestionObject() {
        return new ArrayList<>(suggestionObject.values());
    }

    public void addSuggestionObject(T word) {
        if (!suggestionObject.containsKey(word)) {
            suggestionObject.put(word, word);
        }
    }
}
