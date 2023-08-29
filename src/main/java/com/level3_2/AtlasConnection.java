package com.level3_2;

import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtlasConnection {
    private static final Logger logger = LoggerFactory.getLogger(AtlasConnection.class);
    public MongoDatabase getDatabase () {
        MongoDatabase database;
        String username = "mongoInna";
        String password = "admin124";
        String uri = "mongodb+srv://mongoInna:admin124@cluster0.qg9kdnn.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            logger.info("MongoClient created");
            database = mongoClient.getDatabase("myMongo");
            logger.info("Database is got");
//            MongoCollection<Document> collection = database.getCollection("movies");
//            Document doc = collection.find(eq("title", "Back to the Future")).first();
//            if (doc != null) {
//                System.out.println(doc.toJson());
//            } else {
//                System.out.println("No matching documents found.");
//            }
        }
      return database;
    }
}
