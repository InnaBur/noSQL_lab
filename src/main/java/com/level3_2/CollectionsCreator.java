package com.level3_2;

import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CollectionsCreator {

    private static final Logger logger = LoggerFactory.getLogger(CollectionsCreator.class);

    public void createCollectionsReference(MongoDatabase database) {
        String collectionShop = "shop";
        String collectionProductType = "type";
        String collectionProductsInShops = "ProductsInShops";
        String collectionProducts = "products";

        createCollection(database, collectionShop);
        createCollection(database, collectionProductType);
        createCollection(database, collectionProducts);
        createCollection(database, collectionProductsInShops);
        logger.info("Collections created");
    }

    public void createCollection(MongoDatabase database, String collectionName) {
        logger.info("Start create collection");
//        if (database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
            database.getCollection(collectionName).drop();
            logger.debug("Collection {} dropped", collectionName);
//        }
        database.createCollection(collectionName);
        logger.debug("Collection {} created", collectionName);
    }
}
