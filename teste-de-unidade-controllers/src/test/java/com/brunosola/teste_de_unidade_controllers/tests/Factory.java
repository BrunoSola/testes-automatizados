package com.brunosola.teste_de_unidade_controllers.tests;

import com.brunosola.teste_de_unidade_controllers.dto.ProductDTO;
import com.brunosola.teste_de_unidade_controllers.entities.Product;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(
                1L,
                "Iphone 15 PRO MAX",
                "O Apple iPhone 15 Pro Max é um smartphone iOS com características inovadoras que o tornam uma excelente opção para qualquer tipo de utilização, representando um dos melhores dispositivos móveis já feitos. A tela de 6.7 polegadas coloca esse Apple no topo de sua categoria. A resolução também é alta: 2796x1290 pixel. As funcionalidades oferecidas pelo Apple iPhone 15 Pro Max são muitas e todas top de linha. Começando pelo 5G que permite a transferência de dados e excelente navegação na internet, além de conectividade Wi-fi e GPS presente no aparelho. Tem também leitor multimídia, videoconferência, e bluetooth. Enfatizamos a excelente memória interna de 1024 GB mas sem a possibilidade de expansão.\n" +
                        "A excelência deste Apple iPhone 15 Pro Max é completada por uma câmera de 48 megapixels que permite tirar fotos fantásticas com uma resolução de 8000x6000 pixels e gravar vídeos em 4K a espantosa resolução de 3840x2160 pixels. A espessura de 8.3 milímetros é realmente ótima e torna o Apple iPhone 15 Pro Max ainda mais espetacular.",
                7000.00
        );
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product);
    }
}
