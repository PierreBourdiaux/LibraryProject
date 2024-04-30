package org.PierreBourdiaux;


import org.PierreBourdiaux.Model.Book;
import org.PierreBourdiaux.Model.Borrower;
import org.PierreBourdiaux.Model.BorrowingHistory;
import org.PierreBourdiaux.Model.Genre;
import org.PierreBourdiaux.Utils.BookAuthorFilter;
import org.PierreBourdiaux.Utils.BookGenreFilter;
import org.PierreBourdiaux.Utils.BookTitleFilter;
import org.PierreBourdiaux.Utils.BookYearFilter;
import org.PierreBourdiaux.dataPersitence.Persistance;
import org.PierreBourdiaux.dataPersitence.SerialisationPersistance;
import org.PierreBourdiaux.exceptions.BookNotFoundExeption;
import org.PierreBourdiaux.exceptions.BorrowerNotFoundExeption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Console {

    private Library library;
    private boolean isInitialized = false;

    private Book current_Book;
    private Borrower current_Borrower;

    Persistance persistance;

    public Console() {
        current_Book = null;
        current_Borrower = null;
    }

    public void init() {
        persistance = new SerialisationPersistance();
        library = persistance.loadLibrary();
        if (library == null) library = new Library();
        isInitialized = true;
    }

    private void showMenu() {
        System.out.println("What would you like to do?");

        System.out.println("1. Add a book");
        System.out.println("2. Remove a book");
        System.out.println("3. Search for a book");
        System.out.println("4. Add a borrower");
        System.out.println("5. Remove a borrower");
        System.out.println("6. Search for a borrower");
        System.out.println("7. Borrow a book");
        System.out.println("8. Return a book");
        System.out.println("9. Show all books");
        System.out.println("10. Show all borrowers");
        System.out.println("11. Show all borrowing history");
        System.out.println("12. Exit");
    }

    public void run() {
        if (!isInitialized) {
            init();
            isInitialized = true;
        }

        String input = "";

        System.out.println("Welcome to the Library!");


        while (!input.equals("12")) {
            showMenu();
            System.out.println("Enter your choice: ");

            // Enter data using BufferReader
            input = getInput();

            switch (input) {
                case "1":
                    addBook();
                    break;
                case "2":
                    removeBook();
                    break;
                case "3":
                    searchBook();
                    break;
                case "4":
                    addBorrower();
                    break;
                case "5":
                    removeBorrower();
                    break;
                case "6":
                    searchBorrower();
                    break;
                case "7":
                    borrowBook();
                    break;
                case "8":
                    returnBook();
                    break;
                case "9":
                    showAllBooks();
                    break;
                case "10":
                    showAllBorrowers();
                    break;
                case "11":
                    showAllBorrowingHistory();
                    break;
                case "12":

                    saveLibrary();
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
            System.out.println();
        }

    }



    private String getInput() {
        String input = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            input = reader.readLine();
        } catch (IOException e) {
            System.out.println("An error occurred while reading input\n message : " + e.getMessage());
        }
        return input;
    }


    // Action fonction

    private void addBook() {
        System.out.println("Enter the title of the book: ");
        String title = getInput();
        System.out.println("Enter the author of the book: ");
        String author = getInput();
        System.out.println("Enter the genre of the book: ");
        System.out.println("1. FANTASY, 2. SCIENCE_FICTION, 3. MYSTERY, 4. THRILLER, 5. ROMANCE, 6. HORROR, 7. BIOGRAPHY, 8. HISTORY, 9. COOKBOOK, 10. OTHER");
        Genre genre = Genre.OTHER;
        int genreIndex = 0;
        try {
            genreIndex = Integer.parseInt(getInput());
        } catch (Exception e) {
            System.out.println("Invalid genre. 'Other' is choose by default.");
            genreIndex =10;
        }
        if(genreIndex < 1 || genreIndex > 10) {
            System.out.println("Invalid genre index. 'Other' is choose by default.");
            genreIndex =10;

        }
        genre = Genre.values()[genreIndex - 1];
        System.out.println("Enter the publication year of the book: ");
        int publicationYear = Integer.parseInt(getInput());
        library.addBook(new Book(title, author, publicationYear, genre));
        System.out.println(title+ " is added to the library.");

    }

    private void showAllBooks() {
        System.out.println("All books in the library:");
        for (Book book : library.getBooks()) {
            System.out.println(book.getTitle() + " by " + book.getAuthor() + " (" + book.getPublicationYear() + ")");
        }
    }

    private void showAllBorrowers() {
        System.out.println("All borrowers in the library:");
        for (Borrower borrower : library.getBorrowers()) {
            System.out.println(borrower);
        }
    }


    private void showAllBorrowingHistory() {
        System.out.println("History by : 1. Model.Book, 2. Model.Borrower");
        String choice = getInput();
        if(choice.equals("1")) {
            for (Book book : library.getBooks()) {
                System.out.println("Borrowing history for " + book.getTitle() + ":");
                for (BorrowingHistory history : book.getBorrowingHistory()) {
                    System.out.println("\t" + history.getBorrower().getName() + " borrowed on " + history.getBorrowDate() );
                }
                System.out.println();

            }
        }
        else if(choice.equals("2")) {
            for (Borrower borrower : library.getBorrowers()) {
                System.out.println("Borrowing history for " + borrower.getName() + ":");
                for (BorrowingHistory history : borrower.getBorrowingHistory()) {
                    System.out.println("\t" + history.getBook().getTitle() + " borrowed on " + history.getBorrowDate() );
                }
                System.out.println();
            }
        }
        else {
            System.out.println("Invalid choice. Please try again.");
        }
    }

    private void addBorrower() {
        System.out.println("Enter the name of the borrower: ");
        String name = getInput();
        System.out.println("Enter the email of the borrower: ");
        String address = getInput();
        System.out.println("Enter the phone number of the borrower: ");
        String phoneNumber = getInput();
        library.addBorrower(new Borrower(name, address, phoneNumber));
        System.out.println(name+ " is added to the library.");
    }

    private Borrower getBorrower() throws BorrowerNotFoundExeption {
        System.out.println("Do you want to search for a borrower by 1. Name, 2. Membership ID");
        String choice = getInput();

        Borrower borrower = null;
        if(choice.equals("1")) {
            System.out.println("Enter the name of the borrower: ");
            String name = getInput();
            borrower = library.searchBorrowers_by_name(name).get(0);

        }
        else if(choice.equals("2")) {
            System.out.println("Enter the membership ID of the borrower: ");
            int membershipID = Integer.parseInt(getInput());

            borrower = library.searchBorrowers_by_membershipID(membershipID).get(0);

        }
        else {
            System.out.println("Invalid choice. Please try again.");
        }
        return borrower;
    }

    private void searchBorrower() {
        Borrower borrower = null;
        try {
            borrower = getBorrower();
        } catch (BorrowerNotFoundExeption e) {
            System.out.println("No borrower found");
        }
        System.out.println("Model.Borrower found: " + borrower);
        current_Borrower=borrower;
    }

    private void removeBorrower() {
        if(current_Borrower != null){
            System.out.println("Do you want to remove this borrower ? (Y/N) : \n Current borrower : "+current_Borrower);
            String input = getInput();
            if (input.toLowerCase().equals("y")){
                library.removeBorrower(current_Borrower);
                current_Borrower = null;
                System.out.println("Borrower deleted");
                return;
            }
        }
        try {
            current_Borrower = getBorrower();
        } catch (BorrowerNotFoundExeption e) {
            System.out.println("No borrower Found");
        }
        library.removeBorrower(current_Borrower);
        current_Borrower = null;
        System.out.println("Borrower deleted");
    }

    private Book getBook() throws BookNotFoundExeption {
        List<Book> matchingBooks = new ArrayList<Book>(library.getBooks());

        while (matchingBooks.size()>1){
            System.out.println("Do you want to search for a book by 1. Title, 2. Author, 3. Genre, 4. Publication Year");
            String choice = getInput();
            switch (choice) {
                case "1":
                    System.out.println("Enter the title of the book: ");
                    String title = getInput();
                    matchingBooks = (new BookTitleFilter().filter(matchingBooks,title));
                    break;
                case "2":
                    System.out.println("Enter the author of the book: ");
                    String author = getInput();
                    matchingBooks = (new BookAuthorFilter().filter(matchingBooks,author));

                    break;
                case "3":
                    System.out.println("Enter the genre of the book: ");
                    System.out.println("(FANTASY, SCIENCE_FICTION, MYSTERY, THRILLER, ROMANCE, HORROR, BIOGRAPHY, HISTORY, COOKBOOK, OTHER)");
                    String genre = getInput();
                    matchingBooks = (new BookGenreFilter().filter(matchingBooks, genre));
                    break;
                case "4":
                    System.out.println("Enter the publication year of the book: ");
                    String publicationYear = (getInput());
                    matchingBooks = (new BookYearFilter().filter(matchingBooks,publicationYear));
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
            System.out.println("Matching books:");
            for(Book b : matchingBooks) System.out.println(b);

        }
        if(matchingBooks.size() == 0) {
            throw new BookNotFoundExeption("Book not found");
        }
        return matchingBooks.get(0);

    }

    private void searchBook() {
        Book book = null;
        try {
            book = getBook();
        } catch (BookNotFoundExeption e) {
            System.out.println("Book not found");
            return;
        }

        System.out.println("Book found: " + book);
        current_Book = book;


    }

    private void removeBook() {
        if(current_Book != null){
            System.out.println("Do you want to remove this book ? (Y/N) : \n Current book : "+current_Book);
            String input = getInput();
            if (input.toLowerCase().equals("y")){
                library.removeBook(current_Book);
                current_Book = null;
                System.out.println("Book deleted");
                return;
            }
        }
        try {
            current_Book = getBook();
        } catch (BookNotFoundExeption e) {
            System.out.println("No book Found");
        }
        library.removeBook(current_Book);
        current_Book = null;
        System.out.println("Book deleted");
    }

    private void borrowBook() {
        if(current_Borrower != null){
            System.out.println("is the current borrower the right one? (Y/N) : \n Current borrower : "+current_Borrower);
            String input = getInput();
            if (!input.toLowerCase().equals("y")){
                try {
                    current_Borrower = getBorrower();
                } catch (BorrowerNotFoundExeption e) {

                }
            }
        }else {
            try {
                current_Borrower = getBorrower();
            } catch (BorrowerNotFoundExeption e) {
                System.out.println("No borrower Found");
                return;
            }
        }

        if(current_Book != null){
            System.out.println("is the current book the right one? (Y/N) : \n Current book : "+current_Book);
            String input = getInput();
            if (!input.toLowerCase().equals("y")){
                try {
                    current_Book = getBook();
                } catch (BookNotFoundExeption e) {
                    System.out.println("No book Found");
                    return;
                }
            }
        } else {
            try {
                current_Book = getBook();
            } catch (BookNotFoundExeption e) {
                System.out.println("No book Found");
                return;
            }
        }

        System.out.println("Enter the number of days to borrow the book: ");
        int daysToReturn = 7; // default value
        try {
            daysToReturn = Integer.parseInt(getInput());
            if(daysToReturn < 1 || daysToReturn > 365) daysToReturn = 7;

        } catch (Exception e) {
            System.out.println("Invalid number of days. 7 days is choose by default.");

        }

        try {
            library.borrowBook(current_Book, current_Borrower, daysToReturn);
            System.out.println(current_Borrower.getName() + " borrowed " + current_Book.getTitle() + " for " + daysToReturn + " days.");
        } catch (Exception e) {
            System.out.println("An error occurred while borrowing the book\n message : " + e.getMessage());
        }

    }

    private void returnBook() {
        if(current_Borrower != null){
            System.out.println("is the current borrower the right one? (Y/N) : \n Current borrower : "+current_Borrower);
            String input = getInput();
            if (!input.toLowerCase().equals("y")){
                try {
                    current_Borrower = getBorrower();
                } catch (BorrowerNotFoundExeption e) {
                    System.out.println("No borrower Found");
                    return;
                }
            }
        }
        List<Book> booksToBeReturned = current_Borrower.getBookToBeReturned();
        if(booksToBeReturned.size() == 0) {
            System.out.println("No book to be returned");
            return;
        }
        System.out.println("Choose the index of the book to be returned:");
        for (int i = 0; i < booksToBeReturned.size(); i++) {
            System.out.println(i + ". " + booksToBeReturned.get(i).getTitle());
        }
        int index = Integer.parseInt(getInput());
        Book book = booksToBeReturned.get(index);
        library.returnBook(book);

        System.out.println(current_Borrower.getName() + " returned " + book.getTitle());
    }


    private void saveLibrary() {

        persistance.saveLibrary(library);
    }


}
