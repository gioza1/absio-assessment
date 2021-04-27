package com.sample.exception.mapper;

import com.sample.dto.common.validator.ErrorCode;
import com.sample.exception.UserDuplicateException;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class UserDuplicateExceptionMapper extends BaseExceptionMapper<UserDuplicateException> {

    @Override
    public Response toResponse(UserDuplicateException e) {
        return toResponse(e, ErrorCode.USER_ALREADY_EXISTS_ERROR, Response.Status.CONFLICT);
    }
}
