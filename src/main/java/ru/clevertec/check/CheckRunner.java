package main.java.ru.clevertec.check;

import java.time.LocalDateTime;
import java.util.List;

public class CheckRunner {
    public static void main(String[] args) throws BadRequestException, InternalServerErrorException {

        if (args.length > 0) {
            Creator creator = new CreatorImpl(args);

            DiscountCard discountCard = creator.createDiscountCard();
            List<Item> order = creator.createOrder();
            List<Product> products = creator.createProduct();
            DebitCard debitCard = creator.createDebitCard();

            Check check = Check.builder()
                    .balanceDebitCard(debitCard)
                    .discountCard(discountCard)
                    .time(LocalDateTime.now())
                    .products(products)
                    .order(order)
                    .build();

            DataWriterInFile writer = new DataWriterInFile(FilePathConstants.PATH_CHECK_FILE);
            Printer checkPrinterToCSV = new PrinterToCSV(check, writer);
            checkPrinterToCSV.print();
            Printer checkPrinterToConsole = new PrinterToConsole(check);
            checkPrinterToConsole.print();

        } else {
            throw new BadRequestException();
        }
    }
}
