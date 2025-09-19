package co.com.pragma.model.loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanTypeTest {

    private LoanType baseType;

    @BeforeEach
    void setUp() {
        baseType = LoanType.builder()
                .id(1L)
                .code("PERSONAL")
                .description("Préstamo personal")
                .build();
    }

    @Test
    void shouldCreateLoanTypeWithBuilder() {
        assertEquals(1L, baseType.getId());
        assertEquals("PERSONAL", baseType.getCode());
        assertEquals("Préstamo personal", baseType.getDescription());
    }

    @Test
    void shouldModifyUsingSetters() {
        baseType.setId(2L);
        baseType.setCode("CAR");
        baseType.setDescription("Préstamo para vehículo");

        assertEquals(2L, baseType.getId());
        assertEquals("CAR", baseType.getCode());
        assertEquals("Préstamo para vehículo", baseType.getDescription());
    }

    @Test
    void shouldUseAllArgsConstructor() {
        LoanType type = new LoanType(3L, "MORTGAGE", "Préstamo hipotecario");

        assertEquals(3L, type.getId());
        assertEquals("MORTGAGE", type.getCode());
        assertEquals("Préstamo hipotecario", type.getDescription());
    }

    @Test
    void shouldValidateEqualsAndHashCode() {
        LoanType copy = LoanType.builder()
                .id(1L)
                .code("PERSONAL")
                .description("Préstamo personal")
                .build();

        assertEquals(baseType, copy);
        assertEquals(baseType.hashCode(), copy.hashCode());
    }

    @Test
    void shouldGenerateNonNullToString() {
        String toString = baseType.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("PERSONAL"));
        assertTrue(toString.contains("Préstamo personal"));
    }
}
