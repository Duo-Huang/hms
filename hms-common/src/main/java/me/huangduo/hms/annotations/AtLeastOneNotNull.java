package me.huangduo.hms.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import me.huangduo.hms.validators.AtLeastOneNotNullValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneNotNullValidator.class)
public @interface AtLeastOneNotNull {
    String message() default "At least one field must be not null.";

    Class<?>[] groups() default {}; // 分组校验

    Class<? extends Payload>[] payload() default {};
}
