package me.huangduo.hms.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.annotations.AtLeastOneNotNull;
import me.huangduo.hms.annotations.ValidId;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@Slf4j
public class IdValidator implements ConstraintValidator<ValidId, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        if (value instanceof Integer) {
            return (Integer) value > 0;
        } else if (value instanceof String) {
            try {
                int intValue = Integer.parseInt((String) value);
                return intValue > 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }
}
