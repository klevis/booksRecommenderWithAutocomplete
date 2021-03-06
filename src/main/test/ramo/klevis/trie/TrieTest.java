package ramo.klevis.trie;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.List;
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

    @Test
    public void shouldReturnSuggestionListWhenSearchWithPrefix() {
        //http://ramok.tech
        Trie trie = new Trie();
        trie.insert("ted");
        trie.insert("ten");
        trie.insert("teddy");
        trie.insert("great");
        trie.insert("movie");
        trie.insert("moon");

        List<String> words=trie.findWordsStartingWith("te");

        assertThat(words.get(0),Is.is("ted"));
        assertThat(words.get(1),Is.is("ten"));
        assertThat(words.get(2),Is.is("teddy"));
        assertThat(words.size(),Is.is(3));
    }

    @Test
    public void shouldSearchEachWordOnSentenceButSuggestAllSentence() {
        Trie trie = new Trie();
        trie.insert("bla ted bla ");
        trie.insert("blu ten blu xxc");
        trie.insert("xxc teddy");
        trie.insert("ty great ga");
        trie.insert("aaaa movie zzzzz");
        trie.insert("zzz moon d g ");

        List<String> words=trie.findWordsStartingWith("te");
        assertThat(words.get(0),Is.is("bla ted bla "));
        assertThat(words.get(1),Is.is("blu ten blu xxc"));
        assertThat(words.get(2),Is.is("xxc teddy"));
        assertThat(words.size(),Is.is(3));
    }

    @Test
    public void shouldSearchEachWordOnSentenceAndNotSuggestDuplicates() {
        Trie trie = new Trie();
        trie.insert("bla ted bla ted");

        List<String> words=trie.findWordsStartingWith("te");
        assertThat(words.size(),Is.is(1));
        assertThat(words.get(0),Is.is("bla ted bla ted"));
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