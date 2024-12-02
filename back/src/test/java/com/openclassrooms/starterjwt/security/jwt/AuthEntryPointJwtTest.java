package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.MimeTypeUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthEntryPointJwtTest {

    private AuthEntryPointJwt authEntryPointJwt = new AuthEntryPointJwt();

    /**
     * Test de la méthode `commence` qui s'exécute lorsqu'un utilisateur non authentifié
     * tente d'accéder à une ressource protégée.
     * Vérifie que la réponse contient les informations correctes.
     */
    @Test
    void commence_SetsUnauthorizedResponseCorrectly() throws IOException, ServletException {
        // GIVEN
        // Création de requêtes et réponses HTTP fictives
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Exception d'authentification avec un message d'erreur spécifique
        AuthenticationException authException = new AuthenticationException("Unauthorized error message") {};

        // WHEN
        // Appel de la méthode `commence` avec la requête, la réponse, et l'exception d'authentification
        authEntryPointJwt.commence(request, response, authException);

        // THEN
        // Vérification du statut HTTP (401 Unauthorized)
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);

        // Vérification du type de contenu (JSON)
        assertThat(response.getContentType()).isEqualTo(MimeTypeUtils.APPLICATION_JSON_VALUE);

        // Lecture du corps de la réponse et conversion en une Map pour vérification
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseBody = objectMapper.readValue(response.getContentAsString(), Map.class);

        // Vérification du contenu de la réponse JSON
        assertThat(responseBody.get("status")).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED); // Statut attendu
        assertThat(responseBody.get("error")).isEqualTo("Unauthorized"); // Type d'erreur
        assertThat(responseBody.get("message")).isEqualTo("Unauthorized error message"); // Message de l'exception
        assertThat(responseBody.get("path")).isEqualTo(request.getServletPath()); // Chemin de la requête
    }
}
