package com.spedine.server.domain.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.spedine.server.domain.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String genToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("Kung Fu Taishan API")
                    .withSubject(user.getUsername())
                    .withExpiresAt(expirationDate())
                    .withArrayClaim("authorities",
                            user.getAuthorities().stream().map(
                                    GrantedAuthority::getAuthority
                            ).toArray(String[]::new)
                    )
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new JWTCreationException("JWT Token: Generate token", e.getCause());
        }
    }

    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("Kung Fu Taishan API")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("JWT Token: Invalid/Expired!");
        }
    }


    private Instant expirationDate() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03:00"));
    }

}
