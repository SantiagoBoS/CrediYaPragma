package co.com.pragma.sqsadapter.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqsPropertiesTest {

    @Test
    void testDefaultValueIsDisabledAndCapacityQueueUrlIsNull() {
        SqsProperties props = new SqsProperties();
        assertFalse(props.isEnabled(), "Por defecto debe estar deshabilitado");
        assertNull(props.getCapacityQueueUrl(), "Por defecto capacityQueueUrl debe ser null");
    }

    @Test
    void testCanEnableSqsProperty() {
        SqsProperties props = new SqsProperties();
        props.setEnabled(true);
        assertTrue(props.isEnabled(), "Debe permitir habilitar la propiedad");
    }

    @Test
    void testCanSetAndGetCapacityQueueUrl() {
        SqsProperties props = new SqsProperties();
        String url = "https://sqs.aws.com/queue/capacity";
        props.setCapacityQueueUrl(url);
        assertEquals(url, props.getCapacityQueueUrl(), "Debe devolver la URL asignada");
    }
}
