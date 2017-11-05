package ramo.klevis.trie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klevis.ramo on 11/5/2017.
 */
public class Trie {

    private Root root = new Root();

    public Root getRoot() {
        return root;
    }

    public void insert(String word) {

        char[] chars = word.toCharArray();
        Node startFrom = root;
        for (char aChar : chars) {
            Node found = insert(aChar, startFrom, word);
            startFrom = found;
            startFrom.addWord(word);
        }
    }

    private Node insert(char currentChar, Node startFrom, String word) {
        Node node = startFrom.getChildren().get(currentChar);
        if (node == null) {
            Node newNode = new Node(currentChar);
            startFrom.getChildren().put(currentChar, newNode);
            return newNode;
        }
        return node;
    }

    public List<String> findWordsStartingWith(String prefix) {

        char[] chars = prefix.toCharArray();
        Node current=root;
        for (char currentChar : chars) {
            Node node = current.getChildren().get(currentChar);
            if (node == null) {
                return new ArrayList<>();
            }
            current = node;
        }
        return current.getWords();
    }
}
