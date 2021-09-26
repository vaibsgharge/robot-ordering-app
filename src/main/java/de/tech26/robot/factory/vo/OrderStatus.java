package de.tech26.robot.factory.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent=true)
@JsonPropertyOrder({ "orderId", "total"})
public class OrderStatus {

    @JsonProperty("order_id")
    private @NonNull String orderId;

    @JsonProperty
    private BigDecimal total = BigDecimal. ZERO;
}
