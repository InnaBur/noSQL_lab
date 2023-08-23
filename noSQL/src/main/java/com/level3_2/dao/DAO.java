package com.level3_2.dao;

import com.level3_2.FileProcessing;
import com.level3_2.QueriesProcessing;
import com.level3_2.dto.ProductDto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public interface DAO {

    Logger logger = LoggerFactory.getLogger(DAO.class);
    FileProcessing fileProcessing = new FileProcessing();
    QueriesProcessing queriesProcessing = new QueriesProcessing();

    void insertDataIntoCollection(MongoDatabase database) throws IOException;
    Document createDocument(String value);
    List<ObjectId> getIdIntoList(MongoDatabase database);
    void insertDataIntoCollection(MongoDatabase database, List<ProductDto> productDtos) throws IOException;
}
