package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Date;
import java.util.Calendar;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SessionControllerIntegrationTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    private MockMvc mockMvc;
    private String jwtToken;
    private Long createdSessionId;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        // Créer un utilisateur de test
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser@example.com")
                .password("password")
                .firstName("First")
                .lastName("Last")
                .admin(false)
                .build();

        // Créer une authentification avec cet utilisateur
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        // Générer le token JWT
        this.jwtToken = jwtUtils.generateJwtToken(auth);

        // Créer et enregistrer un Teacher dans la base de données
        Teacher teacher = new Teacher();
        teacher.setFirstName("Albus");
        teacher.setLastName("Dumbledore");
        Teacher savedTeacher = teacherRepository.save(teacher);

        // Insérer une session dans la base de données
        Session session = new Session();
        session.setName("Yoga Session");
        session.setTeacher(savedTeacher);
        session.setDescription("This is a yoga session description.");

        // Utiliser java.util.Date pour la date
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.NOVEMBER, 10, 10, 0);
        session.setDate(calendar.getTime());

        Session savedSession = sessionRepository.save(session);
        this.createdSessionId = savedSession.getId(); // Sauvegarder l'ID pour le test
    }


    @Test
    public void testFindById_ValidId_ReturnsSession() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/" + createdSessionId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindAll_ReturnsSessionList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateSession_ValidInput_ReturnsCreatedSession() throws Exception {
        String sessionJson = "{\"name\":\"New Yoga Session\", \"teacher_id\": 1, \"description\": \"This is a new yoga session.\", \"date\": \"2024-11-10T10:00:00\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionJson))
                .andExpect(status().isOk());
    }
}
