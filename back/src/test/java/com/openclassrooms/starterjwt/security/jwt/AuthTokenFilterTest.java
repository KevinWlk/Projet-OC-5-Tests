package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTokenFilterTest {

    private final AuthTokenFilter authTokenFilter = new AuthTokenFilter();

    @Test
    void parseJwt_ReturnsToken_WhenAuthorizationHeaderIsValid() throws Exception {
        // GIVEN
        // Création d'une requête HTTP fictive avec un en-tête "Authorization" valide
        MockHttpServletRequest request = new MockHttpServletRequest();
        String validToken = "validToken";
        request.addHeader("Authorization", "Bearer " + validToken);

        // WHEN
        // Utilisation de la réflexion pour accéder à la méthode privée parseJwt
        Method parseJwtMethod = AuthTokenFilter.class.getDeclaredMethod("parseJwt", HttpServletRequest.class);
        parseJwtMethod.setAccessible(true); // Rendre la méthode accessible
        String result = (String) parseJwtMethod.invoke(authTokenFilter, request); // Appel de la méthode avec la requête

        // THEN
        // Vérification que le résultat correspond au token attendu
        assertThat(result).isEqualTo(validToken);
    }

    @Test
    void parseJwt_ReturnsNull_WhenAuthorizationHeaderIsMissing() throws Exception {
        // GIVEN
        // Création d'une requête HTTP fictive sans en-tête "Authorization"
        MockHttpServletRequest request = new MockHttpServletRequest();

        // WHEN
        // Accès et invocation de la méthode parseJwt via réflexion
        Method parseJwtMethod = AuthTokenFilter.class.getDeclaredMethod("parseJwt", HttpServletRequest.class);
        parseJwtMethod.setAccessible(true); // Rendre la méthode accessible
        String result = (String) parseJwtMethod.invoke(authTokenFilter, request); // Appel de la méthode avec la requête

        // THEN
        // Vérification que le résultat est null (absence de token)
        assertThat(result).isNull();
    }

    @Test
    void parseJwt_ReturnsNull_WhenAuthorizationHeaderIsMalformed() throws Exception {
        // GIVEN
        // Création d'une requête HTTP fictive avec un en-tête "Authorization" mal formé
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "InvalidBearerToken"); // En-tête mal formé

        // WHEN
        // Accès et invocation de la méthode parseJwt via réflexion
        Method parseJwtMethod = AuthTokenFilter.class.getDeclaredMethod("parseJwt", HttpServletRequest.class);
        parseJwtMethod.setAccessible(true); // Rendre la méthode accessible
        String result = (String) parseJwtMethod.invoke(authTokenFilter, request); // Appel de la méthode avec la requête

        // THEN
        // Vérification que le résultat est null (en-tête incorrect)
        assertThat(result).isNull();
    }
}
