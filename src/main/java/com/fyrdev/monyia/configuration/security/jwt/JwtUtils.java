package com.fyrdev.monyia.configuration.security.jwt;

import com.fyrdev.monyia.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.expiration.time}")
    private long jwtExpirationTime;

    @Value("${jwt.secret.key}")
    private String jwtSecret;

    public String generateToken(User user) {
        String email = user.getEmail();
        String userId = user.getId().toString();
        String uuid = user.getUuid();
        String firstName = user.getName().split(" ")[0];

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiresAt = new Date(System.currentTimeMillis() + jwtExpirationTime);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("uuid", uuid)
                .claim("firstName", firstName)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", String.class);
    }

    public String getUuidFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("uuid", String.class);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public SecretKey getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
