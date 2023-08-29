package com.level3_2;

import com.mongodb.client.*;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.level3_2.dao.DAO.logger;

public class ConnectionCreator {

    public MongoClient createConnection () {
        String template = "mongodb://%s:%s@%s/%s?ssl=true&replicaSet=rs0&readpreference=%s";

        String username = "mongoDB";
        String password = "admin123";
        String db = "myMongoDb";
        String clusterEndpoint = "docdb-2023-08-27-10-58-41.cgknngoatzj8.eu-central-1.docdb.amazonaws.com:27017";
        String readPreference = "secondaryPreferred";
        String connectionString = String.format(template, username, password, clusterEndpoint, db, readPreference);

        String truststore = "/home/ubuntu/mongo/rds-truststore.jks";
        String truststorePassword = "adminPass";
        System.setProperty("javax.net.ssl.trustStore", truststore);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);

        MongoClient mongoClient = MongoClients.create(connectionString);

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
