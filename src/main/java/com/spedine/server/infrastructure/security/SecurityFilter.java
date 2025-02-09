package com.spedine.server.infrastructure.security;

import com.spedine.server.infrastructure.CookieName;
import com.spedine.server.domain.repository.UserRepository;
import com.spedine.server.domain.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getCookiesToken(request);
        if (jwt != null) {
            String email = tokenService.getSubject(jwt);
            UserDetails user = userRepository.findByEmail(email);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getCookiesToken(HttpServletRequest request) {
        String cookies = request.getHeader("Cookie");

        if (cookies != null) {
            // SAVING COOKIE NAME AS VARIABLE
            String cookieName = CookieName.getName() + "=";

            String[] cookieArray = cookies.split(";");
            for (String cookie : cookieArray) {
                cookie = cookie.trim();
                if (cookie.startsWith(cookieName)) {
                    return cookie.substring(cookieName.length());
                }
            }
        }

        return null;
    }
}
