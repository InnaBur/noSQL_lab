package com.level3_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataProcessing {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessing.class);

    public String readOutputFormat() {
        String outputFormat = System.getProperty("type");
        if (outputFormat == null) {
            logger.error("You need to input product type. Now type is Food");
            outputFormat = "Food";
        }
        return outputFormat;
    }
}
