package ramo.klevis.trie;

import java.util.Map;

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
            Node found = insert(aChar, startFrom);
            startFrom = found;
        }
    }

    private Node insert(char currentChar, Node startFrom) {
        Node node = startFrom.getChildren().get(currentChar);
        if (node == null) {
            Node newNode = new Node(currentChar);
            startFrom.getChildren().put(currentChar, newNode);
            return newNode;
        }
        return node;
    }

}
