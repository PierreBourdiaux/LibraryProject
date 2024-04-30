package org.PierreBourdiaux.dataPersitence;

import org.PierreBourdiaux.Library;

public interface Persistance {

    public static final String FILE_NAME = "data.txt";
    public void saveLibrary(Library library);
    public Library loadLibrary();
}
