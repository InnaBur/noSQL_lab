package com.level3_2.dao;

import com.level3_2.DataProcessing;
import com.level3_2.dto.DocumentGenerator;
import com.level3_2.dto.ProductDto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductsDAO implements DAO {

    private static final int BATCH_SIZE = 1000;
    private static final int BATCH_SIZE_FOR_LOG = 10000;
    DocumentGenerator documentGenerator = new DocumentGenerator();
    private final String COLLECTION_PRODUCTS = "products";
//    ProductDto productDto = new ProductDto();
//    private static final String inputtedType = new DataProcessing().readOutputFormat();


    private void logRPS(double sec, int count) {
        logger.info("Inserted RPS into products table is {} ", sec);
        logger.info("Inserted RPS rows in second {} ", count / sec);
        logger.info("Inserted rows {} ", count);
    }

    private void logBatchNum(int count) {
        if (count % BATCH_SIZE_FOR_LOG == 0) {
            logger.debug("{} batches into products table inserted", count);
        }
    }

    @Override
    public void insertDataIntoCollection(MongoDatabase database) throws IOException {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_PRODUCTS);
        List<Document> documents = documentGenerator.generateProductsDTOlist(database);
        StopWatch watch = new StopWatch();
        watch.start();
        collection.insertMany(documents);

        watch.stop();
        double seconds = watch.getTime() / 1000.0;
        logRPS(seconds, documents.size());
        logger.debug("Data into collection 'products' inserted");
    }

    @Override
    public Document createDocument(String value) {
        return null;
    }

    @Override
    public List<ObjectId> getIdIntoList(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_PRODUCTS);

        List<ObjectId> idList = new ArrayList<>();
        for (Document document : collection.find()) {
            ObjectId id = document.getObjectId("_id");
            idList.add(id);
        }
        return idList;
    }

    @Override
    public void insertDataIntoCollection(MongoDatabase database, List<ProductDto> productDtos) throws IOException {

    }

    public List<ProductDto> getProductsIntoList(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_PRODUCTS);

        List<ProductDto> list = new ArrayList<>();
        for (Document document : collection.find()) {
            String prod = document.getString("product");
            ObjectId type = document.getObjectId("type_id");
            list.add(new ProductDto(prod, type));
        }
        return list;
    }
}

