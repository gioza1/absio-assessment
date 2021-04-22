package com.sample.exception.mapper;

import com.sample.dto.ErrorCode;
import com.sample.exception.UserCredentialsException;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class UserCredentialsExceptionMapper extends BaseExceptionMapper<UserCredentialsException> {
    @Override
    public Response toResponse(UserCredentialsException e) {
        return toResponse(e, ErrorCode.USER_CREDENTIALS_ERROR, Response.Status.FORBIDDEN);
    }
}
