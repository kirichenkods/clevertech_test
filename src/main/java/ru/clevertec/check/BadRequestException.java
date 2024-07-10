package main.java.ru.clevertec.check;

public class BadRequestException extends CustomException {
    private final String MESSAGE_FILE = "BAD REQUEST";
    public BadRequestException() {
        reportException(MESSAGE_FILE);
    }

    public BadRequestException(String messageConsole) {
        reportException(MESSAGE_FILE, messageConsole);
    }
}
