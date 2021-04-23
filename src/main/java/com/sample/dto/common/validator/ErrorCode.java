package com.sample.dto.common.validator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

public enum ErrorCode {

    GENERAL_DATABASE_ERROR(ErrorCodeCodes.GENERAL_DATABASE_ERROR_CODE),
    GENERAL_PARAMETER_ERROR(ErrorCodeCodes.GENERAL_PARAMETER_ERROR_CODE),
    JSON_ERROR(ErrorCodeCodes.JSON_ERROR_CODE),
    USER_ALREADY_EXISTS_ERROR(ErrorCodeCodes.USER_ALREADY_EXISTS_ERROR_CODE),
    USER_CREDENTIALS_ERROR(ErrorCodeCodes.USER_CREDENTIALS_ERROR_CODE),
    USER_DELETE_SELF_ERROR(ErrorCodeCodes.USER_DELETE_SELF_ERROR_CODE),
    USER_IS_NOT_AUTHENTICATED_ERROR(ErrorCodeCodes.USER_IS_NOT_AUTHENTICATED_ERROR_CODE),
    USER_NAME_ALREADY_EXISTS_ERROR(ErrorCodeCodes.USER_NAME_ALREADY_EXISTS_ERROR_CODE),
    USER_NOT_FOUND_ERROR(ErrorCodeCodes.USER_NOT_FOUND_ERROR_CODE);

    private UUID code;

    ErrorCode(String code) {
        this.code = UUID.fromString(code);
    }

    @JsonCreator
    public static ErrorCode fromCode(UUID code) {
        for (ErrorCode nextCode : values()) {
            if (nextCode.code.equals(code)) {
                return nextCode;
            }
        }

        return null;
    }

    @JsonValue
    public UUID getCode() {
        return code;
    }
}
