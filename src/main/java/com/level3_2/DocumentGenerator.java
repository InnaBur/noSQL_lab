package com.level3_2;

import com.level3_2.dao.ProductTypeDAO;
import com.level3_2.dto.ProductDto;
import com.level3_2.dto.ProductsInShopsDTO;
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
    private static final Logger logger = LoggerFactory.getLogger(DocumentGenerator.class);
    int prodNameMaxLength = Integer.parseInt(properties.getProperty("prodNameMaxLength"));
    int prodNameMinLength = Integer.parseInt(properties.getProperty("prodNameMinLength"));
    MyValidator validator = new MyValidator();
    ProductTypeDAO productTypeDAO = new ProductTypeDAO();
    Random random = new Random();

    public List<Document> generateProductsDTOlist(MongoDatabase database) {
        logger.debug("Generating products list");
        int quantityDTO = Integer.parseInt(properties.getProperty("quantityProducts"));

        List<ObjectId> typeIdList = productTypeDAO.getIdIntoList(database);

        StopWatch watch = new StopWatch();
        watch.start();
        List<Document> listDTO = generateList(quantityDTO, typeIdList);
        watch.stop();

        logRPS(listDTO, watch);
        return listDTO;
    }

    private List<Document> generateList(int quantityDTO, List<ObjectId> typeIdList) {
        return Stream.generate(() -> new ProductDto
                        (generateProductName(), generateRandomTypeId(typeIdList)))
                .filter(this::isValid)
                .limit(quantityDTO)
                .map(ProductDto::toDocument)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Document generateStoreDTO(List<ProductDto> productDtos, List<String> shops) {
        return new ProductsInShopsDTO(
                productDtos.get(random.nextInt(productDtos.size())),
                shops.get(random.nextInt(shops.size())),
                random.nextInt(100)).toDocument();
    }

    private void logRPS(List<Document> listDTO, StopWatch watch) {
        double seconds = watch.getTime() / 1000.0;
        logger.info("Generated {} DTO", listDTO.size());
        logger.info("RPS of generating is {} ", seconds);
        logger.info("RPS DTO in second {} ", listDTO.size() / seconds);
    }

    protected String generateProductName() {
        return textGenerator();
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


