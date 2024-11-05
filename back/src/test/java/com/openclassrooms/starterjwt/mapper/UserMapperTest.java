package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testToEntity() {
        UserDto dto = new UserDto(1L, "albus.dumbledore@example.com", "Dumbledore", "Albus", true, "Alohomora", LocalDateTime.now(), LocalDateTime.now());
        User user = userMapper.toEntity(dto);

        assertEquals("albus.dumbledore@example.com", user.getEmail());
        assertEquals("Dumbledore", user.getLastName());
        assertEquals("Albus", user.getFirstName());
        assertEquals(true, user.isAdmin());
    }

    @Test
    public void testToDto() {
        User user = new User();
        user.setId(1L);
        user.setEmail("albus.dumbledore@example.com");
        user.setLastName("Dumbledore");
        user.setFirstName("Albus");
        user.setPassword("Alohomora");
        user.setAdmin(true);

        UserDto dto = userMapper.toDto(user);

        assertEquals("albus.dumbledore@example.com", dto.getEmail());
        assertEquals("Dumbledore", dto.getLastName());
        assertEquals("Albus", dto.getFirstName());
        assertEquals(true, dto.isAdmin());
    }

    @Test
    public void testToEntityList() {
        UserDto dto1 = new UserDto(1L, "albus.dumbledore@example.com", "Dumbledore", "Albus", true, "Alohomora", LocalDateTime.now(), LocalDateTime.now());
        UserDto dto2 = new UserDto(2L, "minerva.mcgonagall@example.com", "McGonagall", "Minerva", true, "ExpectoPatronum", LocalDateTime.now(), LocalDateTime.now());

        List<UserDto> dtoList = Arrays.asList(dto1, dto2);
        List<User> userList = userMapper.toEntity(dtoList);

        assertNotNull(userList);
        assertEquals(2, userList.size());
        assertEquals("albus.dumbledore@example.com", userList.get(0).getEmail());
        assertEquals("minerva.mcgonagall@example.com", userList.get(1).getEmail());
    }

    @Test
    public void testToDtoList() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("albus.dumbledore@example.com");
        user1.setLastName("Dumbledore");
        user1.setFirstName("Albus");
        user1.setPassword("Alohomora");
        user1.setAdmin(true);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("minerva.mcgonagall@example.com");
        user2.setLastName("McGonagall");
        user2.setFirstName("Minerva");
        user2.setPassword("ExpectoPatronum");
        user2.setAdmin(true);

        List<User> userList = Arrays.asList(user1, user2);
        List<UserDto> dtoList = userMapper.toDto(userList);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("albus.dumbledore@example.com", dtoList.get(0).getEmail());
        assertEquals("minerva.mcgonagall@example.com", dtoList.get(1).getEmail());
    }
}
