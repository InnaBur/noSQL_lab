//package com.level3_2.dao;
//
//import com.level3_2.DataProcessing;
//import com.level3_2.FileProcessing;
//import com.level3_2.QueriesProcessing;
//import com.level3_2.dto.DTOGenerator;
//import org.apache.commons.lang3.time.StopWatch;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Random;
//
//public class GeneralTableDAO implements DAO {
//
//    private static final int BATCH_SIZE = 1000;
//    private static final int BATCH_SIZE_FOR_LOG = 50000;
//    private static final String SQL_GENERAL_INSERT = "INSERT INTO generalTable (product_id, shop_id, balance) VALUES (?, ?, ?)";
//    DTOGenerator dtoGenerator = new DTOGenerator();
//    private static final String inputtedType = new DataProcessing().readOutputFormat();
//    private static final String INDEX_QUERY = "CREATE INDEX idx_shop_id ON generalTable (shop_id)";
//    private static final String INDEX_QUERY2 = "CREATE INDEX idx_product_id ON generalTable (product_id)";
//    QueriesProcessing queriesProcessing = new QueriesProcessing();
//    Random random = new Random();
//
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
//    private void logRPS(double sec, int count) {
//        logger.info("Inserted RPS into GeneralTable is {} ", sec);
//        logger.info("Inserted RPS rows in second {} ", count / sec);
//        logger.info("Inserted {} rows into GeneralTable  ", count);
//    }
//
//    private void logBatchNum(int count) {
//        if (count % BATCH_SIZE_FOR_LOG == 0) {
//            logger.debug("{} batches inserted", count);
//        }
//    }
//
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
//}
