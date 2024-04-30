package org.PierreBourdiaux.Utils;

import org.PierreBourdiaux.Model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookTitleFilter implements BookFilter{
    @Override
    public List<Book> filter(List<Book> books, String keyword) {
        List<Book> match = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                match.add(book);
            }
        }

        return match;
    }
}
