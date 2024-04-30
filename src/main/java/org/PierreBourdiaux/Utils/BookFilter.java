package org.PierreBourdiaux.Utils;

import org.PierreBourdiaux.Model.Book;
import org.PierreBourdiaux.exceptions.BookNotFoundExeption;

import java.util.List;

public interface BookFilter {
    public List<Book> filter(List<Book> books, String keyword);
}
