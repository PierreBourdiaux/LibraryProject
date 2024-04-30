package org.PierreBourdiaux.exceptions;

public class BorrowerNotFoundExeption extends Exception{

    public BorrowerNotFoundExeption(String message){
        super(message);
    }

    public BorrowerNotFoundExeption(){
        super("Model.Borrower not found");
    }
}
