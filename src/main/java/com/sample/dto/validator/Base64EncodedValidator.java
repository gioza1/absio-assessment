package com.sample.dto.validator;

import com.sample.dto.validator.annotation.Base64Encoded;
import org.apache.commons.codec.binary.Base64;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Base64EncodedValidator implements ConstraintValidator<Base64Encoded, String> {
    private boolean nullValid;

    @Override
    public void initialize(Base64Encoded constraintAnnotation) {
        nullValid = constraintAnnotation.nullValid();
    }

    @Override
    public boolean isValid(String data, ConstraintValidatorContext cvContext) {
        if (data == null) {
            return nullValid;
        }

        return Base64.isBase64(data.getBytes());
    }
}
