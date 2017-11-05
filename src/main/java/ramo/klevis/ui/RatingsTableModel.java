package ramo.klevis.ui;

import ramo.klevis.ml.Book;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RatingsTableModel extends AbstractTableModel {

    private List<Book> bookList =new ArrayList<>();

    @Override
    public int getRowCount() {
        return bookList.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return false;
        }
        return true;
    }

    @Override
    public String getColumnName(int column) {
        String name = "??";
        switch (column) {
            case 0:
                name = "Title";
                break;
            case 1:
                name = "Rating";
                break;
        }
        return name;
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = bookList.get(rowIndex);
        Object value = null;
        switch (columnIndex) {
            case 0:
                value = book.getTitle();
                break;
            case 1:
                value = book.getRating();
                break;
        }
        return value;
    }

    public void resetAndAddNewMovies(List<Book> newBooks) {
        bookList.clear();
        bookList.addAll(newBooks);
    }

    public Book getMovie(int row) {
       return bookList.get(row);
    }

    public List<Book> getBookList() {
        return bookList;
    }
}