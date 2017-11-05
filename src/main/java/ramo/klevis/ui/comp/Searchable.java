package ramo.klevis.ui.comp;

import java.util.Collection;

/**
 * Created by klevis.ramo on 11/5/2017.
 */
public interface Searchable<T, T1> {
    Collection<String> search(String value);
}
