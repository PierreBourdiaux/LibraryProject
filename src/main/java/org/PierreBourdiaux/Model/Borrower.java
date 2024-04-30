package org.PierreBourdiaux.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Borrower implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String Email;
    private String phoneNumber;

    private int membershipID;

    private List<BorrowingHistory> borrowingHistory;
    private static int nextID = 1;

    public Borrower(String name, String Email, String phoneNumber){
        this.name = name;
        this.Email = Email;
        this.phoneNumber = phoneNumber;
        this.membershipID = nextID;
        borrowingHistory = new ArrayList<>();

        nextID++;

    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return Email;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public int getMembershipID(){
        return membershipID;
    }

    public List<BorrowingHistory> getBorrowingHistory(){
        return borrowingHistory;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String Email){
        this.Email = Email;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public static void setNextID(int nextID){
        Borrower.nextID = nextID;
    }

    public void addBorrowingHistory(BorrowingHistory borrowingHistory){
        this.borrowingHistory.add(borrowingHistory);
    }

    public List<Book> getBookToBeReturned(){
        List<Book> booksToBeReturned = new ArrayList<>();
        for(BorrowingHistory borrowingHistory : borrowingHistory){
            if(borrowingHistory.isReturned() == false){
                booksToBeReturned.add(borrowingHistory.getBook());
            }
        }
        return booksToBeReturned;
    }

    @Override
    public boolean equals(Object other){
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Borrower borrower = (Borrower) other;
        return membershipID == borrower.membershipID;
    }

    @Override
    public String toString() {
        return  "name:'" + name + '\'' +
                ", Email:'" + Email + '\'' +
                ", phoneNumber:'" + phoneNumber + '\'' +
                ", membershipID:" + membershipID;
    }




}
