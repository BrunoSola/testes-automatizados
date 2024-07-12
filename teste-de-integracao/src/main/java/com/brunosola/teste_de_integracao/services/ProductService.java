package com.brunosola.teste_de_integracao.services;


import com.brunosola.teste_de_integracao.dto.ProductDTO;
import com.brunosola.teste_de_integracao.entities.Product;
import com.brunosola.teste_de_integracao.repositories.ProductRepository;
import com.brunosola.teste_de_integracao.services.exceptions.DatabaseException;
import com.brunosola.teste_de_integracao.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public static ModelMapper modelMapper = new ModelMapper();

    public static Product mapDTOToEntity(ProductDTO dto){
        return modelMapper.map(dto, Product.class);
    }

    public Product updateProductFromDTO(ProductDTO dto, Product prod){
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(dto, prod);
        return prod;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
        return new ProductDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductDTO::new);
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product prod = mapDTOToEntity(dto);
        prod = productRepository.save(prod);
        return new ProductDTO(prod);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product prod = productRepository.getReferenceById(id);
            prod = productRepository.save(updateProductFromDTO(dto,prod));
            return new ProductDTO(prod);
        }catch (EntityNotFoundException | MappingException e){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!productRepository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado.");
        }
        try {
            productRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial.");
        }
    }

}
