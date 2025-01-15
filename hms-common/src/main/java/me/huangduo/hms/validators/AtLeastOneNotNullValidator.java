package me.huangduo.hms.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.annotations.AtLeastOneNotNull;

import java.lang.reflect.Field;

@Slf4j
public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object == null) {
            return true;
        }

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(object) != null) {
                    return true;
                }
            } catch (IllegalAccessException e) {
               log.error("AtLeastOneNotNullValidator validate failed", e);
            }
        }

        return false;
    }
}
