package com.healthcare.platform.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service layer responsible for managing JSON Web Tokens (JWT).
 * Handles the generation, parsing, and validation of security credentials for user sessions.
 */
@Service
public class TokenService {

    // Ideally, this secret key should be injected from secure environment properties
    private final String SECRET_KEY = "HealthcarePlatformSuperSecretProductionEncryptionKeyChangeMeInEnv";
    
    // Default token validity set to 24 hours (in milliseconds)
    private final long TOKEN_VALIDITY_MS = 1000 * 60 * 60 * 24;

    /**
     * Extracts the core subject (typically the user's email address) from an active token.
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the explicit expiration timestamp from an active token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts an individual functional claim from the token payloads.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Core Requirement: Generates a signed JWT session credential for an authenticated user.
     * Includes customized role claims matching Admin, Doctor, or Patient scopes.
     * * @param email User identifier used as the token subject
     * @param role The security permissions role ('admin', 'doctor', 'patient')
     * @return Compact serialized JWT string
     */
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Validates an incoming token profile against an authenticated user's credentials.
     * * @param token The extracted JWT string from HTTP Request headers
     * @param userEmail The validated user profile context identifier
     * @return true if the token parameters match and security hashes match
     */
    public Boolean validateToken(String token, String userEmail) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(userEmail) && !isTokenExpired(token));
    }
}
