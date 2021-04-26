package com.sample.exception.mapper;

import com.sample.dto.common.validator.ErrorCode;
import com.sample.exception.AddressNotFoundException;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class AddressNotFoundExceptionMapper extends BaseExceptionMapper<AddressNotFoundException> {

    @Override
    public Response toResponse(AddressNotFoundException exception) {
        return toResponse(exception, ErrorCode.ADDRESS_NOT_FOUND_ERROR, Response.Status.NOT_FOUND);
    }
}
