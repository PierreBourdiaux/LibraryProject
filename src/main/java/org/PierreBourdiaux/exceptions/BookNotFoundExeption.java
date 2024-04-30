package org.PierreBourdiaux.exceptions;

public class BookNotFoundExeption extends Exception{
    public BookNotFoundExeption(String message){
        super(message);
    }
    public BookNotFoundExeption(){
        super ("Model.Book not found");
    }
}
