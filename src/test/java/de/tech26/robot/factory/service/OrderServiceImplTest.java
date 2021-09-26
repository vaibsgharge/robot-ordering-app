package de.tech26.robot.factory.service;

import de.tech26.robot.factory.data.ComponentData;
import de.tech26.robot.factory.data.StockMap;
import de.tech26.robot.factory.vo.Order;
import de.tech26.robot.factory.vo.OrderStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.math.RoundingMode.HALF_DOWN;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Qualifier("orderServiceImpl")
    @Autowired
    private OrderService orderServiceImpl;

    private ComponentData faceComponent;

    private ComponentData armComponent;

    private ComponentData materialComponent;

    private ComponentData mobileComponent;
    private Map<StockMap, ComponentData> stockDataAvailable;

    @Before
    public void prepareTestData() {
        faceComponent = ComponentData
                .builder()
                .price(new BigDecimal(10.28))
                .available(9)
                .build();

        armComponent = ComponentData
                .builder()
                .price(new BigDecimal(28.94))
                .available(1)
                .build();

        materialComponent = ComponentData
                .builder()
                .price(new BigDecimal(90.12))
                .available(92)
                .build();

        mobileComponent = ComponentData
                .builder()
                .price(new BigDecimal(30.77))
                .available(2)
                .build();

        stockDataAvailable = new HashMap<>();
        stockDataAvailable.put(StockMap.A, faceComponent);
        stockDataAvailable.put(StockMap.D, armComponent);
        stockDataAvailable.put(StockMap.I, materialComponent);
        stockDataAvailable.put(StockMap.F, mobileComponent);
    }

    @Test
    public void test_processOrder_withValidRobotConfiguration() {
        Order order = new Order(Arrays.asList("I", "A", "D", "F"));
        Optional<OrderStatus> orderStatus = orderServiceImpl.processOrder(order, stockDataAvailable);
        assertTrue(orderStatus.isPresent());
        assertNotNull(orderStatus.get());
        assertNotNull(orderStatus.get().orderId());
        assertEquals("160.11", String.valueOf(orderStatus.get().total().setScale(2, HALF_DOWN)));
    }

    @Test
    public void test_processOrder_withInValidRobotConfiguration() {
        Order order = new Order(Arrays.asList("I", "A", "D", "D"));
        Optional<OrderStatus> orderStatus = orderServiceImpl.processOrder(order, stockDataAvailable);
        assertFalse(orderStatus.isPresent());
    }
}
