package org.PierreBourdiaux.Model;

import java.io.Serializable;
import java.time.LocalDate;


public class BorrowingHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private static int nextID = 1;
    private Book book;

    private Borrower borrower;
    private LocalDate dateBorrowed;
    private LocalDate dueDate;
    private boolean isReturned;

    public BorrowingHistory(Book book, Borrower borrower, LocalDate dueDate) {
        this.book = book;
        this.borrower = borrower;
        this.dateBorrowed = LocalDate.now();
        this.dueDate = dueDate;
        this.isReturned = false;
        id = nextID;
        nextID++;
    }

    public void setReturned(boolean isReturned) {
        this.isReturned = isReturned;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public Book getBook() {
        return book;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getBorrowDate() {
        return dateBorrowed;
    }

    @Override
    public String toString() {
        return borrower.getName() + " borrowed " + book.getTitle() + " on " + dateBorrowed + " and it is due on " + dueDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BorrowingHistory) {
            BorrowingHistory borrowingHistory = (BorrowingHistory) obj;
            return borrowingHistory.getBook().equals(book) && borrowingHistory.getBorrower().equals(borrower) && borrowingHistory.getDueDate().equals(dueDate);
        }
        return false;
    }



}
