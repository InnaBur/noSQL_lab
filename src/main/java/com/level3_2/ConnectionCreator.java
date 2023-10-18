package com.level3_2;

import com.mongodb.client.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

public class ConnectionCreator {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionCreator.class);
    public MongoClient createConnection () {
        Properties properties = new FileProcessing().loadProperties();
        String uri = properties.getProperty("uri");

        if (uri.equals("mongodb://myMongoDb:admin..................")) {
            String truststore = "/home/ubuntu/mongo/rds-truststore.jks";
            String truststorePassword = "///";
            System.setProperty("javax.net.ssl.trustStore", truststore);
            System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);
        }
//
        MongoClient mongoClient = MongoClients.create(uri);

        logger.info("Mongo client creates");

        return  mongoClient;
    }



}
