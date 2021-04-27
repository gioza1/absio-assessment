package com.sample.resources;

import com.sample.domain.Address;
import com.sample.dto.address.AddressDto;
import com.sample.service.AddressService;
import com.sample.util.TestUtils;
import org.glassfish.jersey.test.JerseyTestNg;
import org.modelmapper.ModelMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static com.sample.util.TestUtils.buildApplication;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class AddressResourceTest extends JerseyTestNg.ContainerPerClassTest {
    private AddressService addressService;

    private Invocation.Builder buildWithGenericHeaders(WebTarget target) {
        MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
        return target.request().headers(headers);
    }

    @Override
    protected Application configure() {
        addressService = mock(AddressService.class);
        return buildApplication(new AddressResource(new ModelMapper(), addressService, "gateway-session-key"));
    }

    @Test
    void getAddress() {
        ModelMapper mapper = new ModelMapper();
        Address address = TestUtils.createAddress();
        List<AddressDto> dtos = new ArrayList<>();
        List<Address> addresses = new ArrayList<>();
        dtos.add(mapper.map(address, AddressDto.class));
        addresses.add(address);
        when(addressService.getAddressByUserId(0)).thenReturn(addresses);
        WebTarget target = target("/address/0");
        Response response = buildWithGenericHeaders(target).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<AddressDto> dtosBack = response.readEntity(new GenericType<List<AddressDto>>() {
        });
        Assert.assertEquals(dtos, dtosBack);
    }


}
