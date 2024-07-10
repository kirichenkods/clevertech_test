package main.java.ru.clevertec.check;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataWriterInFile implements DataWriter {
    private final String pathToFile;

    public DataWriterInFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public void createFile() {
        try {
            File file = new File(pathToFile);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла");
            e.printStackTrace();
        }
    }

    public void write(String data) {
        createFile();
        try {
            FileWriter writer = new FileWriter(pathToFile);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл");
            e.printStackTrace();
        }
    }
}
