package main.java.ru.clevertec.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CreatorImpl implements Creator {
    private final String[] args;
    private final int DEFAULT_DISCOUNT = 2;
//    private DataReader dataReader;

    public CreatorImpl(String[] args) {
        this.args = args;
    }

    @Override
    public List<Product> createProduct() throws BadRequestException {
        DataReader dataReader = new DataReaderFromFile(FilePathConstants.PATH_PRODUCTS);
        return createProduct(dataReader);
    }

    public List<Product> createProduct(DataReader dataReader) throws BadRequestException {
        List<Product> products = new ArrayList<>();
        Map<Integer, Integer> order = getOrderFromArgs("-");
        for (Map.Entry<Integer, Integer> entry : order.entrySet()) {
            Integer key = entry.getKey();
            Map<String, String> mapData = dataReader.getProductById(key.toString());

            Product product = new Product();
            product.setId(Integer.parseInt(mapData.get("id")));
            product.setDescription(mapData.get("description"));
            product.setPrice(Double.parseDouble(mapData.get("price").replace(',','.')));
            product.setQuantityInStock(Integer.parseInt(mapData.get("quantityInStock")));
            product.setWholesaleProduct(mapData.get("wholesaleProduct").equals("+"));
            products.add(product);
        }

        return products;
    }

    @Override
    public List<Item> createOrder() throws BadRequestException {
        Map<Integer, Integer> orderData = getOrderFromArgs("-");
        if (orderData.isEmpty()) {
            throw new BadRequestException();
        }

        List<Item> order = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : orderData.entrySet()) {
            Integer key = entry.getKey();
            Integer val = entry.getValue();
            Item item = new Item(key, val);
            order.add(item);
        }

        return order;
    }

    @Override
    public DiscountCard createDiscountCard() throws BadRequestException {
        DataReader dataReader = new DataReaderFromFile(FilePathConstants.PATH_DISCOUNT_CARDS);
        return createDiscountCard(dataReader);
    }

    public DiscountCard createDiscountCard(DataReader dataReader) throws BadRequestException {
        String discountCardNumber = getNumberDiscountCard();
        String regex = "\\d{4}";
        if (!Pattern.matches(regex, discountCardNumber)) {
            throw new BadRequestException();
        }
        Map<String, String> data = dataReader.getDiscountCardByNumber(discountCardNumber);
        if (data.isEmpty()) {
            DiscountCard card = new DiscountCard();
            card.setNumber(Integer.parseInt(discountCardNumber));
            card.setDiscountAmount(DEFAULT_DISCOUNT);
            return card;
        }

        return new DiscountCard(Integer.parseInt(data.get("id")),
                Integer.parseInt(data.get("number")),
                Integer.parseInt(data.get("discountAmount")));
    }

    @Override
    public DebitCard createDebitCard() throws BadRequestException {
        double balanceDebitCard = getBalanceDebitCard();
        return new DebitCard(balanceDebitCard);
    }

    private double getBalanceDebitCard() throws BadRequestException {
        String balance = getDataFromArgs("=", ArgType.balanceDebitCard);
        if (balance.equals("")) {
            throw new BadRequestException();
        }

        return Double.parseDouble(balance);
    }

    private String getNumberDiscountCard() {
        return getDataFromArgs("=", ArgType.discountCard);
    }

    private String getDataFromArgs(String sym, ArgType type) {
        for (String arg : args) {
            if (arg.contains(sym)) {
                String[] keyVal = arg.split(sym);
                String key = keyVal[0];
                String val = keyVal[1];
                if (key.equals(type.toString())) {
                    return val;
                }
            }
        }
        return "";
    }

    private Map<Integer, Integer> getOrderFromArgs(String sym) {
        Map<Integer, Integer> mapOrder = new HashMap<>();
        for (String arg : args) {
            if (arg.contains(sym) && !arg.contains("=")) {
                String[] keyVal = arg.split("-");
                int key = Integer.parseInt(keyVal[0]);
                int val = Integer.parseInt(keyVal[1]);
                int totalQuantity = mapOrder.getOrDefault(key, 0) + val;
                mapOrder.put(key, totalQuantity);
            }
        }

        return mapOrder;
    }
}

