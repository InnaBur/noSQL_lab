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

//        String truststore = "/home/ubuntu/mongo/rds-truststore.jks";
//        String truststorePassword = "adminPass";
//        System.setProperty("javax.net.ssl.trustStore", truststore);
//        System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);

        MongoClient mongoClient = MongoClients.create(uri);

        logger.info("Mongo client creates");

        return  mongoClient;
    }



//    public static MongoClient createConnectionWithoutTLS () {
//        String template = "mongodb://%s:%s@%s/%s?replicaSet=rs0&readpreference=%s";
//        String username = "mongoDB";
//        String password = "admin123";
//        String db = "myMongoDb";
//        String clusterEndpoint = "docdb-2023-08-27-10-58-41.cluster-cgknngoatzj8.eu-central-1.docdb.amazonaws.com";
//        String readPreference = "secondaryPreferred";
//        String connectionString = String.format(template, username, password, clusterEndpoint, db, readPreference);
//
//        MongoClientURI clientURI = new MongoClientURI(connectionString);
//        MongoClient mongoClient = new MongoClient(clientURI);
//
//
//        return mongoClient;
//    }

}
