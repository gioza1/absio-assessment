package com.sample.util;

import com.sample.exception.UserNotAuthenticatedException;
import com.sample.filters.server.RequestHolderFilter;
import com.sample.session.UserSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Component
public class SessionUtils {
    private final int maxInactiveInterval;
    private final ThreadLocal<HttpServletRequest> requestHolder;
    private final String sessionKey;

    public SessionUtils(@Value("${session.maxSessionMinutes:#{null}}") Integer maxSessionMinutes, @Value("${application.session.key:#{null}}") String sessionKey) {
        Objects.requireNonNull(maxSessionMinutes);
        assert maxSessionMinutes > 0;
        maxInactiveInterval = maxSessionMinutes * 60;
        this.sessionKey = Objects.requireNonNull(sessionKey);
        requestHolder = RequestHolderFilter.HTTP_SERVLET_REQUEST_HOLDER;
    }

    public HttpSession getSession() {
        return getSession(true);
    }

    public HttpSession getSession(boolean create) {
        HttpServletRequest request = requestHolder.get();
        HttpSession session = request.getSession(false);

        if (create && session == null) {
            session = request.getSession(true);
            session.setMaxInactiveInterval(maxInactiveInterval);
        }

        return session;
    }

    public void invalidateUserSession() {
        HttpSession session = getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public UserSession validateUserSession() {
        // Ensure user in session
        HttpSession session = getSession(false);

        if (session == null) {
            throw new UserNotAuthenticatedException("There is no session!");
        }

        UserSession userSession = (UserSession)session.getAttribute(sessionKey);
        // If there is not a user, it is not authenticated.
        if (userSession == null) {
            throw new UserNotAuthenticatedException("User is not Authenticated!");
        }

        return userSession;
    }
}
