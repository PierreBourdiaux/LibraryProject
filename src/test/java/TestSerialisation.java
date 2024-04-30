import org.PierreBourdiaux.Library;
import org.PierreBourdiaux.Model.Book;
import org.PierreBourdiaux.Model.Borrower;
import org.PierreBourdiaux.Model.BorrowingHistory;
import org.PierreBourdiaux.Model.Genre;
import org.junit.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestSerialisation {


    @Test
    public void testSerialisation_one_book() throws IOException, ClassNotFoundException {
        Book book = new Book("Harry Potter", "J.K. Rowling", 1997, Genre.FANTASY);


        FileOutputStream fileOutputStream
                = new FileOutputStream("yourfile.txt");
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(book);
        objectOutputStream.flush();
        objectOutputStream.close();

        FileInputStream fileInputStream
                = new FileInputStream("data.txt");
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        Book b2 = (Book) objectInputStream.readObject();
        objectInputStream.close();

        assertTrue(b2.getTitle().equals(book.getTitle()));
        assertTrue(b2.getAuthor().equals(book.getAuthor()));
        assertTrue(b2.getPublicationYear() == book.getPublicationYear());
        assertTrue(b2.getGenre().equals(book.getGenre()));

        assertTrue(b2.equals(book));
    }

    @Test
    public void testSerialisation_list_book() throws IOException, ClassNotFoundException {
        Book book1 = new Book("Harry Potter", "J.K. Rowling", 1997, Genre.FANTASY);
        Book book2 = new Book("Harry Potter 2", "J.K. Rowling", 1998, Genre.FANTASY);
        Book book3 = new Book("Harry Potter 3", "J.K. Rowling", 1999, Genre.FANTASY);

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);

        FileOutputStream fileOutputStream
                = new FileOutputStream("data.txt");
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(books);
        objectOutputStream.flush();
        objectOutputStream.close();

        FileInputStream fileInputStream
                = new FileInputStream("data.txt");
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        List<Book> books2 = (List<Book>) objectInputStream.readObject();
        objectInputStream.close();

        assertTrue(books2.size() == books.size());
        for (int i = 0; i < books.size(); i++) {
            assertTrue(books2.get(i).equals(books.get(i)));
        }
    }


    @Test
    public void testSerialisation_one_borrower() throws IOException, ClassNotFoundException {
        Borrower borrower = new Borrower("Pierre", "email.email.fr", "+33666666666");
        FileOutputStream fileOutputStream
                = new FileOutputStream("data.txt");
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(borrower);
        objectOutputStream.flush();
        objectOutputStream.close();

        FileInputStream fileInputStream
                = new FileInputStream("data.txt");
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        Borrower b2 = (Borrower) objectInputStream.readObject();
        objectInputStream.close();

        assertTrue(borrower.equals(b2));

    }

    @Test
    public void testSerialisation_list_borrower() throws IOException, ClassNotFoundException {
        Borrower borrower1 = new Borrower("Pierre", "email.email.fr", "+33666666666");
        Borrower borrower2 = new Borrower("Pierre2", "email.email.fr", "+33666666666");
        Borrower borrower3 = new Borrower("Pierre3", "email.email.fr", "+33666666666");


        List<Borrower> books = new ArrayList<>();
        books.add(borrower1);
        books.add(borrower2);
        books.add(borrower3);

        FileOutputStream fileOutputStream
                = new FileOutputStream("data.txt");
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(books);
        objectOutputStream.flush();
        objectOutputStream.close();

        FileInputStream fileInputStream
                = new FileInputStream("data.txt");
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        List<Borrower> borrowers2 = (List<Borrower>) objectInputStream.readObject();
        objectInputStream.close();

        assertTrue(borrowers2.size() == books.size());
        for (int i = 0; i < books.size(); i++) {
            assertTrue(borrowers2.get(i).equals(books.get(i)));
        }
    }


    @Test
    public void testSerialisation_one_borrowingHistory() throws IOException, ClassNotFoundException {
        Borrower borrower = new Borrower("Pierre", "email.email.fr", "+33666666666");
        Book book = new Book("Harry Potter", "J.K. Rowling", 1997, Genre.FANTASY);
        BorrowingHistory borrowingHistory = new BorrowingHistory(book, borrower, LocalDate.now().plusDays(10));
        borrower.addBorrowingHistory(borrowingHistory);
        FileOutputStream fileOutputStream
                = new FileOutputStream("data.txt");
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(borrowingHistory);
        objectOutputStream.flush();
        objectOutputStream.close();

        FileInputStream fileInputStream
                = new FileInputStream("data.txt");
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        BorrowingHistory b2 = (BorrowingHistory) objectInputStream.readObject();
        objectInputStream.close();


        assertTrue(borrowingHistory.equals(b2));
    }


    @Test
    public void testSerialisation_Library() {
        Library library = new Library();
        //add 3 book
        library.addBook(new Book("Harry Potter", "J.K. Rowling", 1997, Genre.FANTASY));
        library.addBook(new Book("Harry Potter 2", "J.K. Rowling", 1998, Genre.FANTASY));
        library.addBook(new Book("Harry Potter 3", "J.K. Rowling", 1999, Genre.FANTASY));

        //add 3 borrower
        library.addBorrower(new Borrower("Pierre", "email.email.fr", "+33666666666"));
        library.addBorrower(new Borrower("Pierre2", "email.email.fr", "+33666666666"));
        library.addBorrower(new Borrower("Pierre3", "email.email.fr", "+33666666666"));

        //borrow a book
        try {
            library.borrowBook(library.getBooks().get(0), library.getBorrowers().get(0), 10);
            library.borrowBook(library.getBooks().get(1), library.getBorrowers().get(1), 10);
            library.borrowBook(library.getBooks().get(2), library.getBorrowers().get(1), 10);
            library.returnBook(library.getBooks().get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //serialisation
        try {
            FileOutputStream fileOutputStream
                    = new FileOutputStream("data.txt");
            ObjectOutputStream objectOutputStream
                    = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(library);
            objectOutputStream.flush();
            objectOutputStream.close();

            FileInputStream fileInputStream
                    = new FileInputStream("data.txt");
            ObjectInputStream objectInputStream
                    = new ObjectInputStream(fileInputStream);
            Library library2 = (Library) objectInputStream.readObject();
            objectInputStream.close();

            assertTrue(library.equals(library2));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
