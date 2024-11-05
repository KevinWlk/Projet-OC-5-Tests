package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    public void testToEntity() {
        TeacherDto dto = new TeacherDto(1L, "Dumbledore", "Albus", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher = teacherMapper.toEntity(dto);

        assertEquals("Dumbledore", teacher.getLastName());
        assertEquals("Albus", teacher.getFirstName());
    }

    @Test
    public void testToDto() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Dumbledore");
        teacher.setFirstName("Albus");

        TeacherDto dto = teacherMapper.toDto(teacher);

        assertEquals("Dumbledore", dto.getLastName());
        assertEquals("Albus", dto.getFirstName());
    }

    @Test
    public void testToEntityList() {
        TeacherDto dto1 = new TeacherDto(1L, "Dumbledore", "Albus", LocalDateTime.now(), LocalDateTime.now());
        TeacherDto dto2 = new TeacherDto(2L, "McGonagall", "Minerva", LocalDateTime.now(), LocalDateTime.now());

        List<TeacherDto> dtoList = Arrays.asList(dto1, dto2);
        List<Teacher> teacherList = teacherMapper.toEntity(dtoList);

        assertNotNull(teacherList);
        assertEquals(2, teacherList.size());
        assertEquals("Dumbledore", teacherList.get(0).getLastName());
        assertEquals("McGonagall", teacherList.get(1).getLastName());
    }

    @Test
    public void testToDtoList() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setLastName("Dumbledore");
        teacher1.setFirstName("Albus");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setLastName("McGonagall");
        teacher2.setFirstName("Minerva");

        List<Teacher> teacherList = Arrays.asList(teacher1, teacher2);
        List<TeacherDto> dtoList = teacherMapper.toDto(teacherList);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("Dumbledore", dtoList.get(0).getLastName());
        assertEquals("McGonagall", dtoList.get(1).getLastName());
    }
}
