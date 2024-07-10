package main.java.ru.clevertec.check;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

public class CheckPrinter {
    private final Check check;

    public CheckPrinter(Check check) {
        this.check = check;
    }

    protected String prepareDataToPrint(String delimiter, String specSymPrice) throws BadRequestException {
        StringBuilder builder = new StringBuilder();

        builder.append(getDataForHead(delimiter));
        builder.append(getDataForBody(delimiter, specSymPrice));
        if (check.getDiscountCard().getId() > 0) {
            builder.append(getDataForDiscountCard(delimiter));
        }
        builder.append(getDataForFooter(delimiter, specSymPrice));

        return builder.toString();
    }

    protected String prepareLine (String delimiter, String... elements) {
        StringJoiner joiner = new StringJoiner(delimiter);
        List<String> list = List.of(elements);
        list.forEach(joiner::add);

        return joiner.toString();
    }

    protected String getFormatNumber(double num, String specSymPrice) {
        return String.format("%.2f",num) + specSymPrice;
    }

    private String getDataForFooter(String delimiter, String specSymPrice) throws BadRequestException {
        StringBuilder builder = new StringBuilder();
        builder.append(prepareLine(delimiter,
                        Check.CheckFooterInfo.TOTAL_PRICE,
                        Check.CheckFooterInfo.TOTAL_DISCOUNT,
                        Check.CheckFooterInfo.TOTAL_WITH_DISCOUNT))
                .append("\n");

        builder.append(prepareLine(delimiter,
                getFormatNumber(check.getTotalPrice(), specSymPrice),
                getFormatNumber(check.getTotalDiscount(), specSymPrice),
                getFormatNumber(check.getTotalPrice() - check.getTotalDiscount(), specSymPrice)));

        return builder.toString();
    }

    private String getDataForDiscountCard(String delimiter) {
        StringBuilder builder = new StringBuilder();

        builder.append(prepareLine(delimiter,
                        Check.CheckCardInfo.DISCOUNT_CARD,
                        Check.CheckCardInfo.DISCOUNT_PERCENTAGE))
                .append("\n");

        DiscountCard card = check.getDiscountCard();
        builder.append(prepareLine(delimiter,
                        String.valueOf(card.getNumber()),
                        String.valueOf(card.getDiscountAmount()) + "%"))
                .append("\n")
                .append("\n");

        return builder.toString();
    }

    private String getDataForBody(String delimiter, String specSymPrice) {
        StringBuilder builder = new StringBuilder();

        builder.append(prepareLine(delimiter,
                        Check.CheckBodyInfo.QTY,
                        Check.CheckBodyInfo.DESCRIPTION,
                        Check.CheckBodyInfo.PRICE,
                        Check.CheckBodyInfo.DISCOUNT,
                        Check.CheckBodyInfo.TOTAL))
                .append("\n");

        List<Item> order = check.getOrder();
        order.forEach(item -> {
            Product product = check.getProductById(item.getProductId());
            builder.append(prepareLine(delimiter,
                    String.valueOf(item.getQty()),
                    product.getDescription(),
                    getFormatNumber(product.getPrice(), specSymPrice),
                    getFormatNumber(check.getDiscountForItem(item,product), specSymPrice),
                    getFormatNumber(item.getQty() * product.getPrice(), specSymPrice)
            )).append("\n");
        });
        builder.append("\n");

        return builder.toString();
    }

    private String getDataForHead(String delimiter) {
        StringBuilder builder = new StringBuilder();
        builder.append(prepareLine(delimiter,
                        Check.CheckHeadInfo.date,
                        Check.CheckHeadInfo.time))
                .append("\n");

        LocalDateTime dateTime = check.getDateTime();
        String formatted = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        builder.append(prepareLine(delimiter,
                        dateTime.toLocalDate().toString(),
                        formatted))
                .append("\n")
                .append("\n");

        return builder.toString();
    }
}
