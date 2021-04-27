package com.sample.env;

import org.glassfish.hk2.api.Factory;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpServletRequestFactory implements Factory<HttpServletRequest> {
    @Override
    public HttpServletRequest provide() {
        HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
        HttpSession sessionMock = Mockito.mock(HttpSession.class);
        Mockito.when(mockedRequest.getSession()).thenReturn(sessionMock);
        return mockedRequest;
    }

    @Override
    public void dispose(HttpServletRequest t) {
    }
}