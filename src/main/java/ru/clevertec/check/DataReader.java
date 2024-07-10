package main.java.ru.clevertec.check;

import java.util.Map;

public interface DataReader {
    Map<String, String> getDiscountCardByNumber(String cardNumber);
    Map<String, String> getProductById(String id) throws BadRequestException;
}

