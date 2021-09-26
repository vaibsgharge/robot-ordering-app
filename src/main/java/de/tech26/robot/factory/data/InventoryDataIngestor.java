package de.tech26.robot.factory.data;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.valueOf;
import static org.springframework.util.ResourceUtils.getFile;

@Component
public class InventoryDataIngestor {

    public static Map<StockMap, ComponentData> readAvailableStockData() throws IOException {

        Map<StockMap, ComponentData> inventoryData = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(getFile("classpath:stock-info.csv")));
        String line = br.readLine();

        while ((line = br.readLine()) != null && !line.isEmpty()) {

            String[] inputFields = line.split(",");

            String code = inputFields[0];
            BigDecimal price = new BigDecimal(inputFields[1]);
            Integer available = valueOf(inputFields[2]);
            ComponentData componentData = ComponentData.builder()
                                            .price(price)
                                            .available(available)
                                            .build();

            inventoryData.put(StockMap.valueOf(code), componentData);
        }
        br.close();
        return inventoryData;
    }
}