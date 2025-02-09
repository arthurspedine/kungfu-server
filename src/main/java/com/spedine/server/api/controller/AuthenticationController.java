package com.spedine.server.api.controller;

import com.spedine.server.api.dto.LoginBodyDTO;
import com.spedine.server.infrastructure.CookieName;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager manager;

    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginBodyDTO body, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(body.email(), body.password());
        Authentication authentication = manager.authenticate(authToken);
        String token = tokenService.genToken((User) authentication.getPrincipal());
        Cookie cookie = new Cookie(CookieName.getName(), token);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(604800); // 7 days
        response.addCookie(cookie);

        response.setHeader("Access-Control-Allow-Credentials", "true");
        return ResponseEntity.ok(Map.of("token", token));
    }
}
