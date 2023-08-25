package com.level3_2.dao;

import com.level3_2.DocumentGenerator;
import com.level3_2.dto.ProductDto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ProductsDAO implements DAO {

    DocumentGenerator documentGenerator = new DocumentGenerator();
    private final String COLLECTION_PRODUCTS = "products";

    @Override
    public void insertDataIntoCollection(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_PRODUCTS);
        List<Document> documents = documentGenerator.generateProductsDTOlist(database);
        StopWatch watch = new StopWatch();
        watch.start();

        collection.insertMany(documents);

        watch.stop();

        logRPS(watch, documents.size(), COLLECTION_PRODUCTS);
        logger.debug("Data into collection 'products' inserted");
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
    public void logRPS(StopWatch watch, int count, String collectionName) {
        double seconds = watch.getTime() / 1000.0;
        logger.info("Inserted RPS into {} is {} ", collectionName, seconds);
        logger.info("Inserted RPS rows in second {} ", count / seconds);
        logger.info("Inserted rows {} ", count);
    }


    @Override
    public Document createDocument(String value) {
        return null;
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

