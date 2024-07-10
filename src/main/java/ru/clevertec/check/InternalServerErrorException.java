package main.java.ru.clevertec.check;

public class InternalServerErrorException extends CustomException {
    private final String MESSAGE_FILE = "INTERNAL SERVER ERROR";
    public InternalServerErrorException() {
        reportException(MESSAGE_FILE);
    }

    public InternalServerErrorException(String messageConsole) {
        reportException(MESSAGE_FILE, messageConsole);
    }
}
