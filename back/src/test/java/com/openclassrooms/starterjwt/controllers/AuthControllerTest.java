package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;

import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticateUser_Success() {
        // Préparation des données de test pour la requête de connexion
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        // Simulation de l'authentification réussie avec un utilisateur admin
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .build();

        // Création d'un utilisateur dans le UserRepository avec admin défini à true
        User mockUser = new User("test@example.com", "Doe", "John", "encodedPassword", true);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        // Simulation des interactions de `authenticationManager` et `jwtUtils`
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Simuler la génération d'un JWT
        String token = "mockJwtToken";
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);

        // Exécution de la méthode à tester
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Vérifications sur la réponse
        assertEquals(200, response.getStatusCodeValue(), "Le statut de la réponse doit être 200");
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse, "Le corps de la réponse ne doit pas être null");
        assertEquals("mockJwtToken", jwtResponse.getToken(), "Le jeton JWT doit correspondre au jeton simulé");
        assertEquals(1L, jwtResponse.getId(), "L'ID de l'utilisateur doit être 1");
        assertEquals("test@example.com", jwtResponse.getUsername(), "Le nom d'utilisateur doit être 'test@example.com'");
        assertEquals("John", jwtResponse.getFirstName(), "Le prénom doit être 'John'");
        assertEquals("Doe", jwtResponse.getLastName(), "Le nom doit être 'Doe'");
        assertEquals(true, jwtResponse.getAdmin(), "L'utilisateur doit être admin");

        // Vérifier les appels aux méthodes simulées
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtToken(authentication);
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }
    @Test
    void testRegisterUser_Success() {
        // Préparation des données d'entrée pour un utilisateur unique
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setFirstName("New");
        signupRequest.setLastName("User");
        signupRequest.setPassword("password123");

        // Simule un nouvel email qui n'existe pas encore dans la base de données
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        // Appel de la méthode pour créer un utilisateur
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Vérification de la réponse
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());

        // Vérifie que le mot de passe est bien encodé et que l'utilisateur est sauvegardé
        verify(passwordEncoder, times(1)).encode(signupRequest.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Préparation des données pour un utilisateur existant
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("existinguser@example.com");
        signupRequest.setFirstName("Existing");
        signupRequest.setLastName("User");
        signupRequest.setPassword("password123");

        // Simule un email qui existe déjà dans la base de données
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // Appel de la méthode
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Vérification de la réponse
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error: Email is already taken!", ((MessageResponse) response.getBody()).getMessage());

        // Vérifie que l'utilisateur n'est pas sauvegardé
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}