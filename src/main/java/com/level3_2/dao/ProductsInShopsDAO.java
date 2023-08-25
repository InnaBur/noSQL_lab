package com.level3_2.dao;

import com.level3_2.DataProcessing;
import com.level3_2.DocumentGenerator;
import com.level3_2.dto.ProductDto;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;

public class ProductsInShopsDAO {

    Properties properties;

    private static final Logger logger = LoggerFactory.getLogger(ProductsInShopsDAO.class);
    private static final String COLLECTION_PRODUCTS_IN_SHOPS = "ProductsInShops";
    DocumentGenerator documentGenerator = new DocumentGenerator();

    public ProductsInShopsDAO(Properties properties) {
        this.properties = properties;
    }

    public void insertDataIntoCollection(MongoDatabase database, List<ProductDto> productDtos, List<String> shop) {

        MongoCollection<Document> collection = database.getCollection(COLLECTION_PRODUCTS_IN_SHOPS);
        int batchSize = Integer.parseInt(properties.getProperty("batch"));
        int rows = Integer.parseInt(properties.getProperty("rows"));
        List<Document> documents = new LinkedList<>();

        StopWatch watch = new StopWatch();
        watch.start();

        int count = 0;
        for (int i = 0; i < rows; i++) {

            documents.add(documentGenerator.generateStoreDTO(productDtos, shop));
            count++;
            if (count % batchSize == 0) {
                insertBatch(collection, documents, count);
            }
        }
        logBatchNum(count, batchSize);
        insertRest(documents, collection);

        logger.debug("Data into Products table inserted");

        watch.stop();

        logRPS(watch, count, COLLECTION_PRODUCTS_IN_SHOPS);
        logger.debug("Data into collection 'products' inserted");
    }

    private void insertRest(List<Document> documents, MongoCollection<Document> collection) {
        if (documents.size() > 0) {
            collection.insertMany(documents);
        }
    }

    private void insertBatch(MongoCollection<Document> collection, List<Document> documents, int count) {
        collection.insertMany(documents);
        logger.debug("Inserted {} rows", count);
        documents.clear();
    }

    private void logRPS(StopWatch watch, int count, String dbName) {
        double sec = watch.getTime() / 1000.0;
        logger.info("Inserted RPS into {} is {} ", dbName, sec);
        logger.info("Inserted RPS rows in second {} ", count / sec);
        logger.info("Inserted {} rows into {}  ", count, dbName);
    }

    private void logBatchNum(int count, int batchSize) {
        if (count % batchSize == 0) {
            logger.debug("{} batches inserted", count);
        }
    }

    public void findShopByProductType(MongoDatabase database) {
        MongoCollection<Document> productTypeCollection = database.getCollection("type");
        MongoCollection<Document> productsInShopsCollection = database.getCollection("ProductsInShops");

        String typeFromConsole = new DataProcessing().readOutputFormat();
        ObjectId prodTypeId = productTypeCollection.find(eq("type", typeFromConsole)).first().getObjectId("_id");

        List<Bson> shopAddress = Arrays.asList(
                match(eq("type_id", prodTypeId)),
                group("$shop", sum("totalAmount", "$amount")),
                sort(descending("totalAmount")),
                limit(1)
        );

        printResults(productsInShopsCollection, shopAddress, typeFromConsole);
    }

    private void printResults(MongoCollection<Document> productsInShopsCollection, List<Bson> shopAddress, String type) {
        AggregateIterable<Document> result = productsInShopsCollection.aggregate(shopAddress);
        for (Document doc : result) {
            logger.debug("Document is: {}", doc);
            logger.info("The largest number of products ({}) of the {} type is in the store at the address: {}",
                    doc.getInteger("totalAmount"),
                    type,
                    doc.getString("_id"));
        }
    }
}
