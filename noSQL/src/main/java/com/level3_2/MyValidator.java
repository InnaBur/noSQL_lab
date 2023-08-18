package com.level3_2;


import com.level3_2.dto.ProductDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class MyValidator {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    public Set<ConstraintViolation<ProductDto>> validateDTO(ProductDto productDto) {
        return validator.validate(productDto);
    }

}
