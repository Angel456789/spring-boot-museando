package com.museando.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof DisabledException) {
            getRedirectStrategy().sendRedirect(request, response, "/login?error=blocked");
        } else {
            getRedirectStrategy().sendRedirect(request, response, "/login?error=true");
        }
    }
}
