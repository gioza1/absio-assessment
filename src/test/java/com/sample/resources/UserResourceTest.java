package com.sample.resources;

import com.sample.domain.AuthenticationCredentials;
import com.sample.domain.User;
import com.sample.dto.AuthenticationCredentialsDto;
import com.sample.dto.ChangePasswordDto;
import com.sample.dto.UserDto;
import com.sample.service.UserService;
import com.sample.util.TestUtils;
import org.glassfish.jersey.test.JerseyTestNg;
import org.modelmapper.ModelMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.sample.util.TestUtils.buildApplication;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class UserResourceTest extends JerseyTestNg.ContainerPerClassTest {
    private UserService userService;

    private Invocation.Builder buildWithGenericHeaders(WebTarget target) {
        MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
        return target.request().headers(headers);
    }

    @Override
    protected Application configure() {
        userService = mock(UserService.class);
        return buildApplication(new UserResource(new ModelMapper(), userService, "gateway-session-key"));
    }

    private Response delete() {
        WebTarget target = target("/user/authenticate");
        return buildWithGenericHeaders(target).delete();
    }

    private Response put(AuthenticationCredentialsDto credentialsDto) {
        WebTarget target = target("/user/authenticate");
        return buildWithGenericHeaders(target).put(Entity.entity(credentialsDto, MediaType.APPLICATION_JSON_TYPE));
    }

    private Response put(ChangePasswordDto changePasswordDto) {
        WebTarget target = target("/user/password");
        return buildWithGenericHeaders(target).put(Entity.entity(changePasswordDto, MediaType.APPLICATION_JSON_TYPE));
    }

    @Test
    void testAuthenticate() throws Exception {
        String pwd = "pwd";
        String b64Pwd = Base64.getEncoder().encodeToString(pwd.getBytes());
        AuthenticationCredentialsDto credentialsDto = AuthenticationCredentialsDto.builder()
                                                                                  .password(b64Pwd)
                                                                                  .username("user")
                                                                                  .build();
        Response response = put(credentialsDto);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(userService).authenticate(AuthenticationCredentials.builder()
                                                                  .password(b64Pwd)
                                                                  .username("user")
                                                                  .build());
        reset(userService);
    }

    @Test
    void testChangePassword() throws Exception {
        String pwd = "pwd";
        String b64Pwd = Base64.getEncoder().encodeToString(pwd.getBytes());
        ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
                                                               .newPassword(b64Pwd)
                                                               .username("user")
                                                               .build();
        Response response = put(changePasswordDto);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(userService).updatePassword("user", b64Pwd);
        reset(userService);
    }

    @Test
    void testGet() {
        ModelMapper mapper = new ModelMapper();
        User user = TestUtils.createUser();
        when(userService.getUser(0)).thenReturn(user);
        WebTarget target = target("/user/0");
        Response response = buildWithGenericHeaders(target).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        UserDto back = response.readEntity(UserDto.class);
        Assert.assertEquals(mapper.map(user, UserDto.class), back);
    }

    @Test
    void testGetAll() {
        ModelMapper mapper = new ModelMapper();
        User user = TestUtils.createUser();
        List<UserDto> dtos = new ArrayList<>();
        List<User> users = new ArrayList<>();
        dtos.add(mapper.map(user, UserDto.class));
        users.add(user);
        when(userService.getUsers()).thenReturn(users);
        WebTarget target = target("/user");
        Response response = buildWithGenericHeaders(target).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<UserDto> dtosBack = response.readEntity(new GenericType<List<UserDto>>() {
        });
        Assert.assertEquals(dtos, dtosBack);
    }

    @Test
    void testLogout() throws Exception {
        Response response = delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(userService).logout();
        reset(userService);
    }
}
