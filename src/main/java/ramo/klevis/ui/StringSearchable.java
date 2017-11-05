package ramo.klevis.ui;

import ramo.klevis.trie.Trie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of the Searchable interface that searches a List of String objects.
 * <p>
 * This implementation searches only the beginning of the words, and is not be optimized
 * <p>
 * for very large Lists.
 *
 * @author G. Cope
 */

public class StringSearchable implements ramo.klevis.ui.comp.Searchable<String, String> {


    private List<String> terms = new ArrayList<String>();
    private final Trie trie;


    /**
     * Constructs a new object based upon the parameter terms.
     *
     * @param terms The inventory of terms to search.
     */

    public StringSearchable(List<String> terms) {

        this.terms.addAll(terms);
        trie = new Trie();
        terms.stream().parallel().forEach(e -> trie.insert(e.toLowerCase()));

    }


    @Override
    public Collection<String> search(String value) {
        return trie.findWordsStartingWith(value.toLowerCase());
    }


}
