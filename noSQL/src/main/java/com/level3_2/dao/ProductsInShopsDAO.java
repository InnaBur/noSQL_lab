package com.level3_2.dao;

import com.level3_2.DataProcessing;
import com.level3_2.QueriesProcessing;
import com.level3_2.dto.DocumentGenerator;
import com.level3_2.dto.ProductDto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class ProductsInShopsDAO implements DAO {

    private List<ProductDto> productDtos;
    private List<String> shop;
    private static final int BATCH_SIZE = 1000;
    private static final int BATCH_SIZE_FOR_LOG = 1000;
    private static final String SQL_GENERAL_INSERT = "INSERT INTO generalTable (product_id, shop_id, balance) VALUES (?, ?, ?)";
    DocumentGenerator documentGenerator = new DocumentGenerator();
    private static final String inputtedType = new DataProcessing().readOutputFormat();
    QueriesProcessing queriesProcessing = new QueriesProcessing();
    Random random = new Random();

//    public ProductsInShopsDAO(List<ProductDto> productDtos, List<String> shop) {
//        this.productDtos = productDtos;
//        this.shop = shop;
//    }

    public ProductsInShopsDAO() {
    }

    public void insertDataIntoCollection2(MongoDatabase database, List<ProductDto> productDtos, List<String> shop) throws IOException {

        MongoCollection<Document> collection = database.getCollection("ProductsInShops");

        StopWatch watch = new StopWatch();
        watch.start();
        List<Document> documents = new LinkedList<>();
        int count = 0;
        while (count < 10000) {

            for (int i = 0; i < BATCH_SIZE; i++) {
                documents.add(documentGenerator.generateStoreDTO(database, productDtos, shop));

//            logger.debug("List StoreDTO created. Size is {}", documents.size());
                count++;
                if (count % BATCH_SIZE == 0) {
                    database.getCollection("ProductsInShops").insertMany(documents);
                    logger.debug("Inserted {} rows", count);
                    documents.clear();
                }
            }

//            logBatchNum(count);
        }
        if (documents.size() > 0) {
            database.getCollection("ProductsInShops").insertMany(documents);
        }
//        database.getCollection("ProductsInShops").insertMany();
        logger.debug("Data into Products table inserted");

        watch.stop();
        double seconds = watch.getTime() / 1000.0;
//            logRPS(seconds, documents.size());
        logger.debug("Data into collection 'products' inserted");
    }

    @Override
    public void insertDataIntoCollection(MongoDatabase database) throws IOException {

        MongoCollection<Document> collection = database.getCollection("ProductsInShops");

        StopWatch watch = new StopWatch();
        watch.start();

        int count = 0;
        while (count < 10000) {
            List<Document> documents = documentGenerator.generateStoreDTOlist(database, productDtos);
            logger.debug("List StoreDTO created. Size is {}", documents.size());
            count += documents.size() - 1;
            logger.debug("Count {}", count);
            if (count % BATCH_SIZE == 0) {
                database.getCollection("ProductsInShops").insertMany(documents);
                logger.debug("Inserted {} rows", count);
                documents.clear();
            } else {
                logger.debug("Batch {}", count % BATCH_SIZE);
            }

//            logBatchNum(count);
        }
//        database.getCollection("ProductsInShops").insertMany();
        logger.debug("Data into Products table inserted");

        watch.stop();
        double seconds = watch.getTime() / 1000.0;
//            logRPS(seconds, documents.size());
        logger.debug("Data into collection 'products' inserted");
    }
//        List<ObjectId> idProducts = productsDAO.getIdIntoList(database);
//        List<ObjectId> idShops = shopDAO.getIdIntoList(database);
//        List<Document> documentsIntoMainCollection = new LinkedList<>();
//
//        StopWatch watch = new StopWatch();
//        watch.start();
//        int count = 0;
//        while (count < 10) {
//
//            int ind = random.nextInt(idShops.size());
//            ObjectId shopId = idShops.get(ind);
//            int ind2 = random.nextInt(idProducts.size());
//            ObjectId prodId = idProducts.get(ind2);
//            Document document = new Document("productId", prodId)
//                    .append("shopId", shopId)
//                    .append("productCount", random.nextInt(100));
//            documentsIntoMainCollection.add(document);
//            count++;
//            if (count % BATCH_SIZE == 0) {
//                database.getCollection("ProductsInShops").insertMany(documentsIntoMainCollection);
//                logger.debug("Inserted {} rows", count);
//                documentsIntoMainCollection.clear();
//            }
//
//            logBatchNum(count);
//        }
//        database.getCollection("ProductsInShops").insertMany(documentsIntoMainCollection);
//        logger.debug("Data into Products table inserted");
//        watch.stop();
//        double sec = watch.getTime() / 1000.0;
//        logRPS(sec, count);


    @Override
    public Document createDocument(String value) {
        return null;
    }

    @Override
    public List<ObjectId> getIdIntoList(MongoDatabase database) {
        return null;
    }

    @Override
    public void insertDataIntoCollection(MongoDatabase database, List<ProductDto> productDtos) throws IOException {

    }

    //    @Override
//    public void insertData(Connection connection) {
//        int rows = Integer.parseInt(new FileProcessing().loadProperties().getProperty("rows"));
//        List<Integer> shopId = queriesProcessing.getShopId(connection);
//        List<Integer> productId = queriesProcessing.getProductId(connection);
//        StopWatch watch = new StopWatch();
//        watch.start();
//        try {
//            PreparedStatement statement = connection.prepareStatement(SQL_GENERAL_INSERT);
//            int count = 0;
//            while (count < rows) {
//
//                statement.setInt(1, dtoGenerator.generateRandomProductId(productId));
//                statement.setInt(2, dtoGenerator.generateRandomShopId(shopId));
//                statement.setInt(3, random.nextInt(2));
//
//                statement.addBatch();
//
//                count++;
//                if (count % BATCH_SIZE == 0) {
//                    statement.executeBatch();
//                    statement.clearBatch();
//                }
//
//                logBatchNum(count);
//            }
//            statement.executeBatch();
//            logger.debug("Data into Products table inserted");
//            watch.stop();
//            connection.commit();
//            double sec = watch.getTime() / 1000.0;
//            logRPS(sec, count);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//
//            try {
//                connection.rollback();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
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

//    public void getShopAddressWithTheLargestNumberOfGivenTypeProducts(Connection connection) {
//        StopWatch watch = new StopWatch();
//        watch.start();
//        try {
//            String sql = "SELECT shop.address AS address, type.product_type AS type, SUM(generalTable.balance) AS balance FROM generalTable " +
//                    "JOIN products AS product ON product.id = generalTable.product_id " +
//                    "JOIN productType AS type ON product.type_id = type.id " +
//                    "JOIN shop ON generalTable.shop_id = shop.id " +
//                    "WHERE type.product_type = ? " +
//                    "GROUP BY shop.id, shop.address, type.product_type " +
//                    "ORDER BY COUNT(*) DESC " +
//                    "LIMIT 1;";
//
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, inputtedType);
//
//            statement.executeBatch();
//
//            connection.commit();
//            watch.stop();
//            double sec = watch.getTime();
//            logger.info("Row found for {} milliseconds", sec);
//            printResult(statement);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//
//            try {
//                connection.rollback();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private void printResult(PreparedStatement statement) throws SQLException {
//        ResultSet resultSet = statement.executeQuery();
//        while (resultSet.next()) {
//            logger.info("The largest number of products ({}) of the {} type is in the store at the address {}",
//                    resultSet.getString("balance"),
//                    resultSet.getString("type"),
//                    resultSet.getString("address"));
//        }
//    }
//
//    public void createIndexGen(Connection connection) {
//        queriesProcessing.createIndex(connection, INDEX_QUERY);
//        queriesProcessing.createIndex(connection, INDEX_QUERY2);
//    }
}
