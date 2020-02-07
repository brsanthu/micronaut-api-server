package micronaut.api.server;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationUserDetailsAdapter;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.filters.SecurityFilter;
import io.micronaut.security.handlers.RedirectingLoginHandler;
import io.micronaut.security.session.SecuritySessionConfiguration;
import io.micronaut.security.session.SessionLoginHandler;
import io.micronaut.security.token.config.TokenConfiguration;
import io.micronaut.session.Session;
import io.micronaut.session.SessionStore;
import io.micronaut.session.http.SessionForRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Primary
@Replaces(SessionLoginHandler.class)
public class AppSessionLoginHandler implements RedirectingLoginHandler {
    protected final SessionStore<Session> sessionStore;
    protected final SecuritySessionConfiguration securitySessionConfiguration;
    private final String rolesKeyName;

    @Inject
    public AppSessionLoginHandler(SecuritySessionConfiguration securitySessionConfiguration,
                                  SessionStore<Session> sessionStore,
                                  TokenConfiguration tokenConfiguration) {
        this.securitySessionConfiguration = securitySessionConfiguration;
        this.sessionStore = sessionStore;
        this.rolesKeyName = tokenConfiguration.getRolesName();
    }

    @Override
    public HttpResponse loginSuccess(UserDetails userDetails, HttpRequest<?> request) {
        Session session = SessionForRequest.find(request).orElseGet(() -> SessionForRequest.create(sessionStore, request));
        session.put(SecurityFilter.AUTHENTICATION, new AuthenticationUserDetailsAdapter(userDetails, rolesKeyName));
        return HttpResponse.status(HttpStatus.OK).body("{\"message\": \"Logged in successfully\"}");
    }

    @Override
    public HttpResponse loginFailed(AuthenticationFailed authenticationFailed) {
        return HttpResponse.status(HttpStatus.OK).body("{\"errorCode\": \"auth_failed\", \"message\": \"" + authenticationFailed.getMessage() + "\"}");
    }
}
