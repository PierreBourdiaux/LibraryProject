package org.PierreBourdiaux.Utils;

import org.PierreBourdiaux.Model.Book;

import java.util.List;

public class BookYearFilter implements BookFilter{
    @Override
    public List<Book> filter(List<Book> books, String keyword) {
        List<Book> match = new java.util.ArrayList<>();
        for(Book b : books){
            if(String.valueOf(b.getPublicationYear()).toLowerCase().contains(keyword.toLowerCase())){
                match.add(b);
            }
        }

        return match;
    }
}
