package com.spedine.server.infrastructure;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieName {

    @Value("${api.cookie_name}")
    private String name;

    private static String COOKIE_NAME;

    @PostConstruct
    public void init() {
        COOKIE_NAME = name;
    }

    public static String getName() {
        return COOKIE_NAME;
    }
}
