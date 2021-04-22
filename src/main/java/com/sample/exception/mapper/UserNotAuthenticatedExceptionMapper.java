package com.sample.exception.mapper;

import com.sample.dto.ErrorCode;
import com.sample.exception.UserNotAuthenticatedException;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class UserNotAuthenticatedExceptionMapper extends BaseExceptionMapper<UserNotAuthenticatedException> {
    @Override
    public Response toResponse(UserNotAuthenticatedException e) {
        return toResponse(e, ErrorCode.USER_IS_NOT_AUTHENTICATED_ERROR, Response.Status.UNAUTHORIZED);
    }
}
