package app.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.BigDecimalValidator;

import java.math.BigDecimal;

public class PriceValidator implements
        ConstraintValidator<PriceConstraint, Object> {

    private final BigDecimalValidator bigDecimalValidator = new BigDecimalValidator();

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return bigDecimalValidator.isValid(value.toString());
    }
}
