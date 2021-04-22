package com.sample.exception.mapper;

import com.sample.dto.ErrorCode;
import com.sample.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@Slf4j
public abstract class BaseExceptionMapper<T extends Exception> implements ExceptionMapper<T> {
    protected Response toResponse(T e, ErrorCode errorCode, Response.Status status) {
        return toResponse(e, errorCode, status.getStatusCode());
    }

    protected Response toResponse(T e, ErrorCode errorCode, int status) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode, e.getMessage());
        log.error("We got a mapped exception: " + status + " : " + e.getClass().getName() + " : " + errorResponse, e);
        return Response.status(status).entity(errorResponse).build();
    }
}
