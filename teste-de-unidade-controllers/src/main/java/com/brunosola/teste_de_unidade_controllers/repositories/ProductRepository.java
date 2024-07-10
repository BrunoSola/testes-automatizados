package com.brunosola.teste_de_unidade_controllers.repositories;

import com.brunosola.teste_de_unidade_controllers.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
