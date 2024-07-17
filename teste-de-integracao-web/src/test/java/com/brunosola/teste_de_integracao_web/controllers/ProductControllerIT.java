package com.brunosola.teste_de_integracao_web.controllers;

import com.brunosola.teste_de_integracao_web.dto.ProductDTO;
import com.brunosola.teste_de_integracao_web.entities.Product;
import com.brunosola.teste_de_integracao_web.repositories.ProductRepository;
import com.brunosola.teste_de_integracao_web.services.exceptions.DatabaseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
        mockMvc.perform(get("/products?sort=name,desc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").value(countTotalProduct))
                .andExpect(jsonPath("$.content[0].name").value("MacBook Pro Apple 14"))
                .andExpect(jsonPath("$.content[1].name").value("MacBook Air"))
                .andExpect(jsonPath("$.content[2].name").value("Iphone 15 PRO MAX"))
                .andExpect(jsonPath("$.content[3].name").value("Iphone 15 PRO"));
    }

    @Test
    public void findByIdShouldReturnProductIdWhenIdExists() throws Exception {
        mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/products/{id}", nonExistingID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        ProductDTO productDTO = new ProductDTO(existingId, "Iphone 14 PRO","O Apple iPhone 14 Pro é um smartphone iOS com características inovadoras que o tornam uma excelente opção para qualquer tipo de utilização, representando um dos melhores dispositivos móveis já feitos. A tela de 6.1 polegadas coloca esse Apple no topo de sua categoria. A resolução também é alta: 2556x1179 pixel. As funcionalidades oferecidas pelo Apple iPhone 14 Pro são muitas e todas top de linha. Começando pelo 5G que permite a transferência de dados e excelente navegação na internet, além de conectividade Wi-fi e GPS presente no aparelho. Tem também leitor multimídia, videoconferência, e bluetooth. Enfatizamos a excelente memória interna de 1024 GB mas sem a possibilidade de expansão.A excelência deste Apple iPhone 14 Pro é completada por uma câmera de 48 megapixels que permite tirar fotos fantásticas com uma resolução de 8000x6000 pixels e gravar vídeos em 4K a espantosa resolução de 3840x2160 pixels. A espessura de 8.3 milímetros é realmente ótima e torna o Apple iPhone 14 Pro ainda mais espetacular.", 4500.00);
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}", existingId)
                    .content(jsonBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(productDTO.getName()))
                .andExpect(jsonPath("$.description").value(productDTO.getDescription()))
                .andExpect(jsonPath("$.price").value(productDTO.getPrice()));

    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ProductDTO productDTO = new ProductDTO(nonExistingID, "Iphone 14 PRO","O Apple iPhone 14 Pro é um smartphone iOS com características inovadoras que o tornam uma excelente opção para qualquer tipo de utilização, representando um dos melhores dispositivos móveis já feitos. A tela de 6.1 polegadas coloca esse Apple no topo de sua categoria. A resolução também é alta: 2556x1179 pixel. As funcionalidades oferecidas pelo Apple iPhone 14 Pro são muitas e todas top de linha. Começando pelo 5G que permite a transferência de dados e excelente navegação na internet, além de conectividade Wi-fi e GPS presente no aparelho. Tem também leitor multimídia, videoconferência, e bluetooth. Enfatizamos a excelente memória interna de 1024 GB mas sem a possibilidade de expansão.A excelência deste Apple iPhone 14 Pro é completada por uma câmera de 48 megapixels que permite tirar fotos fantásticas com uma resolução de 8000x6000 pixels e gravar vídeos em 4K a espantosa resolução de 3840x2160 pixels. A espessura de 8.3 milímetros é realmente ótima e torna o Apple iPhone 14 Pro ainda mais espetacular.", 4500.00);
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}", nonExistingID)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNothingWhenIdExists() throws Exception {
        mockMvc.perform(delete("/products/{id}", existingId)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/products/{id}", nonExistingID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}