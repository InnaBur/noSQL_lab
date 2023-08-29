package com.level3_2;

import static com.mongodb.client.model.Filters.eq;

import com.level3_2.dao.ProductTypeDAO;
import com.level3_2.dao.ProductsDAO;
import com.level3_2.dao.ProductsInShopsDAO;
import com.level3_2.dao.ShopDAO;
import com.level3_2.dto.ProductDto;
import com.mongodb.MongoException;
import com.mongodb.client.*;
//import com.mongodb.MongoClient;
import org.bson.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;

import java.util.List;
import java.util.Properties;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException, SQLException {

        logger.debug("Start program");
String uri = "mongodb://myMongoDb:admin124@docdb-2023-08-27-10-58-41." +
        "cgknngoatzj8.eu-central-1.docdb.amazonaws.com:27017/?tls=true&tlsCAFile=global-bundle.pem&retryWrites=false";
//        String uri = "mongodb+srv://mongoInna:admin124@cluster0.qg9kdnn.mongodb.net/?retryWrites=true&w=majority";
//        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            logger.debug("MongoDB was created");

            MongoDatabase database = mongoClient.getDatabase("myMongoDb");

            CollectionsCreator collectionsCreator = new CollectionsCreator();
            FileProcessing fileProcessing = new FileProcessing();
            Properties properties = fileProcessing.loadProperties();
            ProductTypeDAO productTypeDAO = new ProductTypeDAO();
            ShopDAO shopDAO = new ShopDAO();
            ProductsDAO productsDAO = new ProductsDAO();
            ProductsInShopsDAO productsInShopsDAO = new ProductsInShopsDAO(properties);

            collectionsCreator.createCollectionsReference(database);
            logger.info("Collections created");

            shopDAO.insertDataIntoCollection(database);
            productTypeDAO.insertDataIntoCollection(database);
            productsDAO.insertDataIntoCollection(database);

            List<ProductDto> productDtos = productsDAO.getProductsIntoList(database);
            List<String> shops = shopDAO.getShopIntoList(database);

            productsInShopsDAO.insertDataIntoCollection(database, productDtos, shops);
            productsInShopsDAO.findShopByProductType(database);
        }

        logger.info("Program finished");
    }
}
