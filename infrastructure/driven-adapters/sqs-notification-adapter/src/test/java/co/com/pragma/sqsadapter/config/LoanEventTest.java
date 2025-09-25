package co.com.pragma.sqsadapter.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanEventTest {

    @Test
    void testLoanEventCreationAndAccessors() {
        // Creamos un objeto LoanEvent
        LoanEvent event = new LoanEvent("123", "APPROVED", 5000.0);

        // Verificamos que los getters devuelvan los valores correctos
        assertEquals("123", event.getId());
        assertEquals("APPROVED", event.getStatus());
        assertEquals(5000.0, event.getAmount());

        // Modificamos los valores con setters
        event.setId("456");
        event.setStatus("REJECTED");
        event.setAmount(1000.0);

        // Verificamos que los setters hayan cambiado los valores
        assertEquals("456", event.getId());
        assertEquals("REJECTED", event.getStatus());
        assertEquals(1000.0, event.getAmount());
    }

    @Test
    void testLoanEventNoArgsConstructor() {
        // Probamos que el constructor vacío funcione
        LoanEvent event = new LoanEvent();
        assertNotNull(event);
        assertNull(event.getId());
        assertNull(event.getStatus());
        assertNull(event.getAmount());
    }
}
