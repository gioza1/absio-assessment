package com.sample.filters.server;

import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Component
@Provider
@Priority(Priorities.AUTHENTICATION)
public class RequestHolderFilter implements ContainerRequestFilter {
    public static final ThreadLocal<HttpServletRequest> HTTP_SERVLET_REQUEST_HOLDER = new ThreadLocal<>();

    @Context
    private HttpServletRequest httpServletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        HTTP_SERVLET_REQUEST_HOLDER.set(httpServletRequest);
    }
}
