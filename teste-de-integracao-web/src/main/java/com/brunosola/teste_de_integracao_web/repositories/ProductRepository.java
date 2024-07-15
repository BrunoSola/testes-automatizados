package com.brunosola.teste_de_integracao_web.repositories;

import com.brunosola.teste_de_integracao_web.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
