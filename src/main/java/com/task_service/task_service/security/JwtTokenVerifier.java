package com.task_service.task_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenVerifier {

    private final PublicKeyHolder publicKeyHolder;

    // Validate JWT signature
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(publicKeyHolder.getRsaPublicKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extract all claims
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKeyHolder.getRsaPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Convenience methods
    public Long getUserId(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    public String getUserType(String token) {
        return getClaims(token).get("userType", String.class);
    }
}
