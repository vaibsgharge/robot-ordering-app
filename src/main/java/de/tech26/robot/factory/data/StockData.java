package de.tech26.robot.factory.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Accessors(fluent=true)
public class StockData {
    private final String code;
    private final BigDecimal price;
    private final Integer available;
    private final String partDesc;
}
