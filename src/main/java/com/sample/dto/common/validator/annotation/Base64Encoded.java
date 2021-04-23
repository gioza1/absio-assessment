package com.sample.dto.common.validator.annotation;

import com.sample.dto.common.validator.Base64EncodedValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Documented
@Constraint(validatedBy = Base64EncodedValidator.class)
public @interface Base64Encoded {
    Class<?>[] groups() default {};

    String message() default "{base64encoded.invalid}";

    boolean nullValid() default false;

    Class<? extends Payload>[] payload() default {};
}