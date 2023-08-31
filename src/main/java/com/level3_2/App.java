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
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException, SQLException {

        logger.debug("Start program");
        Properties properties = new FileProcessing().loadProperties();
        String uri = properties.getProperty("uri");

        try (MongoClient mongoClient = MongoClients.create(uri)) {
//       try (MongoClient mongoClient = new ConnectionCreator().createConnection()) {
            logger.debug("MongoDB was created");

            MongoDatabase database = mongoClient.getDatabase("myMongoDb");
            logger.debug("DB was got");

            CollectionsCreator collectionsCreator = new CollectionsCreator();
            ProductTypeDAO productTypeDAO = new ProductTypeDAO();
            ShopDAO shopDAO = new ShopDAO();
            ProductsDAO productsDAO = new ProductsDAO();
            ProductsInShopsDAO productsInShopsDAO = new ProductsInShopsDAO(properties);

            collectionsCreator.createCollectionsReference(database);

            shopDAO.insertDataIntoCollection(database);
            productTypeDAO.insertDataIntoCollection(database);

            productsDAO.insertDataIntoCollection(database);

            List<ProductDto> productDtos = productsDAO.getProductsIntoList(database);
            List<String> shops = shopDAO.getShopIntoList(database);

            productsInShopsDAO.insertWithThreads(database, productDtos, shops);
            productsInShopsDAO.findShopByProductType(database);
        }

        logger.info("Program finished");
    }


}
