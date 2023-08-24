package com.level3_2.dao;

import com.level3_2.App;
import com.level3_2.DataProcessing;
import com.level3_2.QueriesProcessing;
import com.level3_2.dto.DocumentGenerator;
import com.level3_2.dto.ProductDto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class ProductsInShopsDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProductsInShopsDAO.class);
    private static final int BATCH_SIZE = 1000;
    private static final int BATCH_SIZE_FOR_LOG = 1000;
    DocumentGenerator documentGenerator = new DocumentGenerator();
    private static final String inputtedType = new DataProcessing().readOutputFormat();


    public void insertDataIntoCollection2(MongoDatabase database, List<ProductDto> productDtos, List<String> shop) throws IOException {

        MongoCollection<Document> collection = database.getCollection("ProductsInShops");

        StopWatch watch = new StopWatch();
        watch.start();
        List<Document> documents = new LinkedList<>();
        int count = 0;
        while (count < 1000000) {

            for (int i = 0; i < BATCH_SIZE; i++) {
                documents.add(documentGenerator.generateStoreDTO(database, productDtos, shop));
                count++;
                if (count % BATCH_SIZE == 0) {
                    collection.insertMany(documents);
                    logger.debug("Inserted {} rows", count);
                    documents.clear();
                }
            }
            logBatchNum(count);
        }
        if (documents.size() > 0) {
            collection.insertMany(documents);
        }
        logger.debug("Data into Products table inserted");

        watch.stop();
        double seconds = watch.getTime() / 1000.0;
        logRPS(seconds, count);
        logger.debug("Data into collection 'products' inserted");
    }

    private void logRPS(double sec, int count) {
        logger.info("Inserted RPS into GeneralTable is {} ", sec);
        logger.info("Inserted RPS rows in second {} ", count / sec);
        logger.info("Inserted {} rows into GeneralTable  ", count);
    }

    private void logBatchNum(int count) {
        if (count % BATCH_SIZE_FOR_LOG == 0) {
            logger.debug("{} batches inserted", count);
        }
    }


}
