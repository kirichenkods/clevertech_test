package main.java.ru.clevertec.check;

public class ErrMessenger {
    public static void DoErrorReport(String message) {
        DataWriterInFile writer = new DataWriterInFile(FilePathConstants.PATH_ERROR_FILE);
        writer.write("ERROR" + "\n" + message);
    }
}
