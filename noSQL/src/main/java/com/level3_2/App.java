package com.level3_2;

import com.level3_2.dao.ProductTypeDAO;
import com.level3_2.dao.ProductsDAO;
import com.level3_2.dao.ProductsInShopsDAO;
import com.level3_2.dao.ShopDAO;
import com.level3_2.dto.ProductDto;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException, SQLException {
        logger.debug("Start program");
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ShopDAO shopDAO = new ShopDAO();
        ProductsDAO productsDAO = new ProductsDAO();
        FileProcessing fileProcessing = new FileProcessing();
        CollectionsCreator collectionsCreator = new CollectionsCreator();
        String url = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(url)) {

            logger.debug("MongoDB was created");
            MongoDatabase database = mongoClient.getDatabase("myMongoDb");
            collectionsCreator.createCollectionsReference(database);
            shopDAO.insertDataIntoCollection(database);
            productTypeDAO.insertDataIntoCollection(database);

            productsDAO.insertDataIntoCollection(database);
            List<ProductDto> productDtos = productsDAO.getProductsIntoList(database);
            System.out.println("LIST " + productDtos.get(0 ) + "Size " + productDtos.size());
            List<String> shops = shopDAO.getShopIntoList(database);
            System.out.println("SHOPS " + shops.get(0) + shops.size());
            ProductsInShopsDAO productsInShopsDAO = new ProductsInShopsDAO();
            productsInShopsDAO.insertDataIntoCollection2(database, productDtos, shops);


            MongoCollection<Document> productsCollection = database.getCollection("products");
            MongoCollection<Document> shopCollection = database.getCollection("shop");
            MongoCollection<Document> productTypeCollection = database.getCollection("type");
            MongoCollection<Document> generalTableCollection = database.getCollection("ProductsInShops");
//
            String typeFromConsole = "Food";
            ObjectId prodTypeId = productTypeCollection.find(eq("type", typeFromConsole)).first().getObjectId("_id");

            List<Bson> pipeline = Arrays.asList(
                    match(eq("type_id", prodTypeId)),
                    group("$shop", sum("totalAmount", "$amount")),
                    sort(descending("totalAmount")),
                    limit(10)
            );

            AggregateIterable<Document> result = generalTableCollection.aggregate(pipeline);

            for (Document doc : result) {
                System.out.println(doc);
                // You can access specific fields using doc.get("field_name")
            }
//
//            ObjectId prodTypeId = productTypeCollection.find(eq("type", typeFromConsole)).first().getObjectId("_id");
//            System.out.println("PRODTYPEID " + prodTypeId);
//
//
//            List<Bson> pipeline = Arrays.asList(
//                    match(eq("product_id", prodTypeId)));
//            List<Bson> pipeline1 = Arrays.asList(
//            lookup("products", "product_id", "_id", "productInfo"),
//                    unwind("$productInfo"),
//                    match(eq("productInfo.type_id", prodTypeId)),
//                    group("$shop_id", sum("productCount", "$product_amount")),
//                    sort(descending("productCount")),
//                    limit(1)
//            );
//
//            System.out.println("BSON " + pipeline1);
//
//            AggregateIterable<Document> result = generalTableCollection.aggregate(pipeline1);
//
//            for (Document doc : result) {
//                System.out.println("RES " + doc);
//                // You can access specific fields using doc.get("field_name")
//            }

        } catch (MongoException e) {
            logger.error("MongoDB was not created");
            System.exit(1);
        }


        logger.info("Program finished");
    }

    public static ObjectId getProductTypeIdByType(MongoDatabase database, String productType) {
        MongoCollection<Document> typesCollection = database.getCollection("type");
        Document typeDocument = typesCollection.find(new Document("type", productType)).first();
        if (typeDocument != null) {
            return typeDocument.getObjectId("_id");
        }
        return null; // або обробка відсутності типу
    }
}
