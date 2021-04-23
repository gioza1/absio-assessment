package com.sample.dto.common.validator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ErrorResponse {
    @ApiModelProperty(value = "Error Code",
            required = true,
            example = "bf07e4d4-63af-409d-b374-db2b10d37ba9",
            notes = "See com.sample.dto.common.validator.ErrorCode for possible values.")
    private UUID code;
    @ApiModelProperty(value = "Error message", required = true, example = "The identity record was not found.")
    private String message;

    public ErrorResponse(ErrorCode code, String message) {
        this.code = code.getCode();
        this.message = message;
    }
}
