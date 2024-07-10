package main.java.ru.clevertec.check;

public class PrinterToCSV extends CheckPrinter implements Printer {
    private final DataWriterInFile dataWriterInFile;

    @Override
    public void print() throws BadRequestException {
        dataWriterInFile.createFile();
        String specSymPrice = "$";
        String delimiterCSV = ";";
        String data = prepareDataToPrint(delimiterCSV, specSymPrice);
        dataWriterInFile.write(data);
    }

    public PrinterToCSV(Check check, DataWriterInFile dataWriterInFile) {
        super(check);
        this.dataWriterInFile = dataWriterInFile;
    }

}
