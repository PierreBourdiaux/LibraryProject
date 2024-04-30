
package org.PierreBourdiaux;

import org.PierreBourdiaux.Model.Book;
import org.PierreBourdiaux.Model.Borrower;
import org.PierreBourdiaux.Model.BorrowingHistory;
import org.PierreBourdiaux.Model.Genre;
import org.PierreBourdiaux.exceptions.BookNotFoundExeption;
import org.PierreBourdiaux.exceptions.BorrowerNotFoundExeption;
import org.PierreBourdiaux.exceptions.NotAvailableException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Library implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Book> books;
    private List<Borrower> borrowers = new ArrayList<>();


    public Library(List<Book> books) {
        this.books = books;
    }

    public Library() {
        books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Book> searchBooks_By_One_Keyword(String criteria) {
        List<Book> matchingBooks = new ArrayList<>();

        for (Book book : books) {
            if (criteria.isEmpty() ||
                    book.getTitle().toLowerCase().contains(criteria.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(criteria.toLowerCase()) ||
                    String.valueOf(book.getPublicationYear()).contains(criteria)) {
                matchingBooks.add(book);
            }
        }

        return matchingBooks;
    }

    public List<Book> searchBooks_By_Attribute(String title, String author, Genre genre, int publicationYear) {
        List<Book> matchingBooks = new ArrayList<>();

        for (Book book : books) {
            if ((title.isEmpty() || book.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                    (author.isEmpty() || book.getAuthor().toLowerCase().contains(author.toLowerCase())) &&
                    (book.getGenre() == genre) &&
                    (publicationYear == 0 || book.getPublicationYear() == publicationYear)) {
                matchingBooks.add(book);
            }
        }

        return matchingBooks;
    }

    public void addBorrower(Borrower borrower) {
        borrowers.add(borrower);
    }

    public void removeBorrower(Borrower borrower) {
        borrowers.remove(borrower);
    }


    public List<Borrower> getBorrowers() {
        return borrowers;
    }

    public List<Borrower> searchBorrowers_By_One_Keyword(String criteria) {
        List<Borrower> matchingBorrowers = new ArrayList<>();

        for (Borrower borrower : borrowers) {
            if (criteria.isEmpty() ||
                    borrower.getName().toLowerCase().contains(criteria.toLowerCase()) ||
                    borrower.getEmail().toLowerCase().contains(criteria.toLowerCase()) ||
                    borrower.getPhoneNumber().toLowerCase().contains(criteria.toLowerCase()) ||
                    String.valueOf(borrower.getMembershipID()).contains(criteria)) {
                matchingBorrowers.add(borrower);
            }
        }

        return matchingBorrowers;
    }

    public List<Borrower> searchBorrowers_By_Attribute(String name, String email, String phoneNumber, int membershipID) {
        List<Borrower> matchingBorrowers = new ArrayList<>();



        for (Borrower borrower : borrowers) {
            if ((name.isEmpty() || borrower.getName().toLowerCase().contains(name.toLowerCase())) &&
                    (email.isEmpty() || borrower.getEmail().toLowerCase().contains(email.toLowerCase())) &&
                    (phoneNumber.isEmpty() || borrower.getPhoneNumber().toLowerCase().contains(phoneNumber.toLowerCase())) &&
                    (membershipID == 0 || borrower.getMembershipID() == membershipID)) {
                matchingBorrowers.add(borrower);
            }
        }

        return matchingBorrowers;
    }

    public List<Borrower> searchBorrowers_by_name(String name) throws BorrowerNotFoundExeption {
        List<Borrower> matchingBorrowers = new ArrayList<>();

        for (Borrower borrower : borrowers) {
            if (name.isEmpty() || borrower.getName().toLowerCase().contains(name.toLowerCase())) {
                matchingBorrowers.add(borrower);
            }
        }
        if (matchingBorrowers.isEmpty()) {
            throw new BorrowerNotFoundExeption("Model.Borrower not found");
        }
        return matchingBorrowers;
    }

    public List<Borrower> searchBorrowers_by_membershipID(int membershipID) throws BorrowerNotFoundExeption{
        List<Borrower> matchingBorrowers = new ArrayList<>();

        for (Borrower borrower : borrowers) {
            if (membershipID == 0 || borrower.getMembershipID() == membershipID) {
                matchingBorrowers.add(borrower);
            }
        }
        if (matchingBorrowers.isEmpty()) {
            throw new BorrowerNotFoundExeption("Model.Borrower not found");
        }

        return matchingBorrowers;
    }


    public void borrowBook(Book book, Borrower borrower, int daysToReturn) throws NotAvailableException, IllegalArgumentException, BorrowerNotFoundExeption, BookNotFoundExeption {
        if (book == null) {
            System.out.println("Model.Book is null");
            throw new BookNotFoundExeption("Model.Book is null");
        }
        if(borrower == null){
            System.out.println("Model.Borrower is null");
            throw new BorrowerNotFoundExeption("Model.Borrower is null");
        }

        if(!book.IsAvailable()){
            System.out.println("Model.Book is not available for borrowing");
            throw new NotAvailableException("Model.Book is not available for borrowing");
        }
        if(daysToReturn <= 0){
            System.out.println("Days to return must be greater than 0");
            throw new IllegalArgumentException("Days to return must be greater than 0");
        }
        book.setAvailable(false);
        BorrowingHistory borrowingHistory = new BorrowingHistory(book, borrower, LocalDate.now().plusDays(daysToReturn));
        borrower.addBorrowingHistory(borrowingHistory);
        book.addBorrowingHistory(borrowingHistory);
    }

    public void returnBook(Book book){
        book.setAvailable(true);
        for (BorrowingHistory borrowingHistory : book.getBorrowingHistory()) {
            if (!borrowingHistory.isReturned()) {
                borrowingHistory.setReturned(true);
                break;
            }
        }
    }


    public List<Book> getAvailableBooks(){
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if(book.IsAvailable()){
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    public List<Book> getBorrowedBooks(){
        List<Book> borrowedBooks = new ArrayList<>();
        for (Book book : books) {
            if(!book.IsAvailable()){
                borrowedBooks.add(book);
            }
        }
        return borrowedBooks;
    }

    public List<Book> getOverdueBooks(){
        List<Book> overdueBooks = new ArrayList<>();
        for (Book book : books) {
            for (BorrowingHistory borrowingHistory : book.getBorrowingHistory()) {
                if(!borrowingHistory.isReturned() && LocalDate.now().isAfter(borrowingHistory.getDueDate())){
                    overdueBooks.add(book);
                    break;
                }
            }
        }
        return overdueBooks;
    }

    public List<Book> searchBooks_by_title(String title) throws BookNotFoundExeption {

        List<Book> matchingBooks = new ArrayList<>();

        for (Book book : books) {
            if (title.isEmpty() || book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                matchingBooks.add(book);
            }
        }
        if (matchingBooks.isEmpty()) {
            throw new BookNotFoundExeption("Model.Book not found");
        }

        return matchingBooks;
    }

    public Book getBookById(int id) throws BookNotFoundExeption {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        throw new BookNotFoundExeption("Model.Book not found");
    }


    public static void PrintBooks(List<Book> books){
        for (Book book : books) {
            System.out.println(book);
        }
    }


    public void setNextID(){
        int max = 0;
        for (Book book : books) {
            if(book.getId() > max){
                max = book.getId();
            }
        }
        Book.setNextID(max + 1);

        max = 0;
        for (Borrower borrower : borrowers) {
            if(borrower.getMembershipID() > max){
                max = borrower.getMembershipID();
            }
        }
        Borrower.setNextID(max + 1);
    }

}
