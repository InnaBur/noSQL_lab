package com.level3_2.dao;

import com.level3_2.FileProcessing;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ShopDAOTest {

    ShopDAO shopDAO = new ShopDAO();
    FileProcessing mockFileProcessing = mock(FileProcessing.class);
    MongoDatabase mockDatabase = mock(MongoDatabase.class);
    MongoCollection<Document> mockCollection = mock(MongoCollection.class);

    @Test

    void insertDataTest() throws IOException {

        List<String> fileData = Arrays.asList("Hello", "World");
        when(mockDatabase.getCollection("shop")).thenReturn(mockCollection);
        when(mockFileProcessing.getDataFromTheFile("shop_address.csv")).thenReturn(fileData);
        shopDAO.fileProcessing = mockFileProcessing;
        shopDAO.insertDataIntoCollection(mockDatabase);

        verify(mockDatabase, times(1)).getCollection(anyString());
        verify(mockCollection,times(1)).insertMany(argThat(documents -> documents.size() == 2));
    }

}
