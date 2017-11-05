package ramo.klevis.trie;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertThat;


/**
 * Created by klevis.ramo on 11/5/2017.
 */
public class TrieTest {

    @Test
    public void shouldInsertCharacterForFirstTime() throws Exception {

        Trie trie = new Trie();
        trie.insert("ted");
        assertThat(print(trie.getRoot()), Is.is("ROOT t(e(d ))"));
    }

    @Test
    public void shouldReUseExistingCharactersWhenInserting() throws Exception {

        Trie trie = new Trie();
        trie.insert("ted");
        trie.insert("ten");
        assertThat(print(trie.getRoot()), Is.is("ROOT t(e(d n ))"));
    }

    @Test
    public void shouldCreateNewChildrenWhenNoOneExists() throws Exception {

        Trie trie = new Trie();
        trie.insert("ted");
        trie.insert("ten");
        trie.insert("great");
        assertThat(print(trie.getRoot()), Is.is("ROOT t(e(d n ))g(r(e(a(t ))))"));
    }

    private String print(Root root) {
        StringBuilder stringBuilder = new StringBuilder();
        print(root, stringBuilder);
        return stringBuilder.toString();
    }

    private void print(Node root, StringBuilder stringBuilder) {
        Map<Character, Node> children = root.getChildren();

        stringBuilder.append(root.getCharacter() == null ? "ROOT " : !root.getChildren().isEmpty() ?
                root.getCharacter() + "(" : root.getCharacter() + " ");
        for (Node node : children.values()) {
            print(node, stringBuilder);
        }
        if (!root.getChildren().isEmpty()&&!root.isRoot())
            stringBuilder.append(")");
    }
}