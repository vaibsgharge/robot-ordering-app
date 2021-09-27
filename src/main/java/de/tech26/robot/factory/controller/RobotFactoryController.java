package de.tech26.robot.factory.controller;

import de.tech26.robot.factory.data.ComponentData;
import de.tech26.robot.factory.data.InventoryDataIngestor;
import de.tech26.robot.factory.data.StockMap;
import de.tech26.robot.factory.service.OrderServiceImpl;
import de.tech26.robot.factory.vo.Order;
import de.tech26.robot.factory.vo.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.ResponseEntity.status;

@RestController
public class RobotFactoryController {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    private Map<StockMap, ComponentData> inventoryData;

    @EventListener(ApplicationReadyEvent.class)
    public void loadAvailableInventoryData() throws IOException {
        this.inventoryData = InventoryDataIngestor.readAvailableStockData();
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderStatus> placeOrder(@RequestBody Order order) {
        Optional<OrderStatus> orderStatus = orderServiceImpl.processOrder(order, inventoryData);
        return orderStatus.isPresent() ? status(CREATED).body(orderStatus.get()) : status(UNPROCESSABLE_ENTITY).body(null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleNoSuchElementFoundException() {
        return ResponseEntity
                .status(UNPROCESSABLE_ENTITY)
                .body("Unprocessable Entity");
    }
}
