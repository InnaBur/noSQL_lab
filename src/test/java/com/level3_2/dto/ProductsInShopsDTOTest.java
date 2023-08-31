package com.level3_2.dto;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductsInShopsDTOTest {

    ProductDto productDto = new ProductDto();

    @Test
    void toDocument() {
        ProductsInShopsDTO productsInShopsDTO = new ProductsInShopsDTO(
                new ProductDto("name", new ObjectId("123456789101112131415161")),
                "shop", 10);
        Document productDto = new Document("productName", "name")
                .append("type_id", new ObjectId("123456789101112131415161"));

        Document productsInShopDoc = new Document("product", productDto.get("productName"))
                .append("type_id", productDto.get("type_id"))
                .append("shop", "shop")
                .append("amount", 10);
        productsInShopsDTO.toDocument();
        assertEquals(productsInShopDoc, productsInShopsDTO.toDocument());
    }
}