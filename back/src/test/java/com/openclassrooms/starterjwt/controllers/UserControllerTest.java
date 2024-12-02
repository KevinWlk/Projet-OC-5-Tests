package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_ValidId_ReturnsUser() {
        User user = new User();
        user.setId(1L);

        UserDto userDto = new UserDto();
        userDto.setId(1L);

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(userMapper, times(1)).toDto(user);
    }


    @Test
    public void testFindById_InvalidIdFormat_ReturnsBadRequest() {
        ResponseEntity<?> response = userController.findById("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).findById(anyLong());
    }

    @Test
    public void testFindById_UserNotFound_ReturnsNotFound() {
        when(userService.findById(2L)).thenReturn(null);

        ResponseEntity<?> response = userController.findById("2");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDelete_ValidIdAndAuthorizedUser_ReturnsOk() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        when(userService.findById(1L)).thenReturn(user);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).delete(1L);
    }

    @Test
    public void testDelete_UnauthorizedUser_ReturnsUnauthorized() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        when(userService.findById(1L)).thenReturn(user);
        when(userDetails.getUsername()).thenReturn("anotheruser@example.com");

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService, never()).delete(1L);
    }

    @Test
    public void testDelete_InvalidIdFormat_ReturnsBadRequest() {
        ResponseEntity<?> response = userController.save("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).findById(anyLong());
    }

    @Test
    public void testDelete_UserNotFound_ReturnsNotFound() {
        when(userService.findById(2L)).thenReturn(null);

        ResponseEntity<?> response = userController.save("2");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, never()).delete(anyLong());
    }
}
