package com.level3_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueriesProcessing {
    private static final String SQL_CREATE_TABLES_FILE_NAME = "createTables.sql";
    private static final String SQL_DROP_TABLES_FILE_NAME = "dropTables.sql";
    private static final String DROPPED = "dropped";
    private static final String CREATED = "created";
    private static final String SQL_GET_SHOP_ID = "SELECT id FROM shop";
    private static final String SQL_GET_TYPE_ID = "SELECT id FROM productType";
    private static final String SQL_GET_PRODUCT_ID = "SELECT id FROM products";
    private static final Logger logger = LoggerFactory.getLogger(QueriesProcessing.class);
    FileProcessing fileProcessing = new FileProcessing();

    public void createTables(Connection connection) throws IOException, SQLException {
        executeQuery(connection, SQL_DROP_TABLES_FILE_NAME, DROPPED);
        executeQuery(connection, SQL_CREATE_TABLES_FILE_NAME, CREATED);
    }

    public void executeQuery(Connection connection, String fileName, String log) throws IOException {
        int tableNum = 0;
        try {
            List<String> queries = fileProcessing.getDataFromTheFile(fileName);
            for (String query : queries) {
                connection.createStatement().execute(query);
                connection.commit();
                tableNum++;
                logger.debug("Table {} {}", tableNum, log);
            }
            queries.clear();
        } catch (SQLException e) {
            logger.debug("Table is not created");
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getShopId(Connection connection) {
        try {
            Statement stmt = connection.createStatement();
            List<Integer> shopId = new ArrayList<>();
            ResultSet rs = stmt.executeQuery(SQL_GET_SHOP_ID);

            while (rs.next()) {
                shopId.add(rs.getInt("id"));
            }
            rs.close();
            stmt.close();

            return shopId;
        } catch (SQLException e) {
            logger.error("Problems with statement in getShopId Method");
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getProductId(Connection connection) {
        try {
            Statement stmt = connection.createStatement();
            List<Integer> productId = new ArrayList<>();
            ResultSet rs = stmt.executeQuery(SQL_GET_PRODUCT_ID);

            while (rs.next()) {
                productId.add(rs.getInt("id"));
            }
            rs.close();
            stmt.close();

            return productId;
        } catch (SQLException e) {
            logger.error("Problems with statement in getShopId Method");
            throw new RuntimeException(e);
        }
    }
    public List<Integer> getTypeId(Connection connection) {
        try {
            Statement stmt = connection.createStatement();
            List<Integer> typeId = new ArrayList<>();
            ResultSet rs = stmt.executeQuery(SQL_GET_TYPE_ID);

            while (rs.next()) {
                typeId.add(rs.getInt("id"));
            }
            rs.close();
            stmt.close();

            return typeId;
        } catch (SQLException e) {
            logger.error("Problems with statement in getTypeId Method");
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getId(Connection connection, String query) {
        try {
            Statement stmt = connection.createStatement();
            List<Integer> shopId = new ArrayList<>();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                shopId.add(rs.getInt("id"));
            }
            rs.close();
            stmt.close();

            return shopId;
        } catch (SQLException e) {
            logger.error("Problems with statement in getShopId Method");
            throw new RuntimeException(e);
        }
    }
    public void createIndex (Connection connection, String createIndexQuery) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createIndexQuery);
            logger.debug("Index created");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

