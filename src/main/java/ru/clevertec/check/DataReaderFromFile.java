package main.java.ru.clevertec.check;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataReaderFromFile implements DataReader {
    private final String sourcePath;

    public DataReaderFromFile(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public Map<String, String> getDiscountCardByNumber(String cardNumber) {
        String data = getDataFromSource("number", cardNumber);
        Map<String, String> mapData = new HashMap<>();
        if (data.equals("")) {
            return mapData;
        }
        String[] dataValues = data.split(";");
        mapData.put("id", dataValues[0]);
        mapData.put("number", dataValues[1]);
        mapData.put("discountAmount", dataValues[2]);

        return mapData;

    }

    public Map<String, String> getProductById(String id) throws BadRequestException {
        String data = getDataFromSource("id", id);
        if (data.equals("")) {
            throw new BadRequestException("Нет товара с таким id");
        }
        String[] dataValues = data.split(";");
        boolean wholesaleProduct = (dataValues[4].equals("+"));
        Map<String, String> mapData = new HashMap<>();

        mapData.put("id", dataValues[0]);
        mapData.put("description", dataValues[1]);
        mapData.put("price", dataValues[2]);
        mapData.put("quantityInStock", dataValues[3]);
        mapData.put("wholesaleProduct", dataValues[4]);

        return mapData;
    }

    public String getDataFromSource(String key, String value) {
        String data = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(sourcePath))) {
            String line = reader.readLine();
            int numberField = 0;
            if (line != null) {
                String[] fields = line.split(";");
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].equals(key)) {
                        numberField = i;
                        break;
                    }
                }

            }

            line = reader.readLine();
            while (line != null) {
                String[] values = line.split(";");
                if (values[numberField].equals(value)) {
                    data = line;
                    break;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return data;
    }

}
