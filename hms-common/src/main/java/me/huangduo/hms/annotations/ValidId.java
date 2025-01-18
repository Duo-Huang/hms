package me.huangduo.hms.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import me.huangduo.hms.validators.IdValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdValidator.class)
public @interface ValidId {
    String message() default "Invalid ID: must be a positive integer";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}