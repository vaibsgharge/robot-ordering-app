package de.tech26.robot.factory.validator;

import de.tech26.robot.factory.data.ComponentData;
import de.tech26.robot.factory.data.StockMap;
import de.tech26.robot.factory.vo.NewOrderEvent;
import de.tech26.robot.factory.vo.Order;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewOrderValidationServiceImplTest {

    @Qualifier("newOrderValidationServiceImpl")
    @Autowired
    private OrderValidationService newOrderValidationServiceImpl;

    private ComponentData faceComponent;

    private ComponentData armComponent;

    private ComponentData armEComponent;

    private ComponentData materialComponent;

    private ComponentData materialJComponent;

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

        armEComponent = ComponentData
                .builder()
                .price(new BigDecimal(12.39))
                .available(3)
                .build();

        materialComponent = ComponentData
                .builder()
                .price(new BigDecimal(90.12))
                .available(92)
                .build();

        materialJComponent = ComponentData
                .builder()
                .price(new BigDecimal(82.31))
                .available(15)
                .build();

        mobileComponent = ComponentData
                .builder()
                .price(new BigDecimal(30.77))
                .available(2)
                .build();

        stockDataAvailable = new HashMap<>();
        stockDataAvailable.put(StockMap.A, faceComponent);
        stockDataAvailable.put(StockMap.D, armComponent);
        stockDataAvailable.put(StockMap.E, armEComponent);
        stockDataAvailable.put(StockMap.I, materialComponent);
        stockDataAvailable.put(StockMap.J, materialJComponent);
        stockDataAvailable.put(StockMap.F, mobileComponent);
    }

    @Test
    public void test_newOrder_withValidRobotConfiguration() {
        Order order = new Order(Arrays.asList("I", "A", "D", "F"));
        ValidationResult validationResult = newOrderValidationServiceImpl.validate(new NewOrderEvent(order, stockDataAvailable));
        assertTrue(validationResult.isValid());
    }

    @Test
    public void test_newOrder_withInValidRobotConfiguration() {
        Order order = new Order(Arrays.asList("I", "A", "D", "D"));
        ValidationResult validationResult = newOrderValidationServiceImpl.validate(new NewOrderEvent(order, stockDataAvailable));
        assertFalse(validationResult.isValid());
    }

    @Test
    public void test_newOrder_withFacePartTwice() {
        Order order = new Order(Arrays.asList("I", "A", "B", "D"));
        ValidationResult validationResult = newOrderValidationServiceImpl.validate(new NewOrderEvent(order, stockDataAvailable));
        assertFalse(validationResult.isValid());
    }

    @Test
    public void test_newOrder_withArmPartTwice() {
        Order order = new Order(Arrays.asList("A", "D", "E", "H"));
        ValidationResult validationResult = newOrderValidationServiceImpl.validate(new NewOrderEvent(order, stockDataAvailable));
        assertFalse(validationResult.isValid());
    }

    @Test
    public void test_newOrder_withMobilityPartTwice() {
        Order order = new Order(Arrays.asList("A", "D", "F", "H"));
        ValidationResult validationResult = newOrderValidationServiceImpl.validate(new NewOrderEvent(order, stockDataAvailable));
        assertFalse(validationResult.isValid());
    }

    @Test
    public void test_newOrder_withMaterialPartTwice() {
        Order order = new Order(Arrays.asList("A", "D", "I", "J"));
        ValidationResult validationResult = newOrderValidationServiceImpl.validate(new NewOrderEvent(order, stockDataAvailable));
        assertFalse(validationResult.isValid());
    }
}
