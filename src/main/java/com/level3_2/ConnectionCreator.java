package com.level3_2;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.level3_2.dao.DAO.logger;

public class ConnectionCreator {

    public  static MongoClient createConnection () {
        String template = "mongodb://%s:%s@%s/%s?ssl=true&replicaSet=rs0&readpreference=%s";
        String username = "mongoDB";
        String password = "admin123";
        String db = "myMongoDb";
        String clusterEndpoint = "docdb-2023-08-27-10-58-41.cluster-cgknngoatzj8.eu-central-1.docdb.amazonaws.com:27017";
        String readPreference = "secondaryPreferred";
        String connectionString = String.format(template, username, password, clusterEndpoint, db, readPreference);

        String truststore = "/home/ubuntu/mongo/rds-truststore.jks";
        String truststorePassword = "admin124";

        System.setProperty("javax.net.ssl.trustStore", truststore);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);

        MongoClient mongoClient = MongoClients.create(connectionString);
        logger.info("Mongo client creates");

        return mongoClient;
    }

}
