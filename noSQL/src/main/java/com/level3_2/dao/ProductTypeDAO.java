package com.level3_2.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProductTypeDAO implements DAO {

    private static final String PRODUCT_TYPE_TABLE_DATA = "product_type.csv";
    private final String COLLECTION_PRODUCT_TYPE = "type";


    @Override
    public void insertDataIntoCollection(MongoDatabase database) throws IOException {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_PRODUCT_TYPE);
        List<Document> documents = new LinkedList<>();
        List<String> fileData = fileProcessing.getDataFromTheFile(PRODUCT_TYPE_TABLE_DATA);
        for (String d : fileData) {
            documents.add(createDocument(d));
        }

        collection.insertMany(documents);
        logger.debug("Data into collection 'type' inserted");
    }

    @Override
    public Document createDocument(String value) {
        Document document = new Document();
        document.put("type", value);
        return document;
    }

    @Override
    public List<ObjectId> getIdIntoList(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_PRODUCT_TYPE);

        List<ObjectId> idList = new ArrayList<>();
        for (Document document : collection.find()) {
            ObjectId id = document.getObjectId("_id");
            idList.add(id);
        }
        return idList;
    }
}
