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
                .interestRate(5.0)
                .automaticValidation(true)
                .build();
    }

    @Test
    void shouldCreateLoanTypeWithBuilder() {
        assertEquals(1L, baseType.getId());
        assertEquals("PERSONAL", baseType.getCode());
        assertEquals("Préstamo personal", baseType.getDescription());
        assertEquals(5.0, baseType.getInterestRate());
        assertTrue(baseType.getAutomaticValidation());
    }

    @Test
    void shouldModifyUsingSetters() {
        baseType.setId(2L);
        baseType.setCode("CAR");
        baseType.setDescription("Préstamo para vehículo");
        baseType.setInterestRate(3.5);
        baseType.setAutomaticValidation(false);

        assertEquals(2L, baseType.getId());
        assertEquals("CAR", baseType.getCode());
        assertEquals("Préstamo para vehículo", baseType.getDescription());
        assertEquals(3.5, baseType.getInterestRate());
        assertFalse(baseType.getAutomaticValidation());
    }

    @Test
    void shouldUseAllArgsConstructor() {
        LoanType type = new LoanType(3L, "MORTGAGE", "Préstamo hipotecario", 6.0, true);

        assertEquals(3L, type.getId());
        assertEquals("MORTGAGE", type.getCode());
        assertEquals("Préstamo hipotecario", type.getDescription());
        assertEquals(6.0, type.getInterestRate());
        assertTrue(type.getAutomaticValidation());
    }

    @Test
    void shouldValidateEqualsAndHashCode() {
        LoanType copy = LoanType.builder()
                .id(1L)
                .code("PERSONAL")
                .description("Préstamo personal")
                .interestRate(5.0)
                .automaticValidation(true)
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
        assertTrue(toString.contains("5.0"));
        assertTrue(toString.contains("true"));
    }
}
