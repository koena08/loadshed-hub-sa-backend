package com.jumpstart.loadshedhub.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
=======
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

>>>>>>> bce1c30216c5b82041fddfb22f56fd1f90b24ccf
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey key;
<<<<<<< HEAD
    private final long expiration;
    public JwtUtil(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-ms}") long expiration) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.expiration = expiration;
    }
    public String generate(UserDetails user) { Date now = new Date(); return Jwts.builder().subject(user.getUsername()).issuedAt(now).expiration(new Date(now.getTime()+expiration)).signWith(key).compact(); }
    public String email(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject(); }
=======
    private final long expirationMs;

    public JwtUtil(Environment environment) {
        key = Keys.hmacShaKeyFor(environment.getRequiredProperty("app.jwt.secret").getBytes(StandardCharsets.UTF_8));
        expirationMs = environment.getRequiredProperty("app.jwt.expiration-ms", Long.class);
    }

    public String generateToken(UserDetails user) {
        Date now = new Date();
        return Jwts.builder().subject(user.getUsername()).issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs)).signWith(key).compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }
>>>>>>> bce1c30216c5b82041fddfb22f56fd1f90b24ccf
}
