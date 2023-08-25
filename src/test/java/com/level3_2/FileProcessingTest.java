package com.level3_2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessingTest {
    FileProcessing fileProcessing = new FileProcessing();

    @Test
    void loadPropertiesTest() {
        String expected = "mongoDB";
        String actual = fileProcessing.loadProperties().getProperty("username");

        assertEquals(expected, actual);
    }


}