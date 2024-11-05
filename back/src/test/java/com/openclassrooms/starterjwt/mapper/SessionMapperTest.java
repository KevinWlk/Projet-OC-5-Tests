package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SessionMapperTest {

    @Autowired
    private SessionMapper sessionMapper;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        // Réinitialise les mocks avant chaque test pour garantir l'indépendance des tests
        reset(teacherService, userService);
    }

    @Test
    public void testToEntityWithNoTeacherAndNoUsers() {
        // Teste le mappage d'un DTO sans professeur ni utilisateurs
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Session vide");

        Session session = sessionMapper.toEntity(sessionDto);

        // Vérifie que la description est bien mappée et que le professeur et la liste d'utilisateurs sont vides
        assertEquals("Session vide", session.getDescription());
        assertEquals(null, session.getTeacher());
        assertEquals(0, session.getUsers().size());
    }

    @Test
    public void testToDtoWithNoTeacherAndNoUsers() {
        // Teste le mappage d'une entité sans professeur ni utilisateurs
        Session session = new Session();
        session.setDescription("Session vide");

        SessionDto sessionDto = sessionMapper.toDto(session);

        // Vérifie que la description est bien mappée et que l'ID du professeur et la liste d'ID d'utilisateurs sont vides
        assertEquals("Session vide", sessionDto.getDescription());
        assertEquals(null, sessionDto.getTeacher_id());
        assertEquals(0, sessionDto.getUsers().size());
    }

    @Test
    public void testToEntityWithMultipleUsers() {
        // Prépare un DTO avec plusieurs utilisateurs
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Cours de magie");
        sessionDto.setUsers(Arrays.asList(2L, 3L));

        // Simule la récupération des utilisateurs par leurs IDs
        User user1 = new User();
        user1.setId(2L);
        User user2 = new User();
        user2.setId(3L);

        when(userService.findById(2L)).thenReturn(user1);
        when(userService.findById(3L)).thenReturn(user2);

        // Convertit le DTO en entité
        Session session = sessionMapper.toEntity(sessionDto);

        // Vérifie que les utilisateurs ont été correctement mappés
        assertEquals(2, session.getUsers().size());
        assertEquals(2L, session.getUsers().get(0).getId());
        assertEquals(3L, session.getUsers().get(1).getId());
    }

    @Test
    public void testToDtoWithMultipleUsers() {
        // Prépare une entité avec plusieurs utilisateurs
        Session session = new Session();
        session.setDescription("Cours de potions");

        User user1 = new User();
        user1.setId(2L);
        User user2 = new User();
        user2.setId(3L);

        session.setUsers(Arrays.asList(user1, user2));

        // Convertit l'entité en DTO
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Vérifie que les utilisateurs ont été correctement mappés par leurs IDs
        assertEquals(2, sessionDto.getUsers().size());
        assertEquals(2L, sessionDto.getUsers().get(0));
        assertEquals(3L, sessionDto.getUsers().get(1));
    }

    @Test
    public void testToEntityList() {
        // Prépare une liste de DTO avec un seul élément de test
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Cours de botanique");
        sessionDto.setUsers(Collections.singletonList(4L));

        List<SessionDto> dtoList = Collections.singletonList(sessionDto);

        // Simule la récupération de l'utilisateur par son ID
        User user = new User();
        user.setId(4L);
        when(userService.findById(4L)).thenReturn(user);

        // Convertit la liste de DTO en liste d'entités
        List<Session> sessionList = sessionMapper.toEntity(dtoList);

        assertEquals(1, sessionList.size());
        assertEquals("Cours de botanique", sessionList.get(0).getDescription());
        assertEquals(4L, sessionList.get(0).getUsers().get(0).getId());
    }

    @Test
    public void testToDtoList() {
        // Prépare une liste d'entités avec une seule entité de test
        Session session = new Session();
        session.setDescription("Défense contre les forces du mal");

        User user = new User();
        user.setId(5L);
        session.setUsers(Collections.singletonList(user));

        List<Session> sessionList = Collections.singletonList(session);

        // Convertit la liste d'entités en liste de DTO
        List<SessionDto> dtoList = sessionMapper.toDto(sessionList);

        assertEquals(1, dtoList.size());
        assertEquals("Défense contre les forces du mal", dtoList.get(0).getDescription());
        assertEquals(5L, dtoList.get(0).getUsers().get(0));
    }
}
