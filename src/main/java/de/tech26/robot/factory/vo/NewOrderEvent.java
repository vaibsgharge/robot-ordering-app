package de.tech26.robot.factory.vo;

import de.tech26.robot.factory.data.ComponentData;
import de.tech26.robot.factory.data.StockMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.Map;

@Value
@Getter
@Setter
@Accessors(fluent=true)
@AllArgsConstructor
public class NewOrderEvent {
    private Order newOrder;
    Map<StockMap, ComponentData> stockDataAvailable;
}
