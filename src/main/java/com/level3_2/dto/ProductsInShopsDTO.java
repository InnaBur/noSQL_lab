package com.level3_2.dto;

import org.bson.Document;

public class ProductsInShopsDTO {

    private ProductDto productDto;
    private String shop;
    private int amount;


    public ProductsInShopsDTO(ProductDto productDto, String shop, int amount) {
        this.productDto = productDto;
        this.shop = shop;
        this.amount = amount;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ProductsInShopsDTO{" +
                "productDto=" + productDto +
                ", shop='" + shop + '\'' +
                ", amount=" + amount +
                '}';
    }

    public Document toDocument() {
        Document doc = new Document();
        doc.append("product", this.productDto.getProductName())
                .append("type_id", this.productDto.getType_id())
                .append("shop", this.shop)
                .append("amount", this.amount);
        return doc;
    }
}
