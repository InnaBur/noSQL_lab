package com.level3_2;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionCreator {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionCreator.class);


    public static Connection createConnection() {
        Connection connection = null;
        FileProcessing fileProcessing = new FileProcessing();
        QueriesProcessing queriesProcessing = new QueriesProcessing();
        Properties properties = fileProcessing.loadProperties();

        String dbURL = properties.getProperty("dbURL");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("myMongoDb");

//        try {
//            connection = DriverManager.getConnection(dbURL, username, password);
//            connection.setAutoCommit(false);
//            logger.debug("Connection created");
//
//        } catch (SQLException ex) {
//            logger.error("Connection didn`t create", ex);
//            try {
//                assert connection != null;
//                connection.rollback();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
        return connection;
    }


    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
    }
}
