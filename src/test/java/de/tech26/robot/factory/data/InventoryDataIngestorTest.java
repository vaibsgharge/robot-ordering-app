package de.tech26.robot.factory.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InventoryDataIngestorTest {

    @Test
    public void test_readAvailableStockData_checkPartA() throws IOException {
        Map<StockMap, ComponentData> stockDataAvailable = InventoryDataIngestor.readAvailableStockData();
        assertNotNull(stockDataAvailable);
        assertEquals(10, stockDataAvailable.size());
        assertEquals(9, stockDataAvailable.get(StockMap.A).available().intValue());
        assertEquals("10.28", String.valueOf(stockDataAvailable.get(StockMap.A).price()));
    }

    @Test
    public void test_readAvailableStockData_checkPartJ() throws IOException {
        Map<StockMap, ComponentData> stockDataAvailable = InventoryDataIngestor.readAvailableStockData();
        assertNotNull(stockDataAvailable);
        assertEquals(10, stockDataAvailable.size());
        assertEquals(15, stockDataAvailable.get(StockMap.J).available().intValue());
        assertEquals("82.31", String.valueOf((stockDataAvailable.get(StockMap.J).price())));
    }
}
