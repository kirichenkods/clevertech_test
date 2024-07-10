package main.java.ru.clevertec.check;

import java.util.List;

public interface Creator {
    List<Product> createProduct() throws BadRequestException;
    List<Item> createOrder() throws BadRequestException;
    DiscountCard createDiscountCard() throws BadRequestException;
    DebitCard createDebitCard() throws BadRequestException;
}
