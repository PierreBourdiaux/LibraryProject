package org.PierreBourdiaux.dataPersitence;

import org.PierreBourdiaux.Library;

import java.io.*;

public class SerialisationPersistance implements Persistance{
    @Override
    public void saveLibrary(Library library) {

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(FILE_NAME);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }
        ObjectOutputStream objectOutputStream
                = null;
        try {
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(library);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.println("Error while saving the library");
        }

        // Close the file
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println("Error while closing the file");
        }


    }
    @Override
    public Library loadLibrary() {
        FileInputStream fileInputStream = null;
        Library library = null;

        try {
            fileInputStream = new FileInputStream(FILE_NAME);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return library;
        }
        ObjectInputStream objectInputStream= null;
        try {
            objectInputStream = new ObjectInputStream(fileInputStream);
            library = (Library) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException e) {
            System.out.println("Error while loading the library");
        } catch (ClassNotFoundException e) {
            System.out.println("Error while loading the library");
        }

        // Close the file
        try {
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("Error while closing the file");
        }

        //set the nextID of the Borrower class
        if(library != null){
            library.setNextID();
        }
        return library;
    }
}
