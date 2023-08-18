package com.level3_2;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CollectionsCreator {

    private static final Logger logger = LoggerFactory.getLogger(CollectionsCreator.class);
    FileProcessing fileProcessing = new FileProcessing();
    private final String COLLECTION_SHOP = "shop";
    private final String COLLECTION_PRODUCT_TYPE = "type";
    private final String COLLECTION_PRODUCTS = "products";
    private final String FILE_SHOPS =  "shop_address.csv";
    private final String FILE_PRODUCTS_TYPE =  "product_type.csv";
    public void createCollectionsReference(MongoDatabase database) throws IOException {
createCollection(database, COLLECTION_SHOP);
        createCollection(database, COLLECTION_PRODUCT_TYPE);
        createCollection(database, COLLECTION_PRODUCTS);
    }
    public void createCollection(MongoDatabase database, String collectionName) throws IOException {
        if (database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
            database.getCollection(collectionName).drop();
            logger.debug("Collection {} dropped", collectionName);
        }
        database.createCollection(collectionName);
        logger.debug("Collection {} created", collectionName);

//       insertDataIntoCollection(filename, collection);

    }



}
