package ramo.klevis.ml;

import java.io.Serializable;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class Book implements Serializable{
    private int id;
    private String title;
    private Double rating = 0d;

    public Book(int id, String title, Double rating) {
        this.id = id;
        this.title = title;
        this.rating = rating;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return id == book.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
