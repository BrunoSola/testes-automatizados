package com.brunosola.teste_de_unidade_services.services;

import com.brunosola.teste_de_unidade_services.dto.ProductDTO;
import com.brunosola.teste_de_unidade_services.entities.Product;
import com.brunosola.teste_de_unidade_services.repositories.ProductRepository;
import com.brunosola.teste_de_unidade_services.services.exceptions.DatabaseException;
import com.brunosola.teste_de_unidade_services.services.exceptions.ResourceNotFoundException;
import com.brunosola.teste_de_unidade_services.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
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

    private Long existingId;
    private Long nonExistingId;
    private PageImpl<Product> productPage;
    private Product product;
    private ProductDTO productDTO;
    private ModelMapper mapper;

    @BeforeEach
    void setUp(){
        existingId = 1L;
        nonExistingId = 2L;
        product = Factory.createProduct();
        productPage = new PageImpl<>(List.of(product));
        mapper = new ModelMapper();

        doNothing().when(productRepository).deleteById(existingId);
        doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(3L);

        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(productRepository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        when(productRepository.findAll((Pageable) any())).thenReturn(productPage);
        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.getReferenceById(existingId)).thenReturn(product);
        when(productRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        when(productRepository.existsById(existingId)).thenReturn(true);
        when(productRepository.existsById(nonExistingId)).thenReturn(false);
        when(productRepository.existsById(3L)).thenReturn(true);
    }

    @Test
    public void findByIdShouldReturnProductWhenExistingId(){
        productDTO = productService.findById(existingId);

        Assertions.assertNotNull(productDTO);
        Assertions.assertEquals("Iphone 15 PRO MAX", productDTO.getName());
        Assertions.assertEquals(7000.00, productDTO.getPrice());
        Assertions.assertEquals(product.getDescription(), productDTO.getDescription());

        verify(productRepository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenNonExistingId(){
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.findById(nonExistingId));
        verify(productRepository).findById(nonExistingId);
    }

    @Test
    public void findAllShouldReturnAllProductsPaged(){
        Pageable pageable = PageRequest.of(1, 10);
        Page<ProductDTO> products = productService.findAll(pageable);

        Assertions.assertNotNull(products);
        verify(productRepository).findAll(pageable);
    }

    @Test
    public void inserShouldInserNewProductWhenExistingId(){
        productDTO = new ProductDTO(3L,"PC GAMER", "Processador Ryzen 7 5700X3D", 10000.00);
        mapper.map(productDTO, product);
        ProductDTO prod = productService.insert(productDTO);

        Assertions.assertNotNull(prod);
        Assertions.assertEquals(3L, prod.getId());
        Assertions.assertEquals(productDTO.getName(), prod.getName());
        Assertions.assertEquals(productDTO.getDescription(), prod.getDescription());
        Assertions.assertEquals(productDTO.getPrice(), prod.getPrice());
        verify(productRepository).save(product);
    }

    @Test
    public void updateShouldUpdateProductWhenExistingId(){
        //Somente para confirmar que product está vindo conforme iniciado
        Assertions.assertEquals(1L, product.getId());
        Assertions.assertEquals("Iphone 15 PRO MAX", product.getName());

        productDTO = new ProductDTO(1L,"PC GAMER", "Processador Ryzen 7 5700X3D", 10000.00);
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(productDTO, product);
        ProductDTO prod = productService.update(existingId, productDTO);

        Assertions.assertNotNull(prod);
        Assertions.assertEquals(1L, prod.getId());
        Assertions.assertEquals("PC GAMER", prod.getName());
        Assertions.assertEquals(productDTO.getDescription(), prod.getDescription());
        Assertions.assertEquals(productDTO.getPrice(), prod.getPrice());
        verify(productRepository).save(product);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenNonExistingId(){
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.update(nonExistingId, productDTO));
        verify(productRepository).getReferenceById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenExistingId(){
        Assertions.assertDoesNotThrow(() ->  productService.delete(existingId));
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenNonExistingId(){
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.delete(nonExistingId));
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){
        Assertions.assertThrows(DatabaseException.class,
                () -> productService.delete(3L));
    }
}
