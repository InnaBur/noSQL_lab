package com.level3_2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataProcessingTest {

    @Test
    void readOutputFormatt() {
        System.setProperty("type", "Food");
        String type = System.getProperty("type");

        assertEquals("Food", type);

    }
    @Test
    void readOutputFormat1() {
        System.setProperty("type", "Meal");

        assertEquals("Meal", new DataProcessing().readOutputFormat());

    }

}