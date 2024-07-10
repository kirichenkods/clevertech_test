package main.java.ru.clevertec.check;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

public class Check {
    private LocalDateTime dateTime;
    private DiscountCard discountCard;
    private List<Item> order;
    private List<Product> products;
    private DebitCard debitCard;
    private final double wholesaleDiscount = 10.0;

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Item> getOrder() {
        return order;
    }

    public void setOrder(List<Item> order) {
        this.order = order;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setBalanceDebitCard(DebitCard card) {
        this.debitCard = card;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Product getProductById(int productId) {
        Product product = new Product();
        products.forEach(elem -> {
            if (elem.getId() == productId) {
                product.setId(elem.getId());
                product.setDescription(elem.getDescription());
                product.setPrice(elem.getPrice());
                product.setQuantityInStock(elem.getQuantityInStock());
                product.setWholesaleProduct(elem.isWholesaleProduct());
            }
        });

        return product;
    }

    public double getTotalPrice() throws BadRequestException {
        double totalPrice = 0.0;
        for (Item item : order) {
            Product product = getProductById(item.getProductId());
            if (product.getQuantityInStock() < item.getQty()) {
                throw new BadRequestException("не достаточно товара "
                        + product.getDescription() + " на остатках");
            }
            totalPrice += (product.getPrice() * item.getQty());
        }

        return totalPrice;
    }

    public double getTotalDiscount() {
        double totalDiscount = 0.0;
        for (Item item : order) {
            totalDiscount += getDiscountForItem(item, getProductById(item.getProductId()));
        }

        return totalDiscount;
    }

    public double getDiscountForItem(Item item, Product product) {
        double result = 0.0;
        if (product.isWholesaleProduct() && item.getQty() >= 5) {
            result = item.getQty() * product.getPrice() / wholesaleDiscount;
        } else {
            result = item.getQty() * product.getPrice() * (discountCard.getDiscountAmount() / 100.0);
        }

        BigDecimal resScale = new BigDecimal(result);
        resScale = resScale.setScale(2, RoundingMode.DOWN);


        return resScale.doubleValue();
    }

    public void checkingMoneyToBuy() throws NotEnoughMoneyException, BadRequestException {
        if ((getTotalPrice() - getTotalDiscount()) > debitCard.getBalance()) {
            throw new NotEnoughMoneyException();
        }
    }

    public interface CheckHeadInfo {
        String date = "Date";
        String time = "Time";
    }

    public interface CheckBodyInfo {
        String QTY = "QTY";
        String DESCRIPTION = "DESCRIPTION";
        String PRICE = "PRICE";
        String DISCOUNT = "DISCOUNT";
        String TOTAL = "TOTAL";
    }

    public interface CheckCardInfo {
        String DISCOUNT_CARD = "DISCOUNT CARD";
        String DISCOUNT_PERCENTAGE = "DISCOUNT PERCENTAGE";
    }

    public interface CheckFooterInfo {
        String TOTAL_PRICE = "TOTAL PRICE";
        String TOTAL_DISCOUNT = "TOTAL DISCOUNT";
        String TOTAL_WITH_DISCOUNT = "TOTAL WITH DISCOUNT";
    }

    public static final class Builder {
        private final Check check;

        private Builder() {
            check = new Check();
        }

        public Builder order(List<Item> order) throws BadRequestException {
            if (order.isEmpty()) {
                throw new BadRequestException();
            }
            check.setOrder(order);
            return this;
        }

        public Builder time(LocalDateTime time) {
            check.setDateTime(time);
            return this;
        }

        public Builder discountCard(DiscountCard card) {
            check.setDiscountCard(card);
            return this;
        }

        public Builder products(List<Product> products) {
            check.setProducts(products);
            return this;
        }

        public Builder balanceDebitCard(DebitCard card) throws BadRequestException {
            if (card == null) {
                throw new BadRequestException();
            }
            check.setBalanceDebitCard(card);
            return this;
        }

        public Check build() {
            try {
                check.checkingMoneyToBuy();
            } catch (NotEnoughMoneyException | BadRequestException e) {
                e.printStackTrace();
            }
            return check;
        }
    }

}
