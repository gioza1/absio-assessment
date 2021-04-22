package com.sample.aspect;

import com.sample.aspect.annotation.EnsureLogOut;
import com.sample.domain.AuthenticationCredentials;
import com.sample.exception.UserNotAuthenticatedException;
import com.sample.session.UserSession;
import com.sample.util.SessionUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Objects;

@Aspect
@Component
public class AuthenticationAspect {
    private final String sessionKey;
    private final SessionUtils sessionUtils;

    @Inject
    public AuthenticationAspect(@Value("${application.session.key:#{null}}") String sessionKey, SessionUtils sessionUtils) {
        this.sessionKey = Objects.requireNonNull(sessionKey);
        this.sessionUtils = sessionUtils;
    }

    @AfterReturning("execution(* com.sample.service.impl.UserServiceImpl.authenticate(..)) && args(credentials)")
    public void createUserSession(AuthenticationCredentials credentials) {
        HttpSession session = sessionUtils.getSession();
        UserSession userSession = UserSession.builder()
                                             .username(credentials.getUsername())
                                             .created(LocalDateTime.now())
                                             .lastTouched(LocalDateTime.now())
                                             .build();
        session.setAttribute(sessionKey, userSession);
    }

    @Before("@annotation(com.sample.aspect.annotation.Authenticated)")
    public void ensureAuthenticated() {
        // Ensure user in session
        HttpSession session = sessionUtils.getSession(false);

        if (session == null) {
            throw new UserNotAuthenticatedException("Session does not exist.");
        }

        UserSession userSession = (UserSession)session.getAttribute(sessionKey);
        ensureUserInSession(userSession);
    }

    private void ensureUserInSession(UserSession userSession) {
        // If there is not a user, it is not authenticated.
        if (userSession == null) {
            throw new UserNotAuthenticatedException("User is not Authenticated!");
        }
    }

    @AfterReturning("@annotation(ensureLogOut)")
    public void removeUserSession(EnsureLogOut ensureLogOut) {
        if (!ensureLogOut.onlyIfException()) {
            sessionUtils.invalidateUserSession();
        }
    }

    @AfterThrowing("@annotation(ensureLogOut)")
    public void removeUserSessionIfException(EnsureLogOut ensureLogOut) {
        sessionUtils.invalidateUserSession();
    }
}
