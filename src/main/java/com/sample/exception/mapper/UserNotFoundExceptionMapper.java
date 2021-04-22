package com.sample.exception.mapper;

import com.sample.dto.ErrorCode;
import com.sample.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class UserNotFoundExceptionMapper extends BaseExceptionMapper<UserNotFoundException> {
    @Override
    public Response toResponse(UserNotFoundException e) {
        return toResponse(e, ErrorCode.USER_NOT_FOUND_ERROR, Response.Status.NOT_FOUND);
    }
}
