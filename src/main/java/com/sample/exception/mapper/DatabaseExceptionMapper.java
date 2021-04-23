package com.sample.exception.mapper;

import com.sample.dto.common.validator.ErrorCode;
import com.sample.exception.DatabaseException;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class DatabaseExceptionMapper extends BaseExceptionMapper<DatabaseException> {
    @Override
    public Response toResponse(DatabaseException e) {
        return toResponse(e, ErrorCode.GENERAL_DATABASE_ERROR, Response.Status.INTERNAL_SERVER_ERROR);
    }
}