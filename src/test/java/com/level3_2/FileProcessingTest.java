package com.level3_2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileProcessingTest {
    FileProcessing fileProcessing = new FileProcessing();
    Properties propertiesTest = mock(Properties.class);

    void setUp() {

        propertiesTest = new Properties();
        try (InputStream inputStream = DocumentGeneratorTest.class.getResourceAsStream("/test.properties")) {
            propertiesTest.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void loadPropertiesTest() {
        String expected = "mongoDB";
        String actual = fileProcessing.loadProperties().getProperty("username");

        assertEquals(expected, actual);
    }

    @Test
    void loadPropertiesTest2() {
        setUp();
        String expected = "1";

        String actual = propertiesTest.getProperty("quantityProd");

        assertEquals(expected, actual);
    }
}