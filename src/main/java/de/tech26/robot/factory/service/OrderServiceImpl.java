package de.tech26.robot.factory.service;

import de.tech26.robot.factory.data.ComponentData;
import de.tech26.robot.factory.data.StockMap;
import de.tech26.robot.factory.validator.OrderValidationService;
import de.tech26.robot.factory.validator.ValidationResult;
import de.tech26.robot.factory.vo.NewOrderEvent;
import de.tech26.robot.factory.vo.Order;
import de.tech26.robot.factory.vo.OrderStatus;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@ToString
@Service("orderServiceImpl")
public class OrderServiceImpl implements OrderService {

    private static final AtomicLong sequence
            = new AtomicLong(currentTimeMillis() / 1000);

    private final OrderValidationService orderValidationService;

    public OrderServiceImpl(OrderValidationService orderValidationService) {
        this.orderValidationService = orderValidationService;
    }

    @Override
    public Optional<OrderStatus> processOrder(Order order, Map<StockMap, ComponentData> stockDataAvailable) {
        ValidationResult validationResult = orderValidationService.validate(new NewOrderEvent(order, stockDataAvailable));
        return validationResult.isValid() ? of(this.reserveBodyPartsAndCalculateTotal(order, stockDataAvailable)) : empty();
    }

    private OrderStatus reserveBodyPartsAndCalculateTotal(Order order, Map<StockMap, ComponentData> stockDataAvailable) {
        OrderStatus orderStatus = new OrderStatus();
        for (String component : order.components()) {
            ComponentData componentData = stockDataAvailable.get(StockMap.valueOf(component));
            componentData.available(componentData.available() - 1);

            orderStatus.orderId(valueOf(sequence.incrementAndGet()));
            orderStatus.total(orderStatus.total().add(componentData.price()));
        }
        return orderStatus;
    }
}