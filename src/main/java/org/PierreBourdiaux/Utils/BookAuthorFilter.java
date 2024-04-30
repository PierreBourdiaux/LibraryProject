package org.PierreBourdiaux.Utils;


import org.PierreBourdiaux.Model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAuthorFilter implements BookFilter{
    @Override
    public List<Book> filter(List<Book> books, String keyword){
        List<Book> match = new ArrayList<>();
        for(Book b : books){
            if(b.getAuthor().toLowerCase().contains(keyword.toLowerCase())){
                match.add(b);
            }
        }

        return match;
    }
}
