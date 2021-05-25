package application.filesOperations;

import application.geneticAlgorithm.GAProperties;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperations {

    static String path = "Wyniki/";

    /**
     * Tworzenie pliku
     * @param fileName Nazwa pliku.
     */
    static public void createFile(String fileName){
        try {
            File theDir = new File(path);
            if (!theDir.exists()){
                theDir.mkdirs();
            }
            File myFile = new File(FileOperations.path+fileName+".txt");
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
    static public void saveToFile(String fileName, double tab[], GAProperties gaProperties){
        try {
            FileWriter myWriter = new FileWriter(FileOperations.path+fileName+".txt");
            myWriter.write("Wyniki pomiarów;\n");
            for (int i = 0; i < gaProperties.getGenerations(); i++) {
                myWriter.write(tab[i]+";\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
