package com.brunosola.teste_de_integracao_web.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
}