package com.brunosola.teste_de_integracao.repositories;

import com.brunosola.teste_de_integracao.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
