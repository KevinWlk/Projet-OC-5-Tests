package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class SessionControllerTest {

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_Success() {
        // Préparation des données
        Session session = new Session();
        session.setId(1L);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Exécution de la méthode
        ResponseEntity<?> response = sessionController.findById("1");

        // Vérification
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDto, response.getBody());
        verify(sessionService, times(1)).getById(1L);
        verify(sessionMapper, times(1)).toDto(session);
    }

    @Test
    void testFindById_NotFound() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(404, response.getStatusCodeValue());
        verify(sessionService, times(1)).getById(1L);
    }

    @Test
    void testFindById_InvalidId() {
        ResponseEntity<?> response = sessionController.findById("invalid");

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testFindAll() {
        Session session = new Session();
        session.setId(1L);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        when(sessionService.findAll()).thenReturn(Collections.singletonList(session));
        when(sessionMapper.toDto(any(List.class))).thenReturn(Collections.singletonList(sessionDto));

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        List<SessionDto> sessions = (List<SessionDto>) response.getBody();
        assertNotNull(sessions);
        assertEquals(1, sessions.size());
        assertEquals(sessionDto, sessions.get(0));

        verify(sessionService, times(1)).findAll();
        verify(sessionMapper, times(1)).toDto(any(List.class));
    }

    @Test
    void testCreate() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Class");

        Session session = new Session();
        session.setId(1L);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.create(sessionDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDto, response.getBody());

        verify(sessionService, times(1)).create(session);
        verify(sessionMapper, times(1)).toEntity(sessionDto);
        verify(sessionMapper, times(1)).toDto(session);
    }

    @Test
    void testUpdate_Success() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        Session session = new Session();
        session.setId(1L);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDto, response.getBody());

        verify(sessionService, times(1)).update(1L, session);
        verify(sessionMapper, times(1)).toEntity(sessionDto);
        verify(sessionMapper, times(1)).toDto(session);
    }

    @Test
    void testDelete_Success() {
        Session session = new Session();
        session.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService, times(1)).getById(1L);
        verify(sessionService, times(1)).delete(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(404, response.getStatusCodeValue());
        verify(sessionService, times(1)).getById(1L);
        verify(sessionService, never()).delete(anyLong());

    }

    @Test
    void testParticipate_Success() {
        doNothing().when(sessionService).participate(1L, 2L);

        ResponseEntity<?> response = sessionController.participate("1", "2");

        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService, times(1)).participate(1L, 2L);
    }

    @Test
    void testNoLongerParticipate_Success() {
        doNothing().when(sessionService).noLongerParticipate(1L, 2L);

        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");

        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService, times(1)).noLongerParticipate(1L, 2L);
    }
}
