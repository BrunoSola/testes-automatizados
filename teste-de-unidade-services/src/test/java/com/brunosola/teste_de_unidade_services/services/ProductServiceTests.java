package com.brunosola.teste_de_unidade_services.services;

import com.brunosola.teste_de_unidade_services.dto.ProductDTO;
import com.brunosola.teste_de_unidade_services.entities.Product;
import com.brunosola.teste_de_unidade_services.repositories.ProductRepository;
import com.brunosola.teste_de_unidade_services.services.exceptions.ResourceNotFoundException;
import com.brunosola.teste_de_unidade_services.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private PageImpl<Product> productPage;
    private Product product;
    private ProductDTO productDTO;


    @BeforeEach
    void setUp(){
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        productPage = new PageImpl<>(List.of(product));

        doThrow(ResourceNotFoundException.class).when(productRepository).findById(2L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findAll((Pageable) any())).thenReturn(productPage);
    }

    @Test
    public void findByIdShouldReturnProductWhenExistingId(){
        productDTO = productService.findById(1L);

        Assertions.assertNotNull(productDTO);
        Assertions.assertEquals("Iphone 15 PRO MAX", productDTO.getName());
        Assertions.assertEquals(7000.00, productDTO.getPrice());
        Assertions.assertEquals(product.getDescription(), productDTO.getDescription());

        verify(productRepository).findById(1L);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenNonExistingId(){
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.findById(2L));
        verify(productRepository).findById(2L);
    }

    @Test
    public void findAllShouldReturnAllProductsPaged(){
        Pageable pageable = PageRequest.of(1, 10);
        Page<ProductDTO> products = productService.findAll(pageable);

        Assertions.assertNotNull(products);
        verify(productRepository).findAll(pageable);
    }
}
