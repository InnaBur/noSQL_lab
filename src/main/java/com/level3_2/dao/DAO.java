package com.level3_2.dao;

import com.level3_2.FileProcessing;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public interface DAO {

    Logger logger = LoggerFactory.getLogger(DAO.class);
    FileProcessing fileProcessing = new FileProcessing();

    void insertDataIntoCollection(MongoDatabase database) throws IOException;
    Document createDocument(String value);
    List<ObjectId> getIdIntoList(MongoDatabase database);
    void logRPS(StopWatch watch, int count, String collectionName);

}
