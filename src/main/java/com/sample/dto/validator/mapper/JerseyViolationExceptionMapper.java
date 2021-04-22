package com.sample.dto.validator.mapper;

import io.dropwizard.jersey.validation.JerseyViolationException;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@Slf4j
public class JerseyViolationExceptionMapper extends BaseConstraintExceptionMapper implements ExceptionMapper<JerseyViolationException> {
    @Override
    public Response toResponse(JerseyViolationException e) {
        return super.toResponse(e);
    }
}
