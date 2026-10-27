package com.soccorsoweb.rest.security;

import com.soccorsoweb.model.Utente;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Logged
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    private static final String AUTH_SCHEME = "Bearer";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // controllo header che deve cominciare con "Bearer"
        if (authHeader == null || !authHeader.startsWith(AUTH_SCHEME + " ")) {
            abortWithUnauthorized(requestContext);
            return;
        }
        String token = authHeader.substring(AUTH_SCHEME.length()).trim();
        Utente utente = TokenManager.getUtente(token);

        if (token.isEmpty() || utente == null) {
            abortWithUnauthorized(requestContext);
        } else {
            // token e utente in context cosi non devo richiamare ogni volta 
            requestContext.setProperty("validatedToken", token);
            requestContext.setProperty("currentUser", utente);
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"Accesso negato: token mancante o non valido nell'header Authorization\"}")
                        .build());
    }
}
