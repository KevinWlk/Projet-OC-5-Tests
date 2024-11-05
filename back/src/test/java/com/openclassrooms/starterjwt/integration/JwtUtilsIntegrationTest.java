package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtUtilsIntegrationTest {

    @Autowired
    private JwtUtils jwtUtils;

    private String jwtToken;

    @BeforeEach
    public void setUp() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser@example.com")
                .password("password")
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        jwtToken = jwtUtils.generateJwtToken(auth);
    }

    @Test
    public void testGenerateJwtToken() {
        assertThat(jwtToken).isNotNull();
    }

    @Test
    public void testValidateJwtToken() {
        boolean isValid = jwtUtils.validateJwtToken(jwtToken);
        assertThat(isValid).isTrue();
    }

    @Test
    public void testGetUserNameFromJwtToken() {
        String username = jwtUtils.getUserNameFromJwtToken(jwtToken);
        assertThat(username).isEqualTo("testuser@example.com");
    }

    @Test
    public void testValidateInvalidJwtToken() {
        boolean isValid = jwtUtils.validateJwtToken("invalid.token");
        assertThat(isValid).isFalse();
    }

    @Test
    public void testInvalidSignatureJwtToken() {
        // Générer un token avec une signature incorrecte
        String invalidSignatureToken = Jwts.builder()
                .setSubject("testuser@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 10000))
                .signWith(SignatureAlgorithm.HS512, "wrongSecretKey") // Clé secrète incorrecte
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(invalidSignatureToken);
        assertThat(isValid).isFalse();
    }

    @Test
    public void testExpiredJwtToken() {
        // Utilisation d'un JWT expiré en configurant l'expiration dans le passé
        String expiredToken = Jwts.builder()
                .setSubject("testuser@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000)) // Émis dans le passé
                .setExpiration(new Date(System.currentTimeMillis() - 5000)) // Expiré il y a 5 secondes
                .signWith(SignatureAlgorithm.HS512, "your-secret-key")
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(expiredToken);
        assertThat(isValid).isFalse();
    }


    @Test
    public void testUnsupportedJwtToken() {
        // Utiliser un token qui ne correspond pas à une structure JWT valide
        String unsupportedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlckBleGFtcGxlLmNvbSIsImlhdCI6MTYwOTQyODg2NywiZXhwIjoxNjA5NDI4ODcwfQ"; // Header et payload sans signature

        boolean isValid = jwtUtils.validateJwtToken(unsupportedToken);
        assertThat(isValid).isFalse();
    }
    @Test
    public void testUnsupportedAlgoJwtToken() {
        // Créer un jeton avec un algorithme non supporté
        String unsupportedToken = Jwts.builder()
                .setSubject("testuser@example.com")
                .setHeaderParam("alg", "none")
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(unsupportedToken);
        assertThat(isValid).isFalse();
    }


    @Test
    public void testEmptyClaimsJwtToken() {
        // Utiliser un token vide pour déclencher IllegalArgumentException
        boolean isValid = jwtUtils.validateJwtToken("");
        assertThat(isValid).isFalse();
    }
}

