package main.java.ru.clevertec.check;

public class Item {
    private final int productId;
    private final int qty;

    public Item(int productId, int qty) {
        this.productId = productId;
        this.qty = qty;
    }

    public int getProductId() {
        return productId;
    }

    public int getQty() {
        return qty;
    }
}
