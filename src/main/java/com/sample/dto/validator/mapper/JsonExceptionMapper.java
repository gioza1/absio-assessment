package com.sample.dto.validator.mapper;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.sample.dto.ErrorCode;
import com.sample.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@Slf4j
public class JsonExceptionMapper implements ExceptionMapper<JsonMappingException> {

    public Response toResponse(JsonMappingException e) {
        String message = "There was an error parsing JSON: " + e.getMessage();
        log.error(message, e);
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.JSON_ERROR, message);
        return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
    }
}
