package de.tech26.robot.factory.service;

import de.tech26.robot.factory.data.ComponentData;
import de.tech26.robot.factory.data.StockMap;
import de.tech26.robot.factory.vo.Order;
import de.tech26.robot.factory.vo.OrderStatus;

import java.util.Map;
import java.util.Optional;

public interface OrderService {
    Optional<OrderStatus> processOrder(Order order, Map<StockMap, ComponentData> stockDataAvailable);
}
