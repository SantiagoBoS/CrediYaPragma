package co.com.pragma.api.user.mapper;

import co.com.pragma.api.user.dto.UserDTO;
import co.com.pragma.model.user.User;
import co.com.pragma.model.constants.AppMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserDTO dto;
    private User user;
    private LocalDate birthDate;

    @BeforeEach
    void setUp() {
        birthDate = LocalDate.of(1990, 1, 1);
        dto = UserDTO.builder()
                .documentNumber("12345")
                .name("Juan")
                .lastName("Perez")
                .birthDate(birthDate)
                .address("Calle 123")
                .phone("5551234")
                .email("juan@example.com")
                .baseSalary(BigDecimal.valueOf(2000.0))
                .password("secret")
                .role("CLIENT")
                .build();

        user = User.builder()
                .documentNumber(dto.getDocumentNumber())
                .name(dto.getName())
                .lastName(dto.getLastName())
                .birthDate(birthDate)
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .baseSalary(dto.getBaseSalary())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }

    @Test
    void shouldMapDtoToUserEntity() {
        User result = UserMapper.toEntity(dto);

        assertNotNull(result);
        assertEquals("12345", result.getDocumentNumber());
        assertEquals("Juan", result.getName());
        assertEquals("Perez", result.getLastName());
        assertEquals(birthDate, result.getBirthDate());
        assertEquals("Calle 123", result.getAddress());
        assertEquals("5551234", result.getPhone());
        assertEquals("juan@example.com", result.getEmail());
        assertEquals(BigDecimal.valueOf(2000.0), result.getBaseSalary());
        assertEquals("secret", result.getPassword());
        assertEquals("CLIENT", result.getRole());
    }

    @Test
    void shouldReturnNullWhenDtoIsNull() {
        assertNull(UserMapper.toEntity(null));
    }

    @Test
    void shouldMapUserEntityToDto() {
        UserDTO result = UserMapper.toDto(user);

        assertNotNull(result);
        assertEquals("12345", result.getDocumentNumber());
        assertEquals("Juan", result.getName());
        assertEquals("Perez", result.getLastName());
        assertEquals(birthDate, result.getBirthDate());
        assertEquals("Calle 123", result.getAddress());
        assertEquals("5551234", result.getPhone());
        assertEquals("juan@example.com", result.getEmail());
        assertEquals(BigDecimal.valueOf(2000.0), result.getBaseSalary());
        assertEquals("secret", result.getPassword());
        assertEquals("CLIENT", result.getRole());
    }

    @Test
    void shouldReturnNullWhenUserIsNull() {
        assertNull(UserMapper.toDto(null));
    }

    @Test
    void constructorShouldThrowException() {
        Exception exception = assertThrows(UnsupportedOperationException.class, UserMapper::new);
        assertEquals(AppMessages.CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage(), exception.getMessage());
    }
}
