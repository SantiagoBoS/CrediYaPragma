package co.com.pragma.sqsadapter.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqsPropertiesTest {

    @Test
    void testDefaultValueIsDisabled() {
        SqsProperties props = new SqsProperties();
        assertFalse(props.isEnabled(), "Por defecto debe estar deshabilitado");
    }

    @Test
    void testCanEnableSqsProperty() {
        SqsProperties props = new SqsProperties();
        props.setEnabled(true);
        assertTrue(props.isEnabled(), "Debe permitir habilitar la propiedad");
    }
}
