package com.level3_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class FileProcessing {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessing.class);
    private static final String PROPERTIES_FILE_NAME = "config.properties";

    public Properties loadProperties() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = findFileByClasspath(PROPERTIES_FILE_NAME);
            assert inputStream != null;
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            properties.load(reader);
            logger.debug("Properties were loaded");
        } catch (IOException e) {
            logger.error("Properties were not loaded ");
        }
        return properties;
    }

    protected InputStream findFileByClasspath(String fileName) throws FileNotFoundException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("File " + fileName + " not found");
        }
        logger.debug("File {} was found", fileName);
        return inputStream;
    }

    public List<String> getDataFromTheFile(String fileName) throws IOException {
        InputStream inputStream = findFileByClasspath(fileName);
        List<String> dataList = new LinkedList<>();
        String line;

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder data = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            data.append(line.trim());

            if (line.trim().endsWith(";")) {
                if (fileName.contains(".csv")) {
                    data = new StringBuilder(data.substring(0, data.length()-1));
                }
                dataList.add(data.toString());
                data = new StringBuilder();

            }
        }
        logger.debug("Data from the {} file was recieved", fileName);
        return dataList;
    }
}

