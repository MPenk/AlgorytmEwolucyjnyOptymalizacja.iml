package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperations {
    /**
     * Tworzenie pliku
     * @param fileName Nazwa pliku.
     */
    static public void createFile(String fileName){
        try {
            File myFile = new File(fileName+".txt");
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Zapisywanie tablicy do pliku o określonej nazwie
     * @param fileName Nazwa pliku
     * @param tab Yabela z której pobrane będą dane
     */
    static public void saveToFile(String fileName, double tab[][], GAProperties gaProperties){
        try {
            FileWriter myWriter = new FileWriter(fileName+".txt");
            myWriter.write("Wyniki pomiarów;\n");
            for (int i = 0; i < gaProperties.getGenerations(); i++) {
                myWriter.write(tab[gaProperties.getRepetitions()][i]+";\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
