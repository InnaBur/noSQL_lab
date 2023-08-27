package com.level3_2;

import com.level3_2.dao.ProductTypeDAO;
import com.level3_2.dto.ProductDto;
import com.level3_2.dto.ProductsInShopsDTO;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class DocumentGeneratorTest {

    MongoDatabase mockDatabase = mock(MongoDatabase.class);
    ProductTypeDAO productTypeDAOmock = mock(ProductTypeDAO.class);
    DocumentGenerator documentGeneratorTest = new DocumentGenerator();

    Random random = mock(Random.class);

    @Test
    void generateProductsDTOlistTest() {

        List<ObjectId> typeIdList = Arrays.asList(new ObjectId("123456789101112131415161"));

        documentGeneratorTest.productTypeDAO = productTypeDAOmock;

        when(productTypeDAOmock.getIdIntoList(mockDatabase)).thenReturn(typeIdList);

        List<Document> result = documentGeneratorTest.generateProductsDTOlist(mockDatabase);
        assertNotNull(result);
        verify(productTypeDAOmock, times(1)).getIdIntoList(mockDatabase);
    }

    @Test
   void generateListTest() {
        int quantityDTO = 10;
        List<ObjectId> typeIdList = Arrays.asList(new ObjectId("123456789101112131415161"));

        List<Document> doc = documentGeneratorTest.generateList(quantityDTO, typeIdList);

        assertNotNull(doc);
        assertEquals(quantityDTO, doc.size());
    }



    @Test
    void generateRandomObjectId() {
        List<ObjectId> id = Arrays.asList(new ObjectId("123456789101112131415161"));
        when(random.nextInt()).thenReturn(0);
        ObjectId expected = new ObjectId("123456789101112131415161");
        ObjectId actual = documentGeneratorTest.generateRandomTypeId(id);

        assertEquals(expected, actual);
    }

    @Test
    void textGeneratorLengthTest() {
        String result = documentGeneratorTest.textGenerator();

        assertTrue(result.length() <= 11);
    }

    @Test
    void isValidProductDtoTest() {
        ProductDto validProductDto = new ProductDto("Abadefgh", new ObjectId("123456789101112131415161"));
        ProductDto invalidProductDto = new ProductDto("asdfghjklqw", new ObjectId("123456789101112131415161"));

       boolean valid = documentGeneratorTest.isValid(validProductDto);
        boolean invalid = documentGeneratorTest.isValid(invalidProductDto);

        assertTrue(valid);
        assertFalse(invalid);
    }
}