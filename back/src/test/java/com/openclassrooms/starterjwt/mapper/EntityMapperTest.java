package com.openclassrooms.starterjwt.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityMapperTest {

    // Crée une instance de TestEntityMapper, notre implémentation de EntityMapper utilisée pour les tests
    private TestEntityMapper testEntityMapper;

    // Avant chaque test, initialise testEntityMapper avec une nouvelle instance
    @BeforeEach
    public void setUp() {
        testEntityMapper = new TestEntityMapper();
    }

    @Test
    public void testToEntity() {
        // Prépare un objet DTO de test avec un identifiant et un nom "Harry Potter"
        TestDto dto = new TestDto(1, "Harry Potter");

        // Appelle la méthode toEntity pour convertir le DTO en entité
        TestEntity entity = testEntityMapper.toEntity(dto);

        // Vérifie que l'entité résultante a le même identifiant et nom que le DTO d'origine
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    public void testToDto() {
        // Prépare une entité de test avec un identifiant et un nom "Hermione Granger"
        TestEntity entity = new TestEntity(1, "Hermione Granger");

        // Appelle la méthode toDto pour convertir l'entité en DTO
        TestDto dto = testEntityMapper.toDto(entity);

        // Vérifie que le DTO résultant a le même identifiant et nom que l'entité d'origine
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    public void testToEntityList() {
        List<TestDto> dtoList = Arrays.asList(new TestDto(1, "Ron Weasley"), new TestDto(2, "Albus Dumbledore"));

        List<TestEntity> entityList = testEntityMapper.toEntity(dtoList);

        assertEquals(2, entityList.size());

        assertEquals("Ron Weasley", entityList.get(0).getName());
        assertEquals("Albus Dumbledore", entityList.get(1).getName());
    }

    @Test
    public void testToDtoList() {
        List<TestEntity> entityList = Arrays.asList(new TestEntity(1, "Severus Rogue"), new TestEntity(2, "Rubeus Hagrid"));

        List<TestDto> dtoList = testEntityMapper.toDto(entityList);

        assertEquals(2, dtoList.size());

        assertEquals("Severus Rogue", dtoList.get(0).getName());
        assertEquals("Rubeus Hagrid", dtoList.get(1).getName());
    }

    // Classe de test fictive implémentant EntityMapper pour tester ses méthodes
    private static class TestEntityMapper implements EntityMapper<TestDto, TestEntity> {

        @Override
        public TestEntity toEntity(TestDto dto) {
            // Convertit un TestDto en TestEntity
            return new TestEntity(dto.getId(), dto.getName());
        }

        @Override
        public TestDto toDto(TestEntity entity) {
            // Convertit un TestEntity en TestDto
            return new TestDto(entity.getId(), entity.getName());
        }

        @Override
        public List<TestEntity> toEntity(List<TestDto> dtoList) {
            // Convertit une liste de TestDto en liste de TestEntity
            return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
        }

        @Override
        public List<TestDto> toDto(List<TestEntity> entityList) {
            // Convertit une liste de TestEntity en liste de TestDto
            return entityList.stream().map(this::toDto).collect(Collectors.toList());
        }
    }

    // Classe DTO de test représentant un personnage
    private static class TestDto {
        private final int id;
        private final String name;

        public TestDto(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    // Classe Entité de test représentant un personnage
    private static class TestEntity {
        private final int id;
        private final String name;

        public TestEntity(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
