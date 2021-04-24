package com.sample.resources;

import com.codahale.metrics.annotation.Timed;
import com.sample.dto.address.AddressDto;
import com.sample.dto.common.validator.ErrorCodeCodes;
import com.sample.dto.common.validator.ErrorResponse;
import com.sample.service.AddressService;
import com.sample.util.ModelMapperUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Objects;

@Component
@Path("/address")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Address management")
@Slf4j
public class AddressResource {
    private final String sessionKey;
    private final ModelMapper addressMapper;
    private final AddressService addressService;

    @Inject
    public AddressResource(ModelMapper addressMapper,
                           AddressService addressService,
                           @Value("${application.session.key:#{null}}") String sessionKey) {
        this.addressMapper = Objects.requireNonNull(addressMapper);
        this.addressService = Objects.requireNonNull(addressService);
        this.sessionKey = Objects.requireNonNull(sessionKey);
    }

    @GET
    @Path("/{userId}")
    @Timed
    @ApiOperation(value = "Returns user by the given ID")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "User instance",
                    response = AddressDto[].class
            ),
            @ApiResponse(
                    code = 404,
                    message = "User not found. Error code: " + ErrorCodeCodes.USER_NOT_FOUND_ERROR_CODE,
                    response = ErrorResponse.class
            ),
            @ApiResponse(
                    code = 500,
                    message = ApiConstants.UNEXPECTED_ERROR_MESSAGE,
                    response = ErrorResponse.class
            )})
    public List<AddressDto> getAddress(@ApiParam(required = true, example = "1") @PathParam("userId") @Valid @NotNull int userId) {
        return ModelMapperUtils.mapAll(addressService.getAddressByUserId(userId), AddressDto.class, addressMapper);
    }

}
