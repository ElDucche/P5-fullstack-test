package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class JwtUtilsTest {
    private JwtUtils jwtUtils;
    private final String jwtSecret = "testSecretKey";
    private final int jwtExpirationMs = 10000;

    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils = new JwtUtils();
        // Injecter les valeurs des propriétés
        TestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        TestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    void generateJwtToken_and_getUserNameFromJwtToken_shouldWork() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user1");
        String token = jwtUtils.generateJwtToken(authentication);
        assertThat(token).isNotBlank();
        String username = jwtUtils.getUserNameFromJwtToken(token);
        assertThat(username).isEqualTo("user1");
    }

    @Test
    void validateJwtToken_shouldReturnTrue_forValidToken() {
        String token = Jwts.builder()
                .setSubject("user1")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        boolean valid = jwtUtils.validateJwtToken(token);
        assertThat(valid).isTrue();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forInvalidToken() {
        String invalidToken = "invalid.token.value";
        boolean valid = jwtUtils.validateJwtToken(invalidToken);
        assertThat(valid).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forExpiredToken() {
        String token = Jwts.builder()
                .setSubject("user1")
                .setIssuedAt(new Date(System.currentTimeMillis() - 20000))
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        boolean valid = jwtUtils.validateJwtToken(token);
        assertThat(valid).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forInvalidSignature() {
        String token = Jwts.builder()
                .setSubject("user1")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();
        boolean valid = jwtUtils.validateJwtToken(token);
        assertThat(valid).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forEmptyToken() {
        boolean valid = jwtUtils.validateJwtToken("");
        assertThat(valid).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forUnsupportedToken() {
        String token = Jwts.builder()
                .setSubject("user1")
                .compact(); // No signature
        boolean valid = jwtUtils.validateJwtToken(token);
        assertThat(valid).isFalse();
    }
}
