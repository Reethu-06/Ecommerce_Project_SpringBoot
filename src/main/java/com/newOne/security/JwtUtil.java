package com.newOne.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Secret key used for signing JWT. It's encoded in Base64 format to ensure safe usage in JWT.
    private static final String SECRET_KEY = Base64.getEncoder().encodeToString("ThisIsA32CharStrongSecretKeyForJWT".getBytes());

    // The expiry time for the token (10 hours).
    private static final long TOKEN_EXPIRY = 1000 * 60 * 60 * 10; // 10 hours

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the username (subject)
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the user ID from the JWT token.
     *
     * @param token the JWT token
     * @return the user ID
     */
    public Integer extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Integer.class));
    }

    /**
     * Extracts the role of the user from the JWT token.
     *
     * @param token the JWT token
     * @return the user's role
     */
    public Integer extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", Integer.class));
    }

    /**
     * Extracts a specific claim from the JWT token using a provided claim resolver.
     *
     * @param token the JWT token
     * @param claimsResolver function to resolve the claim from the JWT
     * @param <T> the type of the claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);  // Extracts all claims from the token
        return claimsResolver.apply(claims);  // Applies the provided function to get a specific claim
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @return the claims extracted from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())  // Sets the signing key to verify the token's integrity
                .build()
                .parseClaimsJws(token)  // Parses the JWT token
                .getBody();  // Returns the body of the token containing the claims
    }

    /**
     * Generates a JWT token using username, userId, and role as claims.
     *
     * @param username the username of the user
     * @param userId the user ID
     * @param role the role of the user
     * @return the generated JWT token
     */
    public String generateToken(String username, Integer userId, Integer role) {
        return Jwts.builder()
                .setSubject(username)  // Sets the subject (username) of the token
                .claim("userId", userId)  // Adds the user ID as a custom claim
                .claim("role", role)  // Adds the user's role as a custom claim
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Sets the token's issued time
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRY))  // Sets the token's expiration time
                .signWith(getSignKey(), SignatureAlgorithm.HS256)  // Signs the token with the signing key and the specified algorithm
                .compact();  // Returns the compact JWT token string
    }

    /**
     * Validates the JWT token by checking the username and expiry.
     *
     * @param token the JWT token
     * @param username the username to validate against
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token, String username) {
        try {
            return extractUsername(token).equals(username) && !isTokenExpired(token);  // Checks if username matches and token is not expired
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired.");  // Catches expired token error
        } catch (Exception e) {
            throw new RuntimeException("Invalid token.");  // Catches any other error related to the token
        }
    }

    /**
     * Checks whether the JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());  // Compares the token's expiration date with the current date
    }

    /**
     * Provides the signing key used to validate the JWT's signature.
     *
     * @return the signing key
     */
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));  // Decodes the secret key and returns it as the signing key
    }
}
