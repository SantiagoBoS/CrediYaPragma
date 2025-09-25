package co.com.pragma.model.reports;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportResponseTest {

    @Test
    void shouldSetAndGetTotalApprovedLoans() {
        ReportResponse response = new ReportResponse();
        response.setTotalApprovedLoans(10L);

        assertEquals(10L, response.getTotalApprovedLoans());

        // También podemos probar el constructor con parámetros
        ReportResponse response2 = new ReportResponse(20L);
        assertEquals(20L, response2.getTotalApprovedLoans());
    }
}
