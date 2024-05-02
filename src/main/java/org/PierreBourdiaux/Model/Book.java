package org.PierreBourdiaux.Model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {
        private static final long serialVersionUID = 1L;

        private int id;
        private static int nextID = 1;

        private String title;
        private String author;
        private int publicationYear;
        private Genre genre;

        public List<BorrowingHistory> borrowingHistory;
        private boolean isAvailable = true;

        public Book(String title, String author, int publicationYear, Genre genre) {
            this.title = title;
            this.author = author;
            this.publicationYear = publicationYear;
            this.genre = genre;
            borrowingHistory = new ArrayList<>();
            id = nextID;
            nextID++;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public int getPublicationYear() {
            return publicationYear;
        }

        public Genre getGenre() {
            return genre;
        }

    public int getId() {
        return id;
    }

    public List<BorrowingHistory> getBorrowingHistory() {
            return borrowingHistory;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setPublicationYear(int publicationYear) {
            this.publicationYear = publicationYear;
        }

        public void setGenre(Genre genre) {
            this.genre = genre;
        }

        public boolean IsAvailable() {
            return isAvailable;
        }

        public void setAvailable(boolean available) {
            isAvailable = available;
        }

        public void addBorrowingHistory(BorrowingHistory borrowingHistory) {
            this.borrowingHistory.add(borrowingHistory);
        }

        public static void setNextID(int nextID) {
            Book.nextID = nextID;
        }

        @Override
        public String toString() {
            return "Model.Book{" +
                    "title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", publicationYear=" + publicationYear +
                    ", genre='" + genre + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object other){
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;
            Book book = (Book) other;
            return title.equals(book.title) && author.equals(book.author) && publicationYear == book.publicationYear && genre.equals(book.genre);
        }
}
