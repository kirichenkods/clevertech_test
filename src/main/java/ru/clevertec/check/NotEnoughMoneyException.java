package main.java.ru.clevertec.check;

public class NotEnoughMoneyException extends CustomException {
    private final String MESSAGE_FILE = "NOT ENOUGH MONEY";

    public NotEnoughMoneyException() {
        reportException(MESSAGE_FILE);
    }

    public NotEnoughMoneyException(String messageConsole) {
        reportException(MESSAGE_FILE, messageConsole);
    }
}
