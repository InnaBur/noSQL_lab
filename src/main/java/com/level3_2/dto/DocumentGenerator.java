package com.level3_2.dto;

import com.level3_2.*;
import com.level3_2.dao.ProductTypeDAO;
import com.level3_2.dao.ProductsDAO;
import com.level3_2.dao.ShopDAO;
import com.mongodb.client.MongoDatabase;
import jakarta.validation.ConstraintViolation;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentGenerator {
    Properties properties = new FileProcessing().loadProperties();
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private final int quantityDTO = Integer.parseInt(properties.getProperty("quantityProducts"));
    int prodNameMaxLength = Integer.parseInt(properties.getProperty("prodNameMaxLength"));
    int prodNameMinLength = Integer.parseInt(properties.getProperty("prodNameMinLength"));
    MyValidator validator = new MyValidator();
    ProductTypeDAO productTypeDAO = new ProductTypeDAO();
    Random random = new Random();
    ShopDAO shopDAO = new ShopDAO();
//    ProductsDAO productsDAO = new ProductsDAO();


    public List<Document> generateProductsDTOlist(MongoDatabase database) {
        logger.debug("Generating products list");

        List<ObjectId> typeIdList = productTypeDAO.getIdIntoList(database);
        StopWatch watch = new StopWatch();
        watch.start();
        List<Document> listDTO = Stream.generate(() -> new ProductDto
                        (generateProductName(), generateRandomTypeId(typeIdList)))
                .filter(this::isValid)
                .limit(10000)
                .map(ProductDto::toDocument)
                .collect(Collectors.toCollection(LinkedList::new));

        watch.stop();
        double seconds = watch.getTime() / 1000.0;
        logRPS(listDTO, seconds);
        return listDTO;
    }

    public List<Document> generateStoreDTOlist(MongoDatabase database, List<ProductDto> productDtos) {

        logger.debug("Generating Store products list");

        List<String> shops = shopDAO.getShopIntoList(database);
        for (String str : shops) {
            System.out.println(str);
        }
        logger.debug("Shops list is got, size is {}", shops.size());
        System.out.println(productDtos.get(random.nextInt(productDtos.size())).getProductName());
        System.out.println(productDtos.get(random.nextInt(productDtos.size())).getType_id());
        System.out.println(shops.get(random.nextInt(shops.size())));
//        String productName =  productDtos.get(random.nextInt(productDtos.size())).getProductName();
//        ObjectId type = productDtos.get(random.nextInt(productDtos.size())).getType_id();
//        String shop = shops.get(random.nextInt(shops.size()));
//        int amount = random.nextInt(100);
        StopWatch watch = new StopWatch();
        watch.start();

        List<Document> listDTO = Stream.generate(() -> new ProductsInShopsDTO(
                        productDtos.get(random.nextInt(productDtos.size())),
                        shops.get(random.nextInt(shops.size())),
                        random.nextInt(100)))
                .limit(1000)
                .map(ProductsInShopsDTO::toDocument)
                .collect(Collectors.toCollection(LinkedList::new));
        watch.stop();
        double seconds = watch.getTime() / 1000.0;
        logRPS(listDTO, seconds);

        return listDTO;
    }

    public Document generateStoreDTO(MongoDatabase database, List<ProductDto> productDtos, List<String> shops) {

        return new ProductsInShopsDTO(
                productDtos.get(random.nextInt(productDtos.size())),
                shops.get(random.nextInt(shops.size())),
                random.nextInt(100)).toDocument();
    }


    private void logRPS(List<Document> listDTO, double seconds) {
        logger.info("Generated {} DTO", listDTO.size());
        logger.info("RPS of generating is {} ", seconds);
        logger.info("RPS DTO in second {} ", listDTO.size() / seconds);
    }

    protected String generateProductName() {
        return textGenerator();
    }

    public int generateRandomShopId(List<Integer> shopId) {
        int index = random.nextInt(shopId.size());
        return shopId.get(index);
    }

    public int generateRandomProductId(List<Integer> productId) {
        int index = random.nextInt(productId.size());
        return productId.get(index);
    }

    public ObjectId generateRandomTypeId(List<ObjectId> typeId) {
        int index = random.nextInt(typeId.size());
        return typeId.get(index);
    }

    protected String textGenerator() {

        int length = ThreadLocalRandom.current().nextInt(prodNameMinLength, prodNameMaxLength);
        int FIRST_LETTER = 97;
        int LAST_LETTER = 123;

        return ThreadLocalRandom.current()
                .ints(length - 1, FIRST_LETTER, LAST_LETTER)
                .mapToObj(codePoint -> String.valueOf((char) codePoint))
                .collect(Collectors.joining());
    }

    protected boolean isValid(ProductDto productDto) {

        Set<ConstraintViolation<ProductDto>> validateDTO = validator.validateDTO(productDto);
        return validateDTO.isEmpty();
    }
}


