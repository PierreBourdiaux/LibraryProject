package org.PierreBourdiaux.exceptions;

public class NotAvailableException extends Exception{
    public NotAvailableException(String message){
        super(message);
    }


    public NotAvailableException(){
        super("Model.Book is not available");
    }
}
