//package com.level3_2.dao;
//
//import com.level3_2.FileProcessing;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.LinkedList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;
//
//class ShopDAOTest {
//
//        ShopDAO shopDAO = new ShopDAO();
//        Connection connection = mock(Connection.class);
//        Statement statement = mock(Statement.class);
//        FileProcessing fileProcessing = mock(FileProcessing.class);
//
//        @Test
//        void insertDataTest() throws SQLException, IOException {
//            List<String> queries = new LinkedList<>(List.of("INSERT INTO productType (product_type) VALUES (smth)"));
//
//            when(fileProcessing.getDataFromTheFile(anyString())).thenReturn(queries);
//            when(connection.createStatement()).thenReturn(statement);
//
//            shopDAO.insertData(connection);
//
//            verify(statement, times(1)).executeBatch();
//            verify(statement, times(15)).addBatch(anyString());
//            verify(connection, times(2)).createStatement();
//        }
//
//    }
