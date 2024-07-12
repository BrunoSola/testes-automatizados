package com.brunosola.teste_de_integracao.service;

import com.brunosola.teste_de_integracao.dto.ProductDTO;
import com.brunosola.teste_de_integracao.repositories.ProductRepository;
import com.brunosola.teste_de_integracao.services.ProductService;
import com.brunosola.teste_de_integracao.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Test
    public void findAllShouldReturnPageWhenPage0Size4() {
        PageRequest pageRequest = PageRequest.of(0, 4);

        Page<ProductDTO> result = productService.findAll(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(4, result.getSize());
        Assertions.assertEquals(countTotalProduct, result.getTotalElements());
    }

    @Test
    public void findAllShouldReturnEmptyPageWhenPageDoesNotExist() {
        PageRequest pageRequest = PageRequest.of(10, 4);

        Page<ProductDTO> result = productService.findAll(pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("name"));

        Page<ProductDTO> result = productService.findAll(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(4, result.getSize());
        Assertions.assertEquals(countTotalProduct, result.getTotalElements());
        Assertions.assertEquals("Iphone 15 PRO", result.getContent().get(0).getName());
        Assertions.assertEquals("Iphone 15 PRO MAX", result.getContent().get(1).getName());
        Assertions.assertEquals("MacBook Air", result.getContent().get(2).getName());
        Assertions.assertEquals("MacBook Pro Apple 14", result.getContent().get(3).getName());
    }
}
