package de.tech26.robot.factory.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@AllArgsConstructor
@Accessors(fluent=true)
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Order {

    @JsonProperty
    private List<String> components;

}
