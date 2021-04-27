package com.sample.resources;

import com.codahale.metrics.annotation.Timed;
import com.sample.aspect.annotation.Authenticated;
import com.sample.domain.AuthenticationCredentials;
import com.sample.domain.User;
import com.sample.dto.common.validator.ErrorCodeCodes;
import com.sample.dto.common.validator.ErrorResponse;
import com.sample.dto.user.*;
import com.sample.exception.DatabaseException;
import com.sample.service.UserService;
import com.sample.session.UserSession;
import com.sample.util.ModelMapperUtils;
import com.sample.util.StringUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Objects;

@Component
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "User management")
@Slf4j
public class UserResource {
    private final String sessionKey;
    private final ModelMapper userMapper;
    private final UserService userService;

    @Inject
    public UserResource(ModelMapper userMapper,
                        UserService userService,
                        @Value("${application.session.key:#{null}}") String sessionKey) {
        this.userMapper = Objects.requireNonNull(userMapper);
        this.userService = Objects.requireNonNull(userService);
        this.sessionKey = Objects.requireNonNull(sessionKey);
    }

    @PUT
    @Path("/authenticate")
    @Timed
    @ApiOperation(value = "Authenticates a user.")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "Successfully authenticated. Response body is empty"
            ),
            @ApiResponse(
                    code = 400,
                    message = "UI Not Available",
                    response = ErrorResponse.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "Bad password",
                    response = ErrorResponse.class
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
    public void authenticate(@Valid AuthenticationCredentialsDto credentialsDto) {
        userService.authenticate(userMapper.map(credentialsDto, AuthenticationCredentials.class));
    }

    @PUT
    @Path("/password")
    @Timed
    @Authenticated
    @ApiOperation(value = "Updates a users password")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "Successfully changed password. Response body is empty."
            ),
            @ApiResponse(
                    code = 400,
                    message = "UI Not Available",
                    response = ErrorResponse.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "User not authenticated",
                    response = ErrorResponse.class
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
    public void changePassword(@Context HttpServletRequest request, @Valid ChangePasswordDto changePasswordDto) {
        log.info("changePassword: " + changePasswordDto);
        if (StringUtils.isEmpty(changePasswordDto.getUsername()) && request != null) {
            HttpSession session = request.getSession();
            if (session != null) {
                UserSession userSession = (UserSession) session.getAttribute(sessionKey);
                log.info("Set the username to: " + userSession.getUsername());
                changePasswordDto.setUsername(userSession.getUsername());
            }
        }

        userService.updatePassword(changePasswordDto.getUsername(), changePasswordDto.getNewPassword());
    }

    @GET
    @Path("/{userId}")
    @Timed
    @Authenticated
    @ApiOperation(value = "Returns user by the given ID")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "User instance",
                    response = UserDto.class
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
    public UserDto getUser(@ApiParam(required = true, example = "1") @PathParam("userId") @Valid @NotNull int userId) {
        return userMapper.map(userService.getUser(userId), UserDto.class);
    }

    @GET
    @Timed
    @Authenticated
    @ApiOperation(value = "Returns all users")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Users list",
                    response = UserDto[].class
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
    public List<UserDto> getUsers() {
        return ModelMapperUtils.mapAll(userService.getAllUsers(), UserDto.class, userMapper);
    }

    @PUT
    @Path("/create")
    @Timed
    @ApiOperation(value = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "Successfully created user. Response body is empty."
            ),
            @ApiResponse(
                    code = 400,
                    message = "UI Not Available",
                    response = ErrorResponse.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "User not authenticated",
                    response = ErrorResponse.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "User already exists. Error code: " + ErrorCodeCodes.USER_ALREADY_EXISTS_ERROR_CODE,
                    response = ErrorResponse.class
            ),
            @ApiResponse(
                    code = 500,
                    message = ApiConstants.UNEXPECTED_ERROR_MESSAGE,
                    response = ErrorResponse.class
            )})
    // Anyone can create without auth.
    public void createUser(@Context HttpServletRequest request, @Valid CreateUserDto createUserDto) {
        log.info("CreateUserDto: " + createUserDto);
        userService.createUser(userMapper.map(createUserDto, User.class));
    }

    @PUT
    @Path("/update")
    @Timed
    @Authenticated
    @ApiOperation(value = "Updates user details")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "Successfully modified user. Response body is empty.",
                    response = UserDto.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "UI Not Available",
                    response = ErrorResponse.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "User not authenticated",
                    response = ErrorResponse.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "User does not exist. Error code: " + ErrorCodeCodes.USER_ALREADY_EXISTS_ERROR_CODE,
                    response = ErrorResponse.class
            ),
            @ApiResponse(
                    code = 500,
                    message = ApiConstants.UNEXPECTED_ERROR_MESSAGE,
                    response = ErrorResponse.class
            )})
    // MIC assumption: Auth is required to modify any users
    public void updateUser(@Context HttpServletRequest request, @Valid UpdateUserDto updateUserDto) {
        log.info("Updating user: " + updateUserDto);
        if (!StringUtils.isEmpty(updateUserDto.getUsername()) && request != null) {
            HttpSession session = request.getSession();
            if (session != null) {
                log.info("Set the username to: " + updateUserDto.getUsername());
                userService.updateUser(userMapper.map(updateUserDto, User.class));
            }
        }
    }

    @DELETE
    @Path("/authenticate")
    @Timed
    @Authenticated
    @ApiOperation(value = "Logs out a user.")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "Successfully logged out. Response body is empty"
            ),
            @ApiResponse(
                    code = 400,
                    message = "UI Not Available",
                    response = ErrorResponse.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "User not authenticated",
                    response = ErrorResponse.class
            ),
            @ApiResponse(
                    code = 500,
                    message = ApiConstants.UNEXPECTED_ERROR_MESSAGE,
                    response = ErrorResponse.class
            )})
    public Response logout() {
        userService.logout();
        return Response.noContent().build();
    }
}
