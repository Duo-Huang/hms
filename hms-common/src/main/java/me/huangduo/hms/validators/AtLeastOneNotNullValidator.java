package me.huangduo.hms.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.annotations.AtLeastOneNotNull;

import java.lang.reflect.Field;

@Slf4j
public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        for (Field field : value.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(value) != null) {
                    return true;
                }
            } catch (IllegalAccessException e) {
               log.error("AtLeastOneNotNullValidator validate failed", e);
            }
        }

        return false;
    }
}
