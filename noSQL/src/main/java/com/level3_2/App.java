package com.level3_2;

import com.level3_2.dao.ProductTypeDAO;
import com.level3_2.dao.ProductsDAO;
import com.level3_2.dao.ShopDAO;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException, SQLException {
        logger.debug("Start program");
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ShopDAO shopDAO = new ShopDAO();
        ProductsDAO productsDAO = new ProductsDAO();
        FileProcessing fileProcessing = new FileProcessing();
        CollectionsCreator collectionsCreator = new CollectionsCreator();
        String url = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(url)) {

            logger.debug("MongoDB was created");
            MongoDatabase database = mongoClient.getDatabase("myMongoDb");
            collectionsCreator.createCollectionsReference(database);
            shopDAO.insertDataIntoCollection(database);
            productTypeDAO.insertDataIntoCollection(database);

            productsDAO.insertDataIntoCollection(database);






        } catch (MongoException e) {
            logger.error("MongoDB was not created");
            System.exit(1);
        }




        logger.info("Program finished");
    }
}
