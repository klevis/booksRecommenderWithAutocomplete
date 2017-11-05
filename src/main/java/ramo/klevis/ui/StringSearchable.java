package ramo.klevis.ui;

import ramo.klevis.ml.Book;
import ramo.klevis.ml.CollaborationFiltering;
import ramo.klevis.trie.Trie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the Searchable interface that searches a List of String objects.
 * <p>
 * This implementation searches only the beginning of the words, and is not be optimized
 * <p>
 * for very large Lists.
 *
 * @author G. Cope
 */

public class StringSearchable implements ramo.klevis.ui.comp.Searchable {


    private final Trie<Book> trie;
    private CollaborationFiltering collaborationFiltering;
    private List<Book> terms = new ArrayList<>();

    /**
     * Constructs a new object based upon the parameter terms.
     *
     * @param terms The inventory of terms to search.
     */

    public StringSearchable(List<Book> terms, CollaborationFiltering collaborationFiltering) {
        this.collaborationFiltering = collaborationFiltering;
        this.terms.addAll(terms);
        trie = new Trie();
        terms.stream().parallel().forEach(e -> trie.insert(e.toString().toLowerCase(), e));
    }

    public void setCollaborationFiltering(CollaborationFiltering collaborationFiltering) {
        this.collaborationFiltering = collaborationFiltering;
    }

    @Override
    public Collection<String> search(String value) {
        List<Book> wordsStartingWith = trie.findWordsStartingWith(value.toLowerCase());

        List<Book> books = collaborationFiltering.predictBooks(wordsStartingWith);

        return books.stream().map(e -> e.getTitle() + " - " + e.getRating().intValue()).collect(Collectors.toList());
    }


}
