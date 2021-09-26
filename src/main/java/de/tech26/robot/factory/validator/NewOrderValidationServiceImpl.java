package de.tech26.robot.factory.validator;

import de.tech26.robot.factory.data.ComponentData;
import de.tech26.robot.factory.vo.NewOrderEvent;
import de.tech26.robot.factory.vo.Order;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.tech26.robot.factory.data.StockMap.*;
import static de.tech26.robot.factory.validator.ValidationResult.invalid;
import static java.util.stream.Collectors.toList;

@ToString
@Service("newOrderValidationServiceImpl")
@AllArgsConstructor
public class NewOrderValidationServiceImpl implements OrderValidationService {

    @Override
    public ValidationResult validate(NewOrderEvent command) {

        ValidationStep<NewOrderEvent> newOrderValidationStep = new DuplicatePartsValidationStep();
        newOrderValidationStep
                        .linkWith(new FaceAvailabilityValidationStep())
                        .linkWith(new ArmAvailabilityValidationStep())
                        .linkWith(new MaterialAvailabilityValidationStep())
                        .linkWith(new MobilityAvailabilityValidationStep());

        return newOrderValidationStep.verify(command);
    }

    private static class DuplicatePartsValidationStep extends ValidationStep<NewOrderEvent> {

        @Override
        public ValidationResult verify(NewOrderEvent newOrderEvent) {
            Order newOrder = newOrderEvent.newOrder();
            Set<String> robotParts = new HashSet<>(newOrderEvent.newOrder().components());
            return robotParts.size() < newOrder.components().size() ? invalid() : this.checkNext(newOrderEvent);
        }
    }

    @AllArgsConstructor
    private static class FaceAvailabilityValidationStep extends ValidationStep<NewOrderEvent> {
        @Override
        public ValidationResult verify(NewOrderEvent newOrderEvent) {
            return validateForAvailabilityAndSequence(this, newOrderEvent, FACE.getCodes());
        }
    }

    @AllArgsConstructor
    private static class ArmAvailabilityValidationStep extends ValidationStep<NewOrderEvent> {
        @Override
        public ValidationResult verify(NewOrderEvent newOrderEvent) {
            return validateForAvailabilityAndSequence(this, newOrderEvent, ARM.getCodes());
        }
    }

    @AllArgsConstructor
    private static class MobilityAvailabilityValidationStep extends ValidationStep<NewOrderEvent> {
        @Override
        public ValidationResult verify(NewOrderEvent newOrderEvent) {
            return validateForAvailabilityAndSequence(this, newOrderEvent, MOB.getCodes());
        }
    }

    @AllArgsConstructor
    private static class MaterialAvailabilityValidationStep extends ValidationStep<NewOrderEvent> {
        @Override
        public ValidationResult verify(NewOrderEvent newOrderEvent) {
            return validateForAvailabilityAndSequence(this, newOrderEvent, MAT.getCodes());
        }
    }

    private static ValidationResult validateForAvailabilityAndSequence(ValidationStep<NewOrderEvent> validationStep,
                                                                       NewOrderEvent newOrderEvent, List<String> bodyPartCodes) {

        List<String> bodyPartsOrdered = newOrderEvent.newOrder().components();
        List<String> common = bodyPartsOrdered.stream()
                            .filter(bodyPartCodes::contains)
                            .collect(toList());

        boolean isValidOrder = common.size() == 1 ? true : false;

        if (isValidOrder) {
            ComponentData componentData = newOrderEvent.stockDataAvailable()
                                        .get(valueOf(common.stream().findFirst().get()));
            isValidOrder = componentData.available() > 0;
        }

        return isValidOrder ? validationStep.checkNext(newOrderEvent) : invalid();
    }
}
