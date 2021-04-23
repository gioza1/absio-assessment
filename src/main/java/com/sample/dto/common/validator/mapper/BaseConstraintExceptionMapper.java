package com.sample.dto.common.validator.mapper;

import com.sample.dto.common.validator.ErrorCode;
import com.sample.dto.common.validator.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;

@Slf4j
public class BaseConstraintExceptionMapper {
    protected String prepareAndLogMessage(ConstraintViolationException exception) {
        StringBuilder builder = new StringBuilder("There was a parameter validation error:");
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            builder.append("\tProperty Path: ");
            builder.append(cv.getPropertyPath());
            builder.append("\t| Message: ");
            builder.append(cv.getMessage());
            builder.append("\n");
        }

        String message = builder.toString();
        log.error(message);

        return message;
    }

    public Response toResponse(ConstraintViolationException e) {
        if (e.getConstraintViolations() != null) {
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.GENERAL_PARAMETER_ERROR, prepareAndLogMessage(e));
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
