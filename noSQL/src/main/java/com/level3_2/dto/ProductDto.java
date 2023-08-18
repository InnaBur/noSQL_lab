package com.level3_2.dto;

import jakarta.validation.constraints.Size;
import org.bson.Document;
import org.bson.types.ObjectId;

public class ProductDto {

    @Size(max = 10, message = "Name length must not be longer then 10 symbols")
//    @Pattern(regexp = "^A?.*a.*", message = "Must be at least one letter 'а'")
    private String productName;
    private ObjectId type_id;

    public ProductDto() {
    }

    public ProductDto(String productName, ObjectId type_id) {
        this.productName = productName;
        this.type_id = type_id;
    }

    public Document toDocument() {
        Document doc = new Document();
        doc.append("product", this.productName)
                .append("type_id", this.type_id);

        return doc;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ObjectId getType_id() {
        return type_id;
    }

    public void setType_id(ObjectId type_id) {
        this.type_id = type_id;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "productName='" + productName + '\'' +
                ", type_id=" + type_id +
                '}';
    }
}
