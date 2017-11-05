package ramo.klevis.trie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klevis.ramo on 11/5/2017.
 */
public class Trie<T> {

    private Root root = new Root();

    public Root getRoot() {
        return root;
    }

    /**
     * Suggestion word is default same as the word.
     *
     * @param sentence that will be search char by char
     */
    public void insert(String sentence) {
        insert(sentence, (T) sentence);
    }

    /**
     * @param sentence             that will be search char by char
     * @param suggestionObject what is suggested to user
     */
    public void insert(String sentence, T suggestionObject) {
        String[] split = sentence.split(" ");
        for (String word : split) {
            char[] chars = word.toCharArray();
            Node startFrom = root;
            for (char aChar : chars) {
                Node found = insert(aChar, startFrom);
                startFrom = found;
                startFrom.addSuggestionObject(suggestionObject);
            }
        }
    }

    private Node insert(char currentChar, Node<T> startFrom) {
        Node node = startFrom.getChildren().get(currentChar);
        if (node == null) {
            Node newNode = new Node(currentChar);
            startFrom.getChildren().put(currentChar, newNode);
            return newNode;
        }
        return node;
    }

    public List<T> findWordsStartingWith(String prefix) {

        char[] chars = prefix.toCharArray();
        Node<T> current = root;
        for (char currentChar : chars) {
            Node node = current.getChildren().get(currentChar);
            if (node == null) {
                return new ArrayList<>();
            }
            current = node;
        }
        return current.getSuggestionObject();
    }
}
