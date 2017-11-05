package ramo.klevis.ui.comp;

import ramo.klevis.ml.Book;
import ramo.klevis.ui.RatingsTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class StarRaterRenderer implements TableCellRenderer {

    private final RatingsTableModel ratingsTableModel;
    private final StarRater starRater;

    public StarRaterRenderer(RatingsTableModel ratingsTableModel) {
        this.ratingsTableModel = ratingsTableModel;
        starRater = new StarRater(10);

    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        Book currentBook = ratingsTableModel.getMovie(row);
        starRater.setRating(currentBook.getRating().floatValue());
        return starRater;
    }
}
