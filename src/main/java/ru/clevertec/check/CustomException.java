package main.java.ru.clevertec.check;

public class CustomException extends Exception {
    public void reportException(String message) {
        ErrMessenger.DoErrorReport(message);
        printStackTrace();
        throw new RuntimeException(message);
    }

    public void reportException(String messageFile, String messageConsole) {
        ErrMessenger.DoErrorReport(messageFile);
        printStackTrace();
        throw new RuntimeException(messageConsole);
    }
}
