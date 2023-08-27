package com.level3_2.dao;

import com.level3_2.FileProcessing;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShopDAO implements DAO {

    private static final String SHOP_TABLE_DATA = "shop_address.csv";
    private final String COLLECTION_SHOP = "shop";
    FileProcessing fileProcessing = new FileProcessing();


    @Override
    public void insertDataIntoCollection(MongoDatabase database) throws IOException {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_SHOP);
        List<Document> documents = new LinkedList<>();
        List<String> fileData = fileProcessing.getDataFromTheFile(SHOP_TABLE_DATA);
        for (String d : fileData) {
            documents.add(createDocument(d));
        }

        collection.insertMany(documents);
        logger.debug("Data into collection 'shops' inserted");
    }

    @Override
    public Document createDocument(String value) {
        Document document = new Document();
        document.put("address", value);
        return document;
    }

    public List<ObjectId> getIdIntoList(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_SHOP);

        List<ObjectId> idList = new ArrayList<>();
        for (Document document : collection.find()) {
            ObjectId id = document.getObjectId("_id");
            idList.add(id);
        }
        return idList;
    }

    @Override
    public void logRPS(StopWatch watch, int count, String dbName) {

    }

    public List<String> getShopIntoList(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_SHOP);

        List<String> list = new ArrayList<>();
        for (Document document : collection.find()) {
            String shop = document.getString("address");
            list.add(shop);
        }
        return list;
    }
}
