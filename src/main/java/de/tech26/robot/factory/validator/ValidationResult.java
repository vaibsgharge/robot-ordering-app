package de.tech26.robot.factory.validator;

import lombok.Value;

@Value
public class ValidationResult {

    private final boolean isValid;
    private final String errorMsg;

    public static ValidationResult valid() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult invalid() {
        return new ValidationResult(false, null);
    }

    public static ValidationResult invalid(String errorMsg) {
        return new ValidationResult(false, errorMsg);
    }
}
