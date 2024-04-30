package org.PierreBourdiaux;

import org.PierreBourdiaux.Model.Book;
import org.PierreBourdiaux.Model.Borrower;
import org.PierreBourdiaux.Model.BorrowingHistory;
import org.PierreBourdiaux.Model.Genre;
import org.PierreBourdiaux.Utils.*;
import org.PierreBourdiaux.dataPersitence.Persistance;
import org.PierreBourdiaux.dataPersitence.SerialisationPersistance;
import org.PierreBourdiaux.exceptions.BookNotFoundExeption;
import org.PierreBourdiaux.exceptions.BorrowerNotFoundExeption;
import org.PierreBourdiaux.exceptions.NotAvailableException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Graphicapp {

    static boolean isAdmin = false;
    Library library;
    boolean isInit = false;

    private Book currentBook;
    private Borrower currentBorrower;

    Persistance persistance;

    public void init() {
        persistance = new SerialisationPersistance();
        library = persistance.loadLibrary();
        if (library == null) {
            library = new Library();
        }
        isInit = true;
    }

   public void run() {
    if (!isInit) {
        init();
    }
    JFrame f = new JFrame("Library");
    f.setSize(1200,720);
    f.setLayout(new FlowLayout()); // Ajout d'un LayoutManager
       f.setResizable(false);

    JLabel Title = new JLabel("Welcome to the Library");
    Title.setPreferredSize(new Dimension(1200, 100));
    Title.setFont(new Font("Arial", Font.PLAIN, 50));
    Title.setHorizontalAlignment(SwingConstants.CENTER);
    f.add(Title);
    f.setVisible(true);
    Login(f);
    //isAdmin = true; //remove this line
    while (!isAdmin) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //clear
    clear(f);
    showMenu(f);


    }

    private void clear(JFrame f) {
        f.getContentPane().removeAll();
        f.repaint();
        f.revalidate();
    }

    private void Login(JFrame f) {

        clear(f);

        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");
        JTextField user = new JTextField(20);
        JPasswordField password = new JPasswordField(20);
        JButton b = new JButton("Login");
        f.add(usernameLabel);
        f.add(user);
        f.add(passwordLabel);
        f.add(password);
        f.add(b);
        f.setVisible(true);
        b.addActionListener(e -> {
            if (user.getText().equals("admin") && password.getText().equals("admin")) {
               isAdmin = true;
               System.out.println("Admin");
               return;
            }
        });

    }

    private void showMenu(JFrame f) {

        //clear
        clear(f);

        JLabel Title2 = new JLabel("What do you want to do?");
        Title2.setPreferredSize(new Dimension(1200, 100));
        Title2.setFont(new Font("Arial", Font.PLAIN, 30));
        Title2.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(Title2);



        //add a book
        JButton addBook = new JButton("Add a book");
        addBook.setPreferredSize(new Dimension(400, 50));
        f.add(addBook);
        addBook.addActionListener(e -> addBook(f));

        //remove a book
        JButton removeBook = new JButton("Remove a book");
        removeBook.setPreferredSize(new Dimension(400, 50));
        f.add(removeBook);
        removeBook.addActionListener(e -> deleteBook(f));

        //search for a book
        JButton searchBook = new JButton("Search for a book");
        searchBook.setPreferredSize(new Dimension(400, 50));
        f.add(searchBook);
        searchBook.addActionListener(e-> searchBook(f, library.getBooks()));

        //add a borrower
        JButton addBorrower = new JButton("Add a borrower");
        addBorrower.setPreferredSize(new Dimension(400, 50));
        f.add(addBorrower);
        addBorrower.addActionListener(e-> addBorrower(f));

        //remove a borrower
        JButton removeBorrower = new JButton("Remove a borrower");
        removeBorrower.setPreferredSize(new Dimension(400, 50));
        f.add(removeBorrower);
        removeBorrower.addActionListener(e -> deleteBorrower(f));


        //search for a borrower
        JButton searchBorrower = new JButton("Search for a borrower");
        searchBorrower.setPreferredSize(new Dimension(400, 50));
        f.add(searchBorrower);
        searchBorrower.addActionListener(e-> searchBorrower(f, library.getBorrowers()));

        //borrow a book
        JButton borrowBook = new JButton("Borrow a book");
        borrowBook.setPreferredSize(new Dimension(400, 50));
        f.add(borrowBook);
        borrowBook.addActionListener(e -> borrowBook(f));

        //return a book
        JButton returnBook = new JButton("Return a book");
        returnBook.setPreferredSize(new Dimension(400, 50));
        f.add(returnBook);
        returnBook.addActionListener(e -> returnBook(f));

        //show all books
        JButton showBooks = new JButton("Show all books");
        showBooks.setPreferredSize(new Dimension(400, 50));
        f.add(showBooks);
        showBooks.addActionListener(e -> showBooks(f, library.getBooks(), true));

        //show all borrowers
        JButton showBorrowers = new JButton("Show all borrowers");
        showBorrowers.setPreferredSize(new Dimension(400, 50));
        f.add(showBorrowers);
        showBorrowers.addActionListener(e -> showBorrowers(f, library.getBorrowers(), true));

        //show all borrowing history
        JButton showBorrowingHistory = new JButton("Show all borrowing history");
        showBorrowingHistory.setPreferredSize(new Dimension(400, 50));
        f.add(showBorrowingHistory);
        showBorrowingHistory.addActionListener(e -> showBorrowingHistory(f));

        //exit
        JButton exit = new JButton("Exit");
        exit.setPreferredSize(new Dimension(400, 50));
        f.add(exit);
        exit.addActionListener(e -> exit(f));

        f.setVisible(true);

    }

    private void showBooks(JFrame f, List<Book> books, boolean clear) {
        // Suppression des anciens composants
        if(clear)clear(f);

        JLabel Title = new JLabel("Books");
        Title.setPreferredSize(new Dimension(1200, 100));
        Title.setFont(new Font("Arial", Font.PLAIN, 50));
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(Title);

        JLabel explain = new JLabel("Double click on a book to select it");
        explain.setPreferredSize(new Dimension(1200, 50));
        explain.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(explain);

        // Création du modèle de table
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };

        model.addColumn("Title");
        model.addColumn("Author");
        model.addColumn("Year");
        model.addColumn("Genre");
        model.addColumn("Available");

        // Ajout des livres au modèle
        for (Book book : books) {
            //boutton

            model.addRow(new Object[]{book.getTitle(), book.getAuthor(),book.getPublicationYear(), book.getGenre(), book.IsAvailable()? "Available":"Not available"});
        }

        // Creating Table
        JTable table = new JTable(model);

        // Add double click listener
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) { // row selected
                        String selectedBookTitle = (String) model.getValueAt(selectedRow, 0);
                        for (Book book : books) {
                            if (book.getTitle().equals(selectedBookTitle)) {
                                currentBook = book; // Définition du livre sélectionné comme currentBook
                                System.out.println("Double-clicked on " + book.getTitle());
                                //show dialog
                                JOptionPane.showMessageDialog(f, book.getTitle() + " selected", "Book selected", JOptionPane.INFORMATION_MESSAGE);



                                break;
                            }
                        }
                    }
                }}});


        table.setPreferredSize(new Dimension(1000, 100));
        // Création du JScrollPane et ajout de la table
        JScrollPane tableScroller = new JScrollPane(table);
        tableScroller.setPreferredSize(new Dimension(1000,100 ));
        f.add(tableScroller);

        // Bouton de retour
       backButton(f);

        // Mise à jour de l'interface graphique
        f.revalidate();
        f.repaint();
        f.setVisible(true);
    }

    private void showBorrowers(JFrame f, List<Borrower> borrowers, boolean clear) {
        if(clear)clear(f);
        JLabel Title = new JLabel("Borrowers");
        Title.setPreferredSize(new Dimension(1200, 100));
        Title.setFont(new Font("Arial", Font.PLAIN, 50));
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(Title);

        JLabel explain = new JLabel("Double click on a borrower to select it");
        explain.setPreferredSize(new Dimension(1200, 20));
        explain.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(explain);

        //current borrower selected
        JLabel currentBorrowerLabel = new JLabel("Current borrower: " + (currentBorrower == null ? "None" : currentBorrower.getMembershipID()+"."+ currentBorrower.getName()));
        currentBorrowerLabel.setPreferredSize(new Dimension(1200, 20));
        currentBorrowerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if(currentBorrower!=null)f.add(currentBorrowerLabel);

        // Création du modèle de table
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        model.addColumn("Name");
        model.addColumn("Email");
        model.addColumn("Phone number");
        model.addColumn("Membership ID");

        // Ajout des emprunteurs au modèle
        for (Borrower borrower : borrowers) {
            model.addRow(new Object[]{borrower.getName(), borrower.getEmail(), borrower.getPhoneNumber(), borrower.getMembershipID()});
        }

        // Création de la table
        JTable table = new JTable(model);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) { // row selected
                        int selectedIdMember = (int) model.getValueAt(selectedRow, 3);
                        for (Borrower borrower : borrowers) {
                            if (borrower.getMembershipID() == (selectedIdMember)) {
                                currentBorrower = borrower; // Définition du livre sélectionné comme currentBook
                                System.out.println("Double-clicked on " + borrower.getName());
                                //show dialog
                                JOptionPane.showMessageDialog(f, borrower.getName() + " selected", "Borrower selected", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                        }
                    }
                }}});


        table.setPreferredSize(new Dimension(1000, 100));
        JScrollPane tableScroller = new JScrollPane(table);
        tableScroller.setPreferredSize(new Dimension(1000, 100));
        f.add(tableScroller);

        backButton(f);
    }

    private void addBook(JFrame f) {
        clear(f);
        JLabel Title = new JLabel("Add a book");
        Title.setPreferredSize(new Dimension(1200, 100));
        Title.setFont(new Font("Arial", Font.PLAIN, 50));
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(Title);

        // Création d'un nouveau JPanel avec un BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("Title");
        JTextField title = new JTextField(50);
        panel.add(titleLabel);
        panel.add(title);

        // Author
        JLabel authorLabel = new JLabel("Author");
        JTextField author = new JTextField(50);
        panel.add(authorLabel);
        panel.add(author);

        // Year
        JLabel yearLabel = new JLabel("Year");
        JTextField year = new JTextField(20);
        panel.add(yearLabel);
        panel.add(year);

        // Genre with a combobox
        JLabel genreLabel = new JLabel("Genre");
        String[] genres = {"FANTASY", "SCIENCE_FICTION", "NOVEL", "HISTORY", "BIOGRAPHY", "OTHER"};
        JComboBox<String> genre = new JComboBox<>(genres);
        panel.add(genreLabel);
        panel.add(genre);

        // Add button
        JButton add = new JButton("Add");
        panel.add(add);

        // Ajout du panel à la fenêtre
        f.add(panel);
        backButton(f);
        f.setVisible(true);

        add.addActionListener(e -> {
            if (title.getText().isEmpty() || author.getText().isEmpty() || year.getText().isEmpty()) {
                JOptionPane.showMessageDialog(f, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int yearInt = 0;
            try {
                yearInt = Integer.parseInt(year.getText());
                if (yearInt > Year.now().getValue()) {
                    JOptionPane.showMessageDialog(f, "Year must be a number under "+ (Year.now().getValue()+1) , "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(f, "Year must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
                library.addBook(new Book(title.getText(), author.getText(), yearInt, Genre.valueOf((String) genre.getSelectedItem())));
            showMenu(f);

        });
    }

    private void addBorrower(JFrame f) {
        clear(f);
        JLabel Title = new JLabel("Add a borrower");
        Title.setPreferredSize(new Dimension(1200, 100));
        Title.setFont(new Font("Arial", Font.PLAIN, 50));
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(Title);

        // Création d'un nouveau JPanel avec un BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Name
        JLabel nameLabel = new JLabel("Name");
        JTextField name = new JTextField(50);
        panel.add(nameLabel);
        panel.add(name);

        // Email
        JLabel emailLabel = new JLabel("Email");
        JTextField email = new JTextField(50);
        panel.add(emailLabel);
        panel.add(email);

        // Phone number
        JLabel phoneNumberLabel = new JLabel("Phone number");
        JTextField phoneNumber = new JTextField(20);
        panel.add(phoneNumberLabel);
        panel.add(phoneNumber);

        // Add button
        JButton add = new JButton("Add");
        panel.add(add);

        // Ajout du panel à la fenêtre
        f.add(panel);
        backButton(f);
        f.setVisible(true);

        add.addActionListener(e -> {
            if (name.getText().isEmpty() || email.getText().isEmpty() || phoneNumber.getText().isEmpty()) {
                JOptionPane.showMessageDialog(f, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                    .matcher(email.getText())
                    .matches()) {
                JOptionPane.showMessageDialog(f, "Invalid email", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            library.addBorrower(new Borrower(name.getText(), email.getText(), phoneNumber.getText()));
            showMenu(f);

        });
    }

    private void searchBook(JFrame f, List<Book> books){
        clear(f);
        JLabel Title = new JLabel("Search for a book");
        Title.setPreferredSize(new Dimension(1200, 100));
        Title.setFont(new Font("Arial", Font.PLAIN, 50));
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(Title);

        // Création d'un nouveau JPanel avec un BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("Title");
        JTextField title = new JTextField(50);
        panel.add(titleLabel);
        panel.add(title);

        // Author
        JLabel authorLabel = new JLabel("Author");
        JTextField author = new JTextField(50);
        panel.add(authorLabel);
        panel.add(author);

        // Year
        JLabel yearLabel = new JLabel("Year");
        JTextField year = new JTextField(20);
        panel.add(yearLabel);
        panel.add(year);

        // Genre with a combobox
        JLabel genreLabel = new JLabel("Genre");
        String[] genres = {"NONE","FANTASY", "SCIENCE_FICTION", "NOVEL", "HISTORY", "BIOGRAPHY", "OTHER"};
        JComboBox<String> genre = new JComboBox<>(genres);
        panel.add(genreLabel);
        panel.add(genre);

        // Search button
        JButton search = new JButton("Search");
        panel.add(search);

        // Ajout du panel à la fenêtre
        f.add(panel);

        showBooks(f, books, false);

        f.setVisible(true);

        search.addActionListener(e -> {
            List<Book> matchingBooks = new ArrayList<>(books);

            if (title.getText().isEmpty() && author.getText().isEmpty() && year.getText().isEmpty() && genre.getSelectedItem().equals("NONE")) {
                JOptionPane.showMessageDialog(f, "At least one field must be filled", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(!title.getText().isEmpty()){
                matchingBooks = (new BookTitleFilter()).filter(matchingBooks,title.getText());
            }
            if(!author.getText().isEmpty()){
                matchingBooks = (new BookAuthorFilter()).filter(matchingBooks, author.getText());
            }
            if(!year.getText().isEmpty()) {
                matchingBooks = (new BookYearFilter()).filter(matchingBooks, year.getText());
            }
            if(!genre.getSelectedItem().toString().equals("NONE")){
                String test = genre.getSelectedItem().toString();
                matchingBooks = (new BookGenreFilter()).filter(matchingBooks,genre.getSelectedItem().toString());
            }

            searchBook(f, matchingBooks);

        });
    }

    private void searchBorrower(JFrame f, List<Borrower> borrowers){
        clear(f);
        JLabel Title = new JLabel("Search for a borrower");
        Title.setPreferredSize(new Dimension(1200, 100));
        Title.setFont(new Font("Arial", Font.PLAIN, 50));
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(Title);

        // Création d'un nouveau JPanel avec un BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //membership ID
        JLabel membershipIDLabel = new JLabel("Membership ID");
        JTextField membershipID = new JTextField(20);
        panel.add(membershipIDLabel);
        panel.add(membershipID);

        // Name
        JLabel nameLabel = new JLabel("Name");
        JTextField name = new JTextField(50);
        panel.add(nameLabel);
        panel.add(name);

        // Email
        JLabel emailLabel = new JLabel("Email");
        JTextField email = new JTextField(50);
        panel.add(emailLabel);
        panel.add(email);

        // Phone number
        JLabel phoneNumberLabel = new JLabel("Phone number");
        JTextField phoneNumber = new JTextField(20);
        panel.add(phoneNumberLabel);
        panel.add(phoneNumber);


        // Search button
        JButton search = new JButton("Search");
        panel.add(search);

        // Ajout du panel à la fenêtre
        f.add(panel);

        showBorrowers(f, borrowers, false);

        f.setVisible(true);

        search.addActionListener(e -> {
            List<Borrower> matchingBorrowers = new ArrayList<>();
            //parsing membershipID
            int membershipIDInt = 0; // default value, if 0 -> return all borrowers
            if (!membershipID.getText().isEmpty()) {
                try {
                    membershipIDInt = Integer.parseInt(membershipID.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(f, "Membership ID must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            matchingBorrowers = library.searchBorrowers_By_Attribute(name.getText(), email.getText(), phoneNumber.getText(), membershipIDInt);
            searchBorrower(f, matchingBorrowers);

        });

    }

    private void backButton(JFrame f) {
        // Création d'un nouveau JPanel avec un BoxLayout horizontal
        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS));
        backPanel.setPreferredSize(new Dimension(1200, 50));

        // Ajout d'un espace flexible avant le bouton
        backPanel.add(Box.createHorizontalGlue());

        JButton back = new JButton("Back");
        back.setPreferredSize(new Dimension(400, 50));
        backPanel.add(back);

        // Ajout d'un espace flexible après le bouton
        backPanel.add(Box.createHorizontalGlue());

        f.add(backPanel);
        f.setVisible(true);
        back.addActionListener(e -> showMenu(f));
    }

    private void deleteBook(JFrame f) {
        if(currentBook == null) {
            JOptionPane.showMessageDialog(f, "No book selected, select a book before", "Error", JOptionPane.ERROR_MESSAGE);
            searchBook(f, library.getBooks());
        }
        else{
            clear(f);

            JLabel Title = new JLabel("Delete a book");
            Title.setPreferredSize(new Dimension(1200, 100));
            Title.setFont(new Font("Arial", Font.PLAIN, 50));
            Title.setHorizontalAlignment(SwingConstants.CENTER);
            f.add(Title);

            JLabel bookAsk = new JLabel("Do you want to remove this book ?");
            bookAsk.setPreferredSize(new Dimension(1200, 50));
            bookAsk.setFont(new Font("Arial", Font.PLAIN, 30));
            bookAsk.setHorizontalAlignment(SwingConstants.CENTER);
            f.add(bookAsk);

            //write info about the book
            JLabel bookInfo = new JLabel("Current Book : " +currentBook.getTitle()+ " by "+currentBook.getAuthor()+ " published in "+currentBook.getPublicationYear()+ " genre : "+currentBook.getGenre());
            bookInfo.setPreferredSize(new Dimension(1200, 50));
            bookInfo.setFont(new Font("Arial", Font.PLAIN, 20));
            bookInfo.setHorizontalAlignment(SwingConstants.CENTER);

            f.add(bookInfo);


            JButton delete = new JButton("Delete");
            JButton searchForAnotherBook = new JButton("Search for another book");
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(e -> showMenu(f));
            delete.addActionListener(e -> {
                library.removeBook(currentBook);
                currentBook = null;
                showMenu(f);
            });
            searchForAnotherBook.addActionListener(e -> searchBook(f, library.getBooks()));

            f.add(cancel);
            f.add(delete);
            f.add(searchForAnotherBook);

            f.setVisible(true);

        }
    }

    private void deleteBorrower(JFrame f) {
        if(currentBorrower == null) {
            JOptionPane.showMessageDialog(f, "No borrower selected, select a borrower before", "Error", JOptionPane.ERROR_MESSAGE);
            searchBorrower(f, library.getBorrowers());
        }
        else{
            clear(f);

            JLabel Title = new JLabel("Delete a borrower");
            Title.setPreferredSize(new Dimension(1200, 100));
            Title.setFont(new Font("Arial", Font.PLAIN, 50));
            Title.setHorizontalAlignment(SwingConstants.CENTER);
            f.add(Title);

            JLabel borrowerAsk = new JLabel("Do you want to remove this borrower ?");
            borrowerAsk.setPreferredSize(new Dimension(1200, 50));
            borrowerAsk.setFont(new Font("Arial", Font.PLAIN, 30));
            borrowerAsk.setHorizontalAlignment(SwingConstants.CENTER);
            f.add(Title);

            //write info about the borrower
            JLabel borrowerInfo = new JLabel("Current Borrower : " +currentBorrower.getName()+ " with membership ID "+currentBorrower.getMembershipID());
            borrowerInfo.setPreferredSize(new Dimension(1200, 50));
            borrowerInfo.setHorizontalAlignment(SwingConstants.CENTER);

            f.add(borrowerInfo);


            JButton delete = new JButton("Delete");
            JButton searchForAnotherBook = new JButton("Search for another borrower");
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(e -> showMenu(f));
            delete.addActionListener(e -> {
                library.removeBorrower(currentBorrower);
                currentBorrower = null;
                showMenu(f);
            });
            searchForAnotherBook.addActionListener(e -> searchBorrower(f, library.getBorrowers()));

            f.add(cancel);
            f.add(delete);
            f.add(searchForAnotherBook);

            f.setVisible(true);

        }


    }

    private void borrowBook(JFrame f) {

        if(currentBorrower == null){
            JOptionPane.showMessageDialog(f, "No borrower selected, select a borrower before", "Error", JOptionPane.ERROR_MESSAGE);
            searchBorrower(f, library.getBorrowers());
        }
        else if(currentBook == null){
            JOptionPane.showMessageDialog(f, "No book selected, select a book before", "Error", JOptionPane.ERROR_MESSAGE);
            searchBook(f, library.getBooks());
        }
        else{
            clear(f);

            JLabel Title = new JLabel("Borrow a book");
            Title.setPreferredSize(new Dimension(1200, 100));
            Title.setFont(new Font("Arial", Font.PLAIN, 50));
            Title.setHorizontalAlignment(SwingConstants.CENTER);
            f.add(Title);

            JLabel borrowAsk = new JLabel("Do you want to borrow this book ?");
            borrowAsk.setPreferredSize(new Dimension(1200, 50));
            borrowAsk.setFont(new Font("Arial", Font.PLAIN, 30));
            borrowAsk.setHorizontalAlignment(SwingConstants.CENTER);
            f.add(borrowAsk);

            //write info about the book
            JLabel bookInfo = new JLabel("Current Book : " +currentBook.getTitle()+ " by "+currentBook.getAuthor()+ " published in "+currentBook.getPublicationYear()+ " genre : "+currentBook.getGenre());
            bookInfo.setPreferredSize(new Dimension(1200, 50));
            bookInfo.setFont(new Font("Arial", Font.PLAIN, 20));
            bookInfo.setHorizontalAlignment(SwingConstants.CENTER);
            //button for changing the book

            f.add(bookInfo);

            //write info about the borrower
            JLabel borrowerInfo = new JLabel("Current Borrower : " +currentBorrower.getName()+ " with membership ID "+currentBorrower.getMembershipID());
            borrowerInfo.setPreferredSize(new Dimension(1200, 50));
            borrowerInfo.setFont(new Font("Arial", Font.PLAIN, 20));
            borrowerInfo.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel timeLimit = new JLabel("Time limit to return the book (in days)");
            JTextField timeLimitField = new JTextField(20);
            f.add(timeLimit);
            f.add(timeLimitField);


            f.add(borrowerInfo);

            JButton borrow = new JButton("Borrow");
            JButton searchForAnotherBook = new JButton("Search for another book");
            JButton searchForAnotherBorrower = new JButton("Search for another borrower");
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(e -> showMenu(f));
            searchForAnotherBook.addActionListener(e -> searchBook(f, library.getBooks()));
            searchForAnotherBorrower.addActionListener(e -> searchBorrower(f, library.getBorrowers()));
            borrow.addActionListener(e -> {
                int timeLimitInt = 0;
                try {
                    timeLimitInt = Integer.parseInt(timeLimitField.getText());
                    if (timeLimitInt <= 0) {
                        JOptionPane.showMessageDialog(f, "Time limit must be a positive number", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(f, "Time limit must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    library.borrowBook(currentBook, currentBorrower, timeLimitInt);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(f, "An error has occurred ", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                currentBook = null;
                currentBorrower = null;
                showMenu(f);
            });

            f.add(cancel);
            f.add(borrow);
            f.add(searchForAnotherBook);
            f.add(searchForAnotherBorrower);

            f.setVisible(true);
        }


    }

    /*private void showBorrowingHistory(JFrame f) {

        clear(f);

        JLabel Title = new JLabel("Borrowing history");
        Title.setPreferredSize(new Dimension(1200, 100));
        Title.setFont(new Font("Arial", Font.PLAIN, 50));
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(Title);

        //add a scroll bar
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(1200, 500));
        f.add(scrollPane);


        for(Borrower borrower : library.getBorrowers()){
            JLabel borrowerLabel = new JLabel("Borrower : "+borrower.getName());
            borrowerLabel.setPreferredSize(new Dimension(1200, 50));
            borrowerLabel.setFont(new Font("Arial", Font.PLAIN, 30));
            borrowerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            f.add(borrowerLabel);

            // Création du modèle de table
            DefaultTableModel model = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            model.addColumn("Book");
            model.addColumn("Borrowed on");
            model.addColumn("Return date");
            model.addColumn("Is returned");

            for(BorrowingHistory borrowingHistory : borrower.getBorrowingHistory()){
                model.addRow(new Object[]{borrowingHistory.getBook().getTitle(), borrowingHistory.getBorrowDate(), borrowingHistory.getDueDate(), borrowingHistory.isReturned()? "Yes":"No"});
            }

            // Creating Table
            JTable table = new JTable(model);
            table.setPreferredSize(new Dimension(1000, 100));
            // Création du JScrollPane et ajout de la table
            JScrollPane tableScroller = new JScrollPane(table);
            tableScroller.setPreferredSize(new Dimension(1000, 100));
            f.add(tableScroller);
        }

        backButton(f);

        f.setVisible(true);

    }*/

    private void showBorrowingHistory(JFrame f) {
        clear(f);

        JLabel Title = new JLabel("Borrowing history");
        Title.setPreferredSize(new Dimension(1200, 100));
        Title.setFont(new Font("Arial", Font.PLAIN, 50));
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(Title);

        // Création d'un JPanel pour contenir tous les JScrollPane
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(1000, 500));

        for(Borrower borrower : library.getBorrowers()){
            JLabel borrowerLabel = new JLabel("Borrower : "+borrower.getName());
            borrowerLabel.setPreferredSize(new Dimension(1000, 50));
            borrowerLabel.setFont(new Font("Arial", Font.PLAIN, 30));
            borrowerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(borrowerLabel);

            // Création du modèle de table
            DefaultTableModel model = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            model.addColumn("Book");
            model.addColumn("Borrowed on");
            model.addColumn("Return date");
            model.addColumn("Is returned");

            for(BorrowingHistory borrowingHistory : borrower.getBorrowingHistory()){
                model.addRow(new Object[]{borrowingHistory.getBook().getTitle(), borrowingHistory.getBorrowDate(), borrowingHistory.getDueDate(), borrowingHistory.isReturned()? "Yes":"No"});
            }

            // Creating Table
            JTable table = new JTable(model);
            table.setPreferredSize(new Dimension(800, 100));
            // Création du JScrollPane et ajout de la table
            JScrollPane tableScroller = new JScrollPane(table);
            tableScroller.setPreferredSize(new Dimension(1000, 100));
            panel.add(tableScroller);
        }

        // Création du JScrollPane principal et ajout du panel
        JScrollPane mainScroller = new JScrollPane(panel);
        mainScroller.setPreferredSize(new Dimension(1100, 500));
        f.add(mainScroller);

        backButton(f);

        f.setVisible(true);
    }

    private void returnBook(JFrame f) {

        clear(f);

        JLabel Title = new JLabel("Return a book");
        Title.setPreferredSize(new Dimension(1200, 100));
        Title.setFont(new Font("Arial", Font.PLAIN, 50));
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(Title);

        //show all book not returned
        List<Book> booksToBeReturned = new ArrayList<>();
        for(Borrower borrower : library.getBorrowers()){
            booksToBeReturned.addAll(borrower.getBookToBeReturned());
        }

        if(booksToBeReturned.isEmpty()){
            JOptionPane.showMessageDialog(f, "No book to be returned", "Error", JOptionPane.ERROR_MESSAGE);
            showMenu(f);
        }
        else{
            JLabel explain = new JLabel("Double click on a book to return it");
            explain.setPreferredSize(new Dimension(1200, 50));
            explain.setHorizontalAlignment(SwingConstants.CENTER);
            f.add(explain);

            // Création du modèle de table
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {

                    return false;
                }
            };

            model.addColumn("Title");
            model.addColumn("Author");
            model.addColumn("Year");
            model.addColumn("Genre");

            // Ajout des livres au modèle
            for (Book book : booksToBeReturned) {
                model.addRow(new Object[]{book.getTitle(), book.getAuthor(),book.getPublicationYear(), book.getGenre()});
            }

            // Creating Table
            JTable table = new JTable(model);

            // Add double click listener
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) { // Double-click
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) { // row selected
                            String selectedBookTitle = (String) model.getValueAt(selectedRow, 0);
                            for (Book book : booksToBeReturned) {
                                if (book.getTitle().equals(selectedBookTitle)) {
                                    currentBook = book; // Définition du livre sélectionné comme currentBook
                                    System.out.println("Double-clicked on " + book.getTitle());
                                    int response = JOptionPane.showConfirmDialog(f, book.getTitle() + " selected, do you want to return it ? ", "return a book", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                    if (response == JOptionPane.YES_OPTION) {

                                        library.returnBook(currentBook);

                                        currentBook = null;
                                        showMenu(f);
                                    }

                                    break;
                                }
                            }
                        }
                    }}});


            table.setPreferredSize(new Dimension(1000, 300));
            // Création du JScrollPane et ajout de la table
            JScrollPane tableScroller = new JScrollPane(table);
            tableScroller.setPreferredSize(new Dimension(1000,300 ));
            f.add(tableScroller);

            // Bouton de retour
            backButton(f);
        }
    }

    private void exit(JFrame f) {
        if (persistance != null) {
            persistance.saveLibrary(library);
            f.dispose();
        }
    }

}