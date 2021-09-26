package de.tech26.robot.factory.validator;

import de.tech26.robot.factory.vo.NewOrderEvent;

public interface OrderValidationService {
    ValidationResult validate(NewOrderEvent command);
}
