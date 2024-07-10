package main.java.ru.clevertec.check;

public class DiscountCard {
    private int id;
    private int number;
    private int discountAmount;

    public DiscountCard(int id, int number, int amount) {
        this.id = id;
        this.number = number;
        this.discountAmount = amount;
    }

    public DiscountCard() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }
}
