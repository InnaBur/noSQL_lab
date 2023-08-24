//package com.level3_2.dto;
//
//import com.level3_2.MyValidator;
//import com.level3_2.QueriesProcessing;
//import org.junit.jupiter.api.Test;
//
//import java.sql.Connection;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class DTOGeneratorTest {
//
//    DTOGenerator dtoGenerator = new DTOGenerator();
//    QueriesProcessing queriesProcessing = mock(QueriesProcessing.class);
//    Connection connection = mock(Connection.class);
//    MyValidator validator = mock(MyValidator.class);
//
//    Random random = mock(Random.class);
//
//
//    @Test
//    void generateRandomTypeIdOrShopId() {
//        ArrayList<Integer> id = new ArrayList<>(List.of(10));
//        when(random.nextInt()).thenReturn(0);
//        int expected = 10;
//        int actual = dtoGenerator.generateRandomTypeId(id);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void textGeneratorLengthTest() {
//        String result = dtoGenerator.textGenerator();
//
//        assertTrue(result.length() <= 11);
//    }
//
//    @Test
//    void isValidProductDtoTest() {
//        ProductDto validProductDto = new ProductDto("Abcdefgh", 1);
//        ProductDto invalidProductDto = new ProductDto("asdfghjklqw", 2);
//
//       boolean valid = dtoGenerator.isValid(validProductDto);
//        boolean invalid = dtoGenerator.isValid(invalidProductDto);
//
//        assertTrue(valid);
//        assertFalse(invalid);
//    }
//}