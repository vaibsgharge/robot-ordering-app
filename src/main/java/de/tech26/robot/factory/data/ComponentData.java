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
public class ComponentData {
    private BigDecimal price;
    private Integer available;
}
