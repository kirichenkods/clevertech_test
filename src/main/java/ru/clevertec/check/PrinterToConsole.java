package main.java.ru.clevertec.check;

public class PrinterToConsole extends CheckPrinter implements Printer {
    @Override
    public void print() throws BadRequestException {
        String specSymPrice = "$";
        String delimiter = ";";
        String data = prepareDataToPrint(delimiter, specSymPrice);
        System.out.println(data);
    }

    public PrinterToConsole(Check check) {
        super(check);
    }
}
