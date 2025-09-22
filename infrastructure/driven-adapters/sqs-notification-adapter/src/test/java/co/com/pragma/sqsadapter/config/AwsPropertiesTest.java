package co.com.pragma.sqsadapter.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AwsPropertiesTest {

    @Test
    void testSetAndGetRegion() {
        AwsProperties props = new AwsProperties();
        props.setRegion("us-east-1");

        assertEquals("us-east-1", props.getRegion());
    }
}