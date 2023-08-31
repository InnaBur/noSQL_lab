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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    public void insertWithThreads(MongoDatabase database, List<ProductDto> productDtos, List<String> shop) {

        MongoCollection<Document> collection = database.getCollection(COLLECTION_PRODUCTS_IN_SHOPS);
        int batchSize = Integer.parseInt(properties.getProperty("batch"));
        int rows = Integer.parseInt(properties.getProperty("rows"));
        int numThreads = Integer.parseInt(properties.getProperty("numThreads"));;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        StopWatch watch;
        int rowsInThread = rows / numThreads;
        int restRows = rows % numThreads;
        System.out.println("REST Rows " + restRows);

        try {


            watch = new StopWatch();
            watch.start();

            for (int i = 0; i < numThreads; i++) {
                insertInThreads(i, rowsInThread, restRows, executorService, productDtos, shop, collection, batchSize);


            }


        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(10, TimeUnit.MINUTES)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }
        if (restRows > 0) {
            int count = 0;
            List<Document> documents = new LinkedList<>();
            for (int j = 0; j < restRows; j++) {

                documents.add(documentGenerator.generateStoreDTO(productDtos, shop));
                count++;

            }
            collection.insertMany(documents);
            logger.info("Inserted {} documents", documents.size());
            logBatchNum(count, batchSize, numThreads + 1);
//                insertInThreads((numThreads - 1) * rowsInThread, rowsInThread, restRows, executorService, productDtos, shop, collection, batchSize)
        }
        watch.stop();

        logRPS(watch, rows, "COLLECTION_PRODUCTS_IN_SHOPS");
//        insertDataIntoCollection(database, productDtos, shop);
//                        insertBatchData(database, productDtos, shop, startIndex, endIndex, batchSize);
    }

    private void insertInThreads(int i, int rowsInThread, int restRows, ExecutorService executorService,
                                 List<ProductDto> productDtos, List<String> shop, MongoCollection<Document> collection, int batchSize) {
        int startIndex = i * rowsInThread;
        int endIndex = getEnd(i, startIndex, restRows, rowsInThread);
        List<Document> documents = new LinkedList<>();
        executorService.submit(() -> {

            int count = 0;
            for (int j = startIndex; j < endIndex; j++) {

                documents.add(documentGenerator.generateStoreDTO(productDtos, shop));
                count++;
                if (count % batchSize == 0) {
                    insertBatch(collection, documents, count, i);
                    documents.clear();
                }

            }
            logBatchNum(count, batchSize, i);

//
        });
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
                insertBatch(collection, documents, count, i);
                documents.clear();
            }
        }
        logBatchNum(count, batchSize, 1);
        insertRest(documents, collection);

        logger.debug("Data into Products table inserted");

        watch.stop();

        logRPS(watch, count, COLLECTION_PRODUCTS_IN_SHOPS);
        logger.debug("Data into collection 'products' inserted");
    }

    private void insertRest(List<Document> documents, MongoCollection<Document> collection) {
        if (documents.size() > 0) {
            collection.insertMany(documents);
            logger.info("Inserted {} documents", documents.size());
        }
    }

    private void insertBatch(MongoCollection<Document> collection, List<Document> documents, int count, int i) {
        collection.insertMany(documents);
        logger.debug("Inserted {} rows in {} thread", count, i);
    }

    public void logRPS(StopWatch watch, int count, String dbName) {
        double sec = watch.getTime() / 1000.0;
        logger.info("Inserted RPS into {} is {} ", dbName, sec);
        logger.info("Inserted RPS rows in second {} ", count / sec);
        logger.info("Inserted {} rows into {}  ", count, dbName);
    }

    private void logBatchNum(int count, int batchSize, int i) {
        if (count % batchSize == 0) {
            logger.debug("{} rows inserted in {} thread", count, i + 1);
        }
    }

    public void findShopByProductType(MongoDatabase database) {
        MongoCollection<Document> productTypeCollection = database.getCollection("type");
        MongoCollection<Document> productsInShopsCollection = database.getCollection("ProductsInShops");

        String typeFromConsole = new DataProcessing().readOutputFormat();
        ObjectId prodTypeId = productTypeCollection.find(eq("type", typeFromConsole)).first().getObjectId("_id");
        StopWatch watch = new StopWatch();
        watch.start();
        List<Bson> shopAddress = Arrays.asList(
                match(eq("type_id", prodTypeId)),
                group("$shop", sum("totalAmount", "$amount")),
                sort(descending("totalAmount")),
                limit(1)
        );
        watch.stop();
        double sec = watch.getTime();
        logger.info("Row found for {} milliseconds", sec);
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

    private static int getEnd(int i, int startIdx, int remainingRows, int rowsPerThread) {
        int endIdx = startIdx + rowsPerThread;
        if (i == 5 - 1) {
            endIdx += remainingRows;
        }
        return endIdx;
    }
}
