package com.brunosola.teste_de_integracao.service;

import com.brunosola.teste_de_integracao.repositories.ProductRepository;
import com.brunosola.teste_de_integracao.services.ProductService;
import com.brunosola.teste_de_integracao.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingID;
    private Long countTotalProduct;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingID = 1000L;
        countTotalProduct = 4L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        productService.delete(existingId);

        Assertions.assertEquals(countTotalProduct - 1, productRepository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.delete(nonExistingID));
    }
}
